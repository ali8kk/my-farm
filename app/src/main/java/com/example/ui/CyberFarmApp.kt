package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.FactorySection
import com.example.ui.components.FarmFieldsSection
import com.example.ui.components.InventorySection
import com.example.ui.components.LivestockSection
import com.example.ui.components.MarketSection
import com.example.ui.components.PlantSeedModal
import com.example.ui.components.TopHeaderBar
import com.example.ui.components.VillageOrdersBoardSection
import com.example.ui.theme.CyberFarmTheme
import com.example.ui.theme.HarvestBackground
import com.example.ui.theme.HarvestBarBorder
import com.example.ui.theme.HarvestBarSurface
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSurfaceVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSurfaceContainerLowest
import com.example.viewmodel.FarmGameViewModel

@Composable
fun CyberFarmApp(
    viewModel: FarmGameViewModel = viewModel()
) {
    CyberFarmTheme {
        val playerStats by viewModel.playerStats.collectAsState()
        val plots by viewModel.plots.collectAsState()
        val animals by viewModel.animals.collectAsState()
        val factories by viewModel.factories.collectAsState()
        val inventory by viewModel.inventory.collectAsState()
        val villageOrders by viewModel.villageOrders.collectAsState()

        // Modal states
        var showPlantModalForPlotId by remember { mutableStateOf<Int?>(null) }
        var selectedTab by remember { mutableStateOf(0) } // 0: الحقل, 1: المهام, 2: الحيوانات, 3: الآلات, 4: المتجر, 5: المخزن

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(HarvestBackground)
                .statusBarsPadding(),
            containerColor = HarvestBackground,
            bottomBar = {
                Surface(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    color = HarvestBarSurface,
                    border = BorderStroke(1.5.dp, HarvestBarBorder)
                ) {
                    NavigationBar(
                        containerColor = HarvestBarSurface,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .height(72.dp)
                            .testTag("bottom_nav")
                    ) {
                        // 1. الحقل (Agriculture)
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(imageVector = Icons.Default.Agriculture, contentDescription = "الحقل") },
                            label = { Text("الحقل", fontSize = 10.sp, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HarvestOnPrimaryContainer,
                                selectedTextColor = HarvestPrimary,
                                indicatorColor = HarvestPrimaryContainer.copy(alpha = 0.45f),
                                unselectedIconColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f),
                                unselectedTextColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f)
                            )
                        )

                        // 2. المهام (Tasks / Orders)
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(imageVector = Icons.Default.Assignment, contentDescription = "المهام") },
                            label = { Text("المهام", fontSize = 10.sp, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HarvestOnPrimaryContainer,
                                selectedTextColor = HarvestPrimary,
                                indicatorColor = HarvestPrimaryContainer.copy(alpha = 0.45f),
                                unselectedIconColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f),
                                unselectedTextColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f)
                            )
                        )

                        // 3. الحيوانات (Livestock)
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(imageVector = Icons.Default.Pets, contentDescription = "الحيوانات") },
                            label = { Text("الحيوانات", fontSize = 10.sp, fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HarvestOnPrimaryContainer,
                                selectedTextColor = HarvestPrimary,
                                indicatorColor = HarvestPrimaryContainer.copy(alpha = 0.45f),
                                unselectedIconColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f),
                                unselectedTextColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f)
                            )
                        )

                        // 4. الآلات (Machines)
                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            icon = { Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = "الآلات") },
                            label = { Text("الآلات", fontSize = 10.sp, fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HarvestOnPrimaryContainer,
                                selectedTextColor = HarvestPrimary,
                                indicatorColor = HarvestPrimaryContainer.copy(alpha = 0.45f),
                                unselectedIconColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f),
                                unselectedTextColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f)
                            )
                        )

                        // 5. المتجر (Store)
                        NavigationBarItem(
                            selected = selectedTab == 4,
                            onClick = { selectedTab = 4 },
                            icon = { Icon(imageVector = Icons.Default.Storefront, contentDescription = "المتجر") },
                            label = { Text("المتجر", fontSize = 10.sp, fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HarvestOnPrimaryContainer,
                                selectedTextColor = HarvestPrimary,
                                indicatorColor = HarvestPrimaryContainer.copy(alpha = 0.45f),
                                unselectedIconColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f),
                                unselectedTextColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f)
                            )
                        )

                        // 6. المخزن (Inventory)
                        NavigationBarItem(
                            selected = selectedTab == 5,
                            onClick = { selectedTab = 5 },
                            icon = { Icon(imageVector = Icons.Default.Inventory2, contentDescription = "المخزن") },
                            label = { Text("المخزن", fontSize = 10.sp, fontWeight = if (selectedTab == 5) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HarvestOnPrimaryContainer,
                                selectedTextColor = HarvestPrimary,
                                indicatorColor = HarvestPrimaryContainer.copy(alpha = 0.45f),
                                unselectedIconColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f),
                                unselectedTextColor = HarvestOnSurfaceVariant.copy(alpha = 0.85f)
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
                    val usedInventoryCount = inventory.values.sumOf { it.quantity }

                    // 1. Top Modern Header Bar
                    TopHeaderBar(
                        playerStats = playerStats,
                        usedInventoryCount = usedInventoryCount
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Tab Views matching order: الحقل، المهام، الحيوانات، الآلات، المتجر، المخزن
                    when (selectedTab) {
                        0 -> {
                            // 1. الحقل (Active Fields)
                            FarmFieldsSection(
                                plots = plots,
                                playerGold = playerStats.gold,
                                onHarvestPlot = { plotId -> viewModel.harvestPlot(plotId) },
                                onHarvestAllReady = { viewModel.harvestAllReadyPlots() },
                                onOpenPlantModal = { plotId -> showPlantModalForPlotId = plotId },
                                onUpgradePlot = { plotId -> viewModel.upgradePlot(plotId) }
                            )
                        }

                        1 -> {
                            // 2. المهام (Village Orders / Tasks)
                            VillageOrdersBoardSection(
                                orders = villageOrders,
                                inventory = inventory,
                                onFulfillOrder = { orderId -> viewModel.fulfillVillageOrder(orderId) }
                            )
                        }

                        2 -> {
                            // 3. الحيوانات (Barn / Animals)
                            LivestockSection(
                                animals = animals,
                                playerGold = playerStats.gold,
                                onFeedAnimal = { animalId -> viewModel.feedAnimal(animalId) },
                                onCollectAnimal = { animalId -> viewModel.collectAnimalOutput(animalId) },
                                onUpgradeAnimal = { animalId -> viewModel.upgradeAnimal(animalId) }
                            )
                        }

                        3 -> {
                            // 4. الآلات (Production Machines / Factories)
                            FactorySection(
                                factories = factories,
                                recipes = viewModel.recipes,
                                inventory = inventory,
                                onStartProduction = { type, recipeId -> viewModel.startFactoryProduction(type, recipeId) },
                                onCollectProduct = { type -> viewModel.collectFactoryProduct(type) }
                            )
                        }

                        4 -> {
                            // 5. المتجر (Store / Market)
                            MarketSection(
                                inventory = inventory,
                                factories = factories,
                                playerStats = playerStats,
                                onSellItem = { itemName, qty -> viewModel.sellItem(itemName, qty) },
                                onBuySeed = { cropType, qty -> viewModel.buySeedToInventory(cropType, qty) },
                                onBuyAnimal = { animalType -> viewModel.buyNewAnimal(animalType) },
                                onBuyFactory = { factoryType -> viewModel.unlockFactory(factoryType) }
                            )
                        }

                        5 -> {
                            // 6. المخزن (Inventory / Warehouse)
                            InventorySection(
                                inventory = inventory,
                                playerStats = playerStats,
                                onUpgradeInventory = { viewModel.upgradeInventoryCapacity() }
                            )
                        }
                    }
                }
            }
        }

        // Modal for planting seeds
        showPlantModalForPlotId?.let { plotId ->
            PlantSeedModal(
                plotId = plotId,
                playerGold = playerStats.gold,
                playerLevel = playerStats.level,
                onSelectCrop = { cropType ->
                    viewModel.plantCrop(plotId, cropType)
                },
                onDismiss = { showPlantModalForPlotId = null }
            )
        }
    }
}
