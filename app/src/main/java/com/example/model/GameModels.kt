package com.example.model

enum class CropType(
    val id: String,
    val displayName: String,
    val baseGrowthDurationSeconds: Int,
    val seedCost: Int,
    val harvestGold: Int,
    val harvestExp: Int,
    val iconEmoji: String,
    val unlockLevel: Int = 1,
    val sproutEmoji: String = "🌱",
    val growingEmoji: String = "🌿"
) {
    WHEAT("wheat", "قمح", 6, 4, 16, 8, "🌾", 1, "🌱", "🌾"),
    CORN("corn", "ذرة", 15, 12, 38, 18, "🌽", 1, "🌱", "🌿"),
    CARROT("carrot", "جزر", 25, 20, 65, 28, "🥕", 2, "🌱", "🥕"),
    TOMATO("tomato", "طماطم", 40, 35, 110, 42, "🍅", 2, "🌱", "🌿"),
    SOYBEAN("soybean", "صويا", 60, 50, 160, 55, "🫘", 3, "🌱", "🌿"),
    STRAWBERRY("strawberry", "فراولة", 90, 80, 240, 75, "🍓", 3, "🌱", "🌸"),
    PUMPKIN("pumpkin", "يقطين", 130, 120, 360, 100, "🎃", 4, "🌱", "🌿"),
    SUGARCANE("sugarcane", "قصب السكر", 180, 160, 480, 130, "🎋", 4, "🌱", "🎋")
}

enum class PlotStatus {
    EMPTY,
    GROWING,
    READY
}

data class FarmPlot(
    val id: Int,
    val name: String,
    val level: Int = 1, // Level 1 (Normal), Level 2 (Fertile +20% speed & yield), Level 3 (Rich Soil +40% speed & 2x yield)
    val cropType: CropType? = null,
    val status: PlotStatus = PlotStatus.EMPTY,
    val secondsRemaining: Int = 0,
    val totalSeconds: Int = 0,
    val isWatered: Boolean = true // Soil watering visual state
) {
    val progress: Float
        get() = if (totalSeconds > 0) {
            1f - (secondsRemaining.toFloat() / totalSeconds.toFloat())
        } else 0f

    // 3 Growth Stages (0: Sprout 🌱, 1: Growing 🌿, 2: Ripe 🌾)
    val growthStage: Int
        get() = when {
            status == PlotStatus.READY -> 2
            status == PlotStatus.GROWING && progress >= 0.5f -> 1
            status == PlotStatus.GROWING -> 0
            else -> 0
        }

    val upgradeCost: Int
        get() = when (level) {
            1 -> 150
            2 -> 400
            else -> 0
        }

    val canUpgrade: Boolean
        get() = level < 3

    val speedMultiplier: Float
        get() = when (level) {
            1 -> 1.0f
            2 -> 0.8f // 20% faster
            3 -> 0.6f // 40% faster
            else -> 1.0f
        }

    val yieldMultiplier: Int
        get() = when (level) {
            3 -> 2
            else -> 1
        }
}

enum class AnimalType(
    val id: String,
    val displayName: String,
    val baseDurationSeconds: Int,
    val feedRequired: String, // e.g. "قمح"
    val feedRequiredQty: Int,
    val outputItemName: String,
    val outputEmoji: String,
    val iconEmoji: String,
    val goldValue: Int,
    val expValue: Int,
    val buyCost: Int,
    val unlockLevel: Int = 1
) {
    CHICKEN("chicken", "دجاجة المزرعة", 12, "قمح", 1, "بيض", "🥚", "🐔", 24, 12, 120, 1),
    COW("cow", "بقرة حلوب", 22, "ذرة", 1, "حليب", "🥛", "🐮", 48, 22, 280, 1),
    GOAT("goat", "ماعز الجبل", 32, "جزر", 1, "جبن ماعز", "🧀", "🐐", 72, 32, 450, 2),
    SHEEP("sheep", "خروف الصوف", 45, "صويا", 1, "صوف", "🧶", "🐑", 105, 45, 650, 2),
    BEE_HIVE("bee", "خلية النحل الذهبية", 60, "فراولة", 1, "عسل نقي", "🍯", "🐝", 150, 60, 900, 3)
}

enum class AnimalStatus {
    NEEDS_FEED,
    PRODUCING,
    READY_TO_COLLECT
}

data class AnimalCardState(
    val id: String,
    val type: AnimalType,
    val level: Int = 1,
    val status: AnimalStatus = AnimalStatus.NEEDS_FEED,
    val secondsRemaining: Int = 0,
    val totalSeconds: Int = 0
) {
    val progress: Float
        get() = if (totalSeconds > 0) {
            1f - (secondsRemaining.toFloat() / totalSeconds.toFloat())
        } else 0f

    val upgradeCost: Int
        get() = when (level) {
            1 -> type.buyCost
            2 -> type.buyCost * 2
            else -> 0
        }

    val canUpgrade: Boolean
        get() = level < 3

    val speedMultiplier: Float
        get() = when (level) {
            1 -> 1.0f
            2 -> 0.8f
            3 -> 0.6f
            else -> 1.0f
        }

    val outputQuantity: Int
        get() = when (level) {
            3 -> 2
            else -> 1
        }
}

enum class FactoryType(
    val id: String,
    val displayName: String,
    val iconEmoji: String,
    val description: String,
    val buildCost: Int,
    val unlockLevel: Int = 1
) {
    BAKERY("bakery", "مخبز القرية التقليدي", "🥖", "خبز ومعجنات ريفية طازجة من القمح والبيض", 200, 1),
    DAIRY("dairy", "معمل الألبان الطبيعي", "🧀", "صناعة الجبن والزبدة الفاخرة من حليب المزرعة", 400, 2),
    SUGAR_MILL("sugar_mill", "معصرة ومطحنة السكر", "🍬", "تكرير السكر النقي والعصائر الطبيعية", 650, 2),
    JAM_FACTORY("jam_factory", "معمل الحلويات والمربى", "🧁", "صناعة الفطائر والمربى باستخدام منتجات المخبز", 1000, 3)
}

data class FactoryRecipe(
    val id: String,
    val displayName: String,
    val factoryId: String,
    val durationSeconds: Int,
    val requiredIngredients: Map<String, Int>,
    val outputItemName: String,
    val outputEmoji: String,
    val outputQty: Int = 1,
    val goldValue: Int = 120,
    val expValue: Int = 50
)

enum class FactoryStatus {
    LOCKED,
    IDLE,
    PROCESSING,
    READY_TO_COLLECT
}

data class FactoryState(
    val type: FactoryType,
    val isUnlocked: Boolean = false,
    val activeRecipeId: String? = null,
    val status: FactoryStatus = FactoryStatus.LOCKED,
    val secondsRemaining: Int = 0,
    val totalSeconds: Int = 0,
    val level: Int = 1
) {
    val progress: Float
        get() = if (totalSeconds > 0) {
            1f - (secondsRemaining.toFloat() / totalSeconds.toFloat())
        } else 0f
}

data class InventoryItem(
    val name: String,
    val emoji: String,
    val quantity: Int,
    val basePrice: Int,
    val currentMarketPrice: Int,
    val category: ItemCategory = ItemCategory.CROP
)

enum class ItemCategory(val displayName: String, val icon: String) {
    CROP("المحاصيل", "🌾"),
    ANIMAL_PRODUCT("الحيوانات", "🥚"),
    PROCESSED("المصانع", "🥐")
}

// Village Delivery Orders Board System (طلبات أهل القرية وشاحنة التوصيل)
data class VillageOrder(
    val id: String,
    val requesterName: String,
    val requesterAvatar: String,
    val requiredItems: Map<String, Int>, // ItemName -> RequiredQty
    val rewardGold: Int,
    val rewardExp: Int,
    val isCompleted: Boolean = false
)

data class PlayerStats(
    val gold: Int = 1500,
    val level: Int = 1,
    val currentExp: Int = 20,
    val maxExpForLevel: Int = 80,
    val inventoryCapacity: Int = 100,
    val farmName: String = "مزرعة الهدوء الريفي",
    val weather: String = "مشمس ولطيف ☀️",
    val weatherBonus: String = "+15% سرعة نمو"
)
