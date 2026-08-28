package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffectManager
import com.example.model.AnimalCardState
import com.example.model.AnimalStatus
import com.example.model.AnimalType
import com.example.model.CropType
import com.example.model.FactoryRecipe
import com.example.model.FactoryState
import com.example.model.FactoryStatus
import com.example.model.FactoryType
import com.example.model.FarmPlot
import com.example.model.InventoryItem
import com.example.model.ItemCategory
import com.example.model.PlayerStats
import com.example.model.PlotStatus
import com.example.model.VillageOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class FarmGameViewModel : ViewModel() {

    // Player Stats
    private val _playerStats = MutableStateFlow(
        PlayerStats(
            gold = 1500,
            level = 1,
            currentExp = 20,
            maxExpForLevel = 80,
            inventoryCapacity = 100,
            farmName = "مزرعة الهدوء الريفي",
            weather = "مشمس ولطيف ☀️",
            weatherBonus = "+15% سرعة نمو"
        )
    )
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    // 6 Farm Plots
    private val _plots = MutableStateFlow(
        listOf(
            FarmPlot(id = 1, name = "حقل #1", level = 1, cropType = CropType.WHEAT, status = PlotStatus.READY, secondsRemaining = 0, totalSeconds = 6),
            FarmPlot(id = 2, name = "حقل #2", level = 1, cropType = CropType.CORN, status = PlotStatus.GROWING, secondsRemaining = 8, totalSeconds = 15),
            FarmPlot(id = 3, name = "حقل #3", level = 1, cropType = CropType.CARROT, status = PlotStatus.GROWING, secondsRemaining = 18, totalSeconds = 25),
            FarmPlot(id = 4, name = "حقل #4", level = 1, cropType = null, status = PlotStatus.EMPTY),
            FarmPlot(id = 5, name = "حقل #5", level = 1, cropType = null, status = PlotStatus.EMPTY),
            FarmPlot(id = 6, name = "حقل #6", level = 1, cropType = null, status = PlotStatus.EMPTY)
        )
    )
    val plots: StateFlow<List<FarmPlot>> = _plots.asStateFlow()

    // Animals
    private val _animals = MutableStateFlow(
        listOf(
            AnimalCardState(id = "animal_chicken_1", type = AnimalType.CHICKEN, level = 1, status = AnimalStatus.PRODUCING, secondsRemaining = 6, totalSeconds = 12),
            AnimalCardState(id = "animal_cow_1", type = AnimalType.COW, level = 1, status = AnimalStatus.READY_TO_COLLECT, secondsRemaining = 0, totalSeconds = 22),
            AnimalCardState(id = "animal_goat_1", type = AnimalType.GOAT, level = 1, status = AnimalStatus.NEEDS_FEED, secondsRemaining = 0, totalSeconds = 32),
            AnimalCardState(id = "animal_sheep_1", type = AnimalType.SHEEP, level = 1, status = AnimalStatus.NEEDS_FEED, secondsRemaining = 0, totalSeconds = 45)
        )
    )
    val animals: StateFlow<List<AnimalCardState>> = _animals.asStateFlow()

    // Factory Recipes
    val recipes = listOf(
        // Bakery recipes
        FactoryRecipe("recipe_bread", "خبز طازج", FactoryType.BAKERY.id, 14, mapOf("قمح" to 2, "بيض" to 1), "خبز", "🍞", 1, 110, 35),
        FactoryRecipe("recipe_cake", "كعكة ريفية", FactoryType.BAKERY.id, 24, mapOf("قمح" to 3, "بيض" to 2, "سكر نقي" to 1), "كعكة", "🎂", 1, 280, 80),

        // Dairy recipes
        FactoryRecipe("recipe_cheese", "جبنة طبيعية", FactoryType.DAIRY.id, 18, mapOf("حليب" to 2), "جبن مائدة", "🧀", 1, 140, 45),
        FactoryRecipe("recipe_butter", "زبدة مزارع", FactoryType.DAIRY.id, 22, mapOf("حليب" to 2), "زبدة", "🧈", 1, 170, 50),

        // Sugar Mill recipes
        FactoryRecipe("recipe_sugar", "سكر نقي", FactoryType.SUGAR_MILL.id, 16, mapOf("قصب السكر" to 2), "سكر نقي", "🍬", 2, 160, 45),
        FactoryRecipe("recipe_honey_syrup", "قطر العسل", FactoryType.SUGAR_MILL.id, 26, mapOf("عسل نقي" to 1, "سكر نقي" to 1), "قطر العسل", "🍯", 1, 320, 90),

        // Jam & Pastry
        FactoryRecipe("recipe_strawberry_jam", "مربى فراولة", FactoryType.JAM_FACTORY.id, 22, mapOf("فراولة" to 2, "سكر نقي" to 1), "مربى فراولة", "🍓", 1, 350, 95),
        FactoryRecipe("recipe_fruit_pie", "فطيرة التوت والخبز", FactoryType.JAM_FACTORY.id, 35, mapOf("خبز" to 1, "مربى فراولة" to 1, "زبدة" to 1), "فطيرة فاخرة", "🧁", 1, 620, 160)
    )

    // Factories State
    private val _factories = MutableStateFlow(
        listOf(
            FactoryState(type = FactoryType.BAKERY, isUnlocked = true, status = FactoryStatus.IDLE, totalSeconds = 14, level = 1),
            FactoryState(type = FactoryType.DAIRY, isUnlocked = true, status = FactoryStatus.IDLE, totalSeconds = 18, level = 1),
            FactoryState(type = FactoryType.SUGAR_MILL, isUnlocked = false, status = FactoryStatus.LOCKED, totalSeconds = 16, level = 1),
            FactoryState(type = FactoryType.JAM_FACTORY, isUnlocked = false, status = FactoryStatus.LOCKED, totalSeconds = 22, level = 1)
        )
    )
    val factories: StateFlow<List<FactoryState>> = _factories.asStateFlow()

    // Village Orders Board
    private val _villageOrders = MutableStateFlow(
        listOf(
            VillageOrder("order_1", "العم سالم (الخباز)", "👨‍🍳", mapOf("قمح" to 4, "بيض" to 2), 180, 50),
            VillageOrder("order_2", "أم أحمد (صاحبة النزل)", "👵", mapOf("حليب" to 3, "خبز" to 1), 260, 75),
            VillageOrder("order_3", "تاجر السوق الكبير", "🧔‍♂️", mapOf("ذرة" to 4, "جزر" to 3), 320, 90)
        )
    )
    val villageOrders: StateFlow<List<VillageOrder>> = _villageOrders.asStateFlow()

    // Inventory items
    private val _inventory = MutableStateFlow(
        mapOf(
            "قمح" to InventoryItem("قمح", "🌾", 16, 16, 18, ItemCategory.CROP),
            "ذرة" to InventoryItem("ذرة", "🌽", 10, 38, 42, ItemCategory.CROP),
            "جزر" to InventoryItem("جزر", "🥕", 6, 65, 60, ItemCategory.CROP),
            "بيض" to InventoryItem("بيض", "🥚", 8, 24, 28, ItemCategory.ANIMAL_PRODUCT),
            "حليب" to InventoryItem("حليب", "🥛", 5, 48, 52, ItemCategory.ANIMAL_PRODUCT),
            "صوف" to InventoryItem("صوف", "🧶", 2, 105, 115, ItemCategory.ANIMAL_PRODUCT),
            "خبز" to InventoryItem("خبز", "🍞", 3, 110, 125, ItemCategory.PROCESSED)
        )
    )
    val inventory: StateFlow<Map<String, InventoryItem>> = _inventory.asStateFlow()

    val totalInventoryCount: Int
        get() = _inventory.value.values.sumOf { it.quantity }

    init {
        startTickEngine()
        startMarketPriceFluctuation()
    }

    private fun startTickEngine() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                tickSecond()
            }
        }
    }

    private fun startMarketPriceFluctuation() {
        viewModelScope.launch {
            while (true) {
                delay(20000)
                _inventory.update { currentMap ->
                    currentMap.mapValues { (_, item) ->
                        val variancePercent = Random.nextInt(-20, 26)
                        val newPrice = ((item.basePrice * (100 + variancePercent)) / 100).coerceAtLeast(1)
                        item.copy(currentMarketPrice = newPrice)
                    }
                }
            }
        }
    }

    private fun tickSecond() {
        // 1. Tick Crops
        _plots.update { currentPlots ->
            currentPlots.map { plot ->
                if (plot.status == PlotStatus.GROWING && plot.secondsRemaining > 0) {
                    val remaining = plot.secondsRemaining - 1
                    if (remaining <= 0) {
                        plot.copy(status = PlotStatus.READY, secondsRemaining = 0)
                    } else {
                        plot.copy(secondsRemaining = remaining)
                    }
                } else {
                    plot
                }
            }
        }

        // 2. Tick Animals
        _animals.update { currentAnimals ->
            currentAnimals.map { animal ->
                if (animal.status == AnimalStatus.PRODUCING && animal.secondsRemaining > 0) {
                    val remaining = animal.secondsRemaining - 1
                    if (remaining <= 0) {
                        animal.copy(status = AnimalStatus.READY_TO_COLLECT, secondsRemaining = 0)
                    } else {
                        animal.copy(secondsRemaining = remaining)
                    }
                } else {
                    animal
                }
            }
        }

        // 3. Tick Factories
        _factories.update { currentFactories ->
            currentFactories.map { factory ->
                if (factory.status == FactoryStatus.PROCESSING && factory.secondsRemaining > 0) {
                    val remaining = factory.secondsRemaining - 1
                    if (remaining <= 0) {
                        factory.copy(status = FactoryStatus.READY_TO_COLLECT, secondsRemaining = 0)
                    } else {
                        factory.copy(secondsRemaining = remaining)
                    }
                } else {
                    factory
                }
            }
        }
    }

    // --- Crops & Plots Operations ---

    fun plantCrop(plotId: Int, crop: CropType) {
        val currentStats = _playerStats.value
        if (currentStats.gold < crop.seedCost) return

        // Apply plot level speed & weather bonus (-15%)
        val plot = _plots.value.find { it.id == plotId } ?: return
        val calculatedDuration = (crop.baseGrowthDurationSeconds * plot.speedMultiplier * 0.85f).toInt().coerceAtLeast(3)

        _playerStats.update { it.copy(gold = it.gold - crop.seedCost) }
        _plots.update { currentPlots ->
            currentPlots.map {
                if (it.id == plotId) {
                    it.copy(
                        cropType = crop,
                        status = PlotStatus.GROWING,
                        secondsRemaining = calculatedDuration,
                        totalSeconds = calculatedDuration
                    )
                } else it
            }
        }
    }

    fun harvestPlot(plotId: Int) {
        val plot = _plots.value.find { it.id == plotId } ?: return
        if (plot.status != PlotStatus.READY) return

        val crop = plot.cropType ?: return
        val yieldQty = plot.yieldMultiplier

        // Add to inventory
        addItemToInventory(crop.displayName, crop.iconEmoji, yieldQty, crop.harvestGold, ItemCategory.CROP)
        addExp(crop.harvestExp)

        _plots.update { currentPlots ->
            currentPlots.map {
                if (it.id == plotId) {
                    it.copy(cropType = null, status = PlotStatus.EMPTY, secondsRemaining = 0, totalSeconds = 0)
                } else it
            }
        }
    }

    fun harvestAllReadyPlots() {
        val readyPlots = _plots.value.filter { it.status == PlotStatus.READY }
        if (readyPlots.isEmpty()) return

        SoundEffectManager.playHarvestSound()
        readyPlots.forEach { plot ->
            harvestPlot(plot.id)
        }
    }

    fun upgradePlot(plotId: Int) {
        val plot = _plots.value.find { it.id == plotId } ?: return
        if (!plot.canUpgrade || _playerStats.value.gold < plot.upgradeCost) return

        val cost = plot.upgradeCost
        _playerStats.update { it.copy(gold = it.gold - cost) }
        _plots.update { currentPlots ->
            currentPlots.map {
                if (it.id == plotId) {
                    it.copy(level = it.level + 1)
                } else it
            }
        }
    }

    // --- Animal Operations ---

    fun feedAnimal(animalId: String) {
        val animal = _animals.value.find { it.id == animalId } ?: return
        if (animal.status != AnimalStatus.NEEDS_FEED) return

        val feedItem = animal.type.feedRequired
        val currentFeedQty = _inventory.value[feedItem]?.quantity ?: 0
        if (currentFeedQty < animal.type.feedRequiredQty) return

        // Deduct feed
        deductItemFromInventory(feedItem, animal.type.feedRequiredQty)

        val duration = (animal.type.baseDurationSeconds * animal.speedMultiplier).toInt()
        _animals.update { currentAnimals ->
            currentAnimals.map {
                if (it.id == animalId) {
                    it.copy(
                        status = AnimalStatus.PRODUCING,
                        secondsRemaining = duration,
                        totalSeconds = duration
                    )
                } else it
            }
        }
    }

    fun collectAnimalOutput(animalId: String) {
        val animal = _animals.value.find { it.id == animalId } ?: return
        if (animal.status != AnimalStatus.READY_TO_COLLECT) return

        val outputQty = animal.outputQuantity
        addItemToInventory(
            animal.type.outputItemName,
            animal.type.outputEmoji,
            outputQty,
            animal.type.goldValue,
            ItemCategory.ANIMAL_PRODUCT
        )
        addExp(animal.type.expValue)

        _animals.update { currentAnimals ->
            currentAnimals.map {
                if (it.id == animalId) {
                    it.copy(status = AnimalStatus.NEEDS_FEED, secondsRemaining = 0, totalSeconds = 0)
                } else it
            }
        }
    }

    fun upgradeAnimal(animalId: String) {
        val animal = _animals.value.find { it.id == animalId } ?: return
        if (!animal.canUpgrade || _playerStats.value.gold < animal.upgradeCost) return

        val cost = animal.upgradeCost
        _playerStats.update { it.copy(gold = it.gold - cost) }
        _animals.update { currentAnimals ->
            currentAnimals.map {
                if (it.id == animalId) {
                    it.copy(level = it.level + 1)
                } else it
            }
        }
    }

    fun buyNewAnimal(type: AnimalType) {
        val currentStats = _playerStats.value
        if (currentStats.gold < type.buyCost || currentStats.level < type.unlockLevel) return

        _playerStats.update { it.copy(gold = it.gold - type.buyCost) }
        val newAnimal = AnimalCardState(
            id = "animal_${type.id}_${System.currentTimeMillis() % 10000}",
            type = type,
            level = 1,
            status = AnimalStatus.NEEDS_FEED
        )
        _animals.update { it + newAnimal }
    }

    // --- Factory Operations ---

    fun unlockFactory(type: FactoryType) {
        val currentStats = _playerStats.value
        if (currentStats.gold < type.buildCost || currentStats.level < type.unlockLevel) return

        _playerStats.update { it.copy(gold = it.gold - type.buildCost) }
        _factories.update { currentFactories ->
            currentFactories.map {
                if (it.type == type) {
                    it.copy(isUnlocked = true, status = FactoryStatus.IDLE)
                } else it
            }
        }
    }

    fun startFactoryProduction(type: FactoryType, recipeId: String) {
        val factory = _factories.value.find { it.type == type } ?: return
        if (!factory.isUnlocked || factory.status != FactoryStatus.IDLE) return

        val recipe = recipes.find { it.id == recipeId } ?: return
        // Check ingredients
        val hasAll = recipe.requiredIngredients.all { (item, needed) ->
            (_inventory.value[item]?.quantity ?: 0) >= needed
        }
        if (!hasAll) return

        // Deduct ingredients
        recipe.requiredIngredients.forEach { (item, needed) ->
            deductItemFromInventory(item, needed)
        }

        val duration = recipe.durationSeconds
        _factories.update { currentFactories ->
            currentFactories.map {
                if (it.type == type) {
                    it.copy(
                        status = FactoryStatus.PROCESSING,
                        activeRecipeId = recipeId,
                        secondsRemaining = duration,
                        totalSeconds = duration
                    )
                } else it
            }
        }
    }

    fun collectFactoryProduct(type: FactoryType) {
        val factory = _factories.value.find { it.type == type } ?: return
        if (factory.status != FactoryStatus.READY_TO_COLLECT) return

        val recipe = recipes.find { it.id == factory.activeRecipeId } ?: return
        addItemToInventory(recipe.outputItemName, recipe.outputEmoji, recipe.outputQty, recipe.goldValue, ItemCategory.PROCESSED)
        addExp(recipe.expValue)

        _factories.update { currentFactories ->
            currentFactories.map {
                if (it.type == type) {
                    it.copy(status = FactoryStatus.IDLE, activeRecipeId = null, secondsRemaining = 0, totalSeconds = 0)
                } else it
            }
        }
    }

    fun upgradeFactory(type: FactoryType) {
        val factory = _factories.value.find { it.type == type } ?: return
        val cost = type.buildCost
        if (_playerStats.value.gold < cost) return

        _playerStats.update { it.copy(gold = it.gold - cost) }
        _factories.update { currentFactories ->
            currentFactories.map {
                if (it.type == type) {
                    it.copy(level = it.level + 1)
                } else it
            }
        }
    }

    // --- Village Orders Board ---

    fun fulfillVillageOrder(orderId: String) {
        val order = _villageOrders.value.find { it.id == orderId } ?: return
        val hasAll = order.requiredItems.all { (item, needed) ->
            (_inventory.value[item]?.quantity ?: 0) >= needed
        }
        if (!hasAll) return

        // Deduct items
        order.requiredItems.forEach { (item, needed) ->
            deductItemFromInventory(item, needed)
        }

        // Reward gold & exp
        _playerStats.update { it.copy(gold = it.gold + order.rewardGold) }
        addExp(order.rewardExp)

        // Generate a new refreshed order
        val allCrops = listOf("قمح", "ذرة", "جزر", "بيض", "حليب", "خبز")
        val randomItem1 = allCrops.random()
        val randomItem2 = allCrops.random()
        val newReqs = mutableMapOf(randomItem1 to Random.nextInt(2, 5))
        if (randomItem1 != randomItem2) {
            newReqs[randomItem2] = Random.nextInt(1, 3)
        }

        val newOrder = VillageOrder(
            id = "order_${System.currentTimeMillis() % 10000}",
            requesterName = listOf("العم صالح", "السيدة مريم", "تاجر القرية", "سائق الشاحنة").random(),
            requesterAvatar = listOf("👨‍🌾", "👩‍🌾", "🧔‍♂️", "👵").random(),
            requiredItems = newReqs,
            rewardGold = Random.nextInt(200, 450),
            rewardExp = Random.nextInt(50, 110)
        )

        _villageOrders.update { currentOrders ->
            currentOrders.map { if (it.id == orderId) newOrder else it }
        }
    }

    // --- Market & Inventory Operations ---

    fun sellItem(itemName: String, quantity: Int) {
        val item = _inventory.value[itemName] ?: return
        if (item.quantity < quantity || quantity <= 0) return

        val earnedGold = item.currentMarketPrice * quantity
        deductItemFromInventory(itemName, quantity)
        _playerStats.update { it.copy(gold = it.gold + earnedGold) }
    }

    fun buySeedToInventory(crop: CropType, quantity: Int = 5) {
        val totalCost = crop.seedCost * quantity
        if (_playerStats.value.gold < totalCost) return

        _playerStats.update { it.copy(gold = it.gold - totalCost) }
        addItemToInventory(crop.displayName, crop.iconEmoji, quantity, crop.harvestGold, ItemCategory.CROP)
    }

    fun upgradeInventoryCapacity() {
        val cost = 250
        if (_playerStats.value.gold < cost) return

        _playerStats.update {
            it.copy(
                gold = it.gold - cost,
                inventoryCapacity = it.inventoryCapacity + 25
            )
        }
    }

    private fun addItemToInventory(name: String, emoji: String, qty: Int, basePrice: Int, category: ItemCategory) {
        _inventory.update { currentMap ->
            val existing = currentMap[name]
            val updatedItem = if (existing != null) {
                existing.copy(quantity = existing.quantity + qty)
            } else {
                InventoryItem(name, emoji, qty, basePrice, basePrice, category)
            }
            currentMap + (name to updatedItem)
        }
    }

    private fun deductItemFromInventory(name: String, qty: Int) {
        _inventory.update { currentMap ->
            val existing = currentMap[name] ?: return@update currentMap
            val newQty = (existing.quantity - qty).coerceAtLeast(0)
            currentMap + (name to existing.copy(quantity = newQty))
        }
    }

    private fun addExp(amount: Int) {
        _playerStats.update { stats ->
            var exp = stats.currentExp + amount
            var level = stats.level
            var maxExp = stats.maxExpForLevel

            while (exp >= maxExp) {
                exp -= maxExp
                level += 1
                maxExp = (maxExp * 1.5).toInt()
            }

            stats.copy(level = level, currentExp = exp, maxExpForLevel = maxExp)
        }
    }
}
