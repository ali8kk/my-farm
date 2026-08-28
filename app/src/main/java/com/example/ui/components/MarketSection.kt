package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnimalType
import com.example.model.CropType
import com.example.model.FactoryState
import com.example.model.FactoryType
import com.example.model.InventoryItem
import com.example.model.PlayerStats
import com.example.ui.theme.HarvestOnPrimary
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSecondaryContainer
import com.example.ui.theme.HarvestOnSurface
import com.example.ui.theme.HarvestOnSurfaceVariant
import com.example.ui.theme.HarvestOnTertiaryContainer
import com.example.ui.theme.HarvestOutlineVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSecondary
import com.example.ui.theme.HarvestSecondaryContainer
import com.example.ui.theme.HarvestSurfaceContainer
import com.example.ui.theme.HarvestSurfaceContainerHigh
import com.example.ui.theme.HarvestSurfaceContainerLow
import com.example.ui.theme.HarvestSurfaceContainerLowest
import com.example.ui.theme.HarvestSurfaceVariant
import com.example.ui.theme.HarvestTertiaryContainer

@Composable
fun MarketSection(
    inventory: Map<String, InventoryItem>,
    factories: List<FactoryState>,
    playerStats: PlayerStats,
    onSellItem: (String, Int) -> Unit,
    onBuySeed: (CropType, Int) -> Unit,
    onBuyAnimal: (AnimalType) -> Unit,
    onBuyFactory: (FactoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    var marketTab by remember { mutableStateOf(0) } // 0: الكل / بيع, 1: بذور, 2: حيوانات, 3: آلات

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("market_section")
    ) {
        // Categories Filter (الكل، بذور، حيوانات، آلات)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf("البيع والمنتجات", "بذور", "حيوانات", "آلات")
            tabs.forEachIndexed { index, title ->
                val isSelected = marketTab == index
                Surface(
                    onClick = { marketTab = index },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) HarvestPrimaryContainer else HarvestSurfaceContainerHigh,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) HarvestOnPrimaryContainer else HarvestOnSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (marketTab) {
            0 -> {
                // 1. Sell Items Grid (Tactile Cards 2-col)
                val itemsList = inventory.values.toList()
                val chunked = itemsList.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    chunked.forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowItems.forEach { item ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ModernTactileSellCard(
                                        item = item,
                                        onSell = { onSellItem(item.name, 1) }
                                    )
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            1 -> {
                // 2. Buy Seeds Grid (Tactile Cards 2-col)
                val cropList = CropType.values().toList()
                val chunked = cropList.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    chunked.forEach { rowCrops ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowCrops.forEach { crop ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ModernTactileBuySeedCard(
                                        crop = crop,
                                        playerGold = playerStats.gold,
                                        onBuy = { onBuySeed(crop, 5) }
                                    )
                                }
                            }
                            if (rowCrops.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            2 -> {
                // 3. Buy Animals Grid
                val animalsList = AnimalType.values().toList()
                val chunked = animalsList.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    chunked.forEach { rowAnimals ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowAnimals.forEach { animalType ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ModernTactileBuyAnimalCard(
                                        animalType = animalType,
                                        playerGold = playerStats.gold,
                                        playerLevel = playerStats.level,
                                        onBuy = { onBuyAnimal(animalType) }
                                    )
                                }
                            }
                            if (rowAnimals.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            3 -> {
                // 4. Buy Factories / Machines Grid
                val factoryList = FactoryType.values().toList()
                val chunked = factoryList.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    chunked.forEach { rowFactories ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowFactories.forEach { factoryType ->
                                val isOwned = factories.find { it.type == factoryType }?.isUnlocked == true
                                Box(modifier = Modifier.weight(1f)) {
                                    ModernTactileBuyFactoryCard(
                                        factoryType = factoryType,
                                        isOwned = isOwned,
                                        playerGold = playerStats.gold,
                                        playerLevel = playerStats.level,
                                        onBuy = { onBuyFactory(factoryType) }
                                    )
                                }
                            }
                            if (rowFactories.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTactileSellCard(
    item: InventoryItem,
    onSell: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .testTag("market_item_card_${item.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HarvestSecondaryContainer
                ) {
                    Text(
                        text = "متوفر: ${item.quantity}",
                        color = HarvestOnSecondaryContainer,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "معلومات",
                    tint = HarvestOutlineVariant,
                    modifier = Modifier.size(14.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HarvestPrimaryContainer.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.emoji, fontSize = 28.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    color = HarvestOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    onClick = onSell,
                    enabled = item.quantity > 0,
                    shape = RoundedCornerShape(8.dp),
                    color = HarvestSurfaceContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "بيع (+${item.currentMarketPrice})",
                            color = HarvestOnSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "🪙", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTactileBuySeedCard(
    crop: CropType,
    playerGold: Int,
    onBuy: () -> Unit
) {
    val cost5 = crop.seedCost * 5
    val canAfford = playerGold >= cost5

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .testTag("buy_seed_${crop.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HarvestSecondaryContainer
                ) {
                    Text(
                        text = "بذور",
                        color = HarvestOnSecondaryContainer,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "معلومات",
                    tint = HarvestOutlineVariant,
                    modifier = Modifier.size(14.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HarvestPrimaryContainer.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = crop.iconEmoji, fontSize = 28.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = crop.displayName,
                    color = HarvestOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    onClick = onBuy,
                    enabled = canAfford,
                    shape = RoundedCornerShape(8.dp),
                    color = if (canAfford) HarvestSurfaceContainer else HarvestSurfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "شراء 5 ($cost5)",
                            color = if (canAfford) HarvestOnSurfaceVariant else HarvestOutlineVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "🪙", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTactileBuyAnimalCard(
    animalType: AnimalType,
    playerGold: Int,
    playerLevel: Int,
    onBuy: () -> Unit
) {
    val isUnlocked = playerLevel >= animalType.unlockLevel
    val canAfford = playerGold >= animalType.buyCost && isUnlocked

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .testTag("buy_animal_${animalType.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HarvestTertiaryContainer.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "حيوان",
                        color = HarvestOnTertiaryContainer,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "معلومات",
                    tint = HarvestOutlineVariant,
                    modifier = Modifier.size(14.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HarvestTertiaryContainer.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = animalType.iconEmoji, fontSize = 28.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = animalType.displayName,
                    color = HarvestOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    onClick = onBuy,
                    enabled = canAfford,
                    shape = RoundedCornerShape(8.dp),
                    color = if (canAfford) HarvestSurfaceContainer else HarvestSurfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isUnlocked) "${animalType.buyCost}" else "مستوى ${animalType.unlockLevel}",
                            color = if (canAfford) HarvestOnSurfaceVariant else HarvestOutlineVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isUnlocked) {
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "🪙", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTactileBuyFactoryCard(
    factoryType: FactoryType,
    isOwned: Boolean,
    playerGold: Int,
    playerLevel: Int,
    onBuy: () -> Unit
) {
    val isUnlocked = playerLevel >= factoryType.unlockLevel
    val canAfford = playerGold >= factoryType.buildCost && isUnlocked && !isOwned

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .testTag("buy_factory_${factoryType.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HarvestSurfaceVariant
                ) {
                    Text(
                        text = "آلة",
                        color = HarvestOnSurfaceVariant,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "معلومات",
                    tint = HarvestOutlineVariant,
                    modifier = Modifier.size(14.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HarvestOutlineVariant.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = factoryType.iconEmoji, fontSize = 28.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = factoryType.displayName,
                    color = HarvestOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    onClick = onBuy,
                    enabled = canAfford,
                    shape = RoundedCornerShape(8.dp),
                    color = if (canAfford) HarvestSurfaceContainer else HarvestSurfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when {
                                isOwned -> "مملوكة ✓"
                                !isUnlocked -> "مستوى ${factoryType.unlockLevel}"
                                else -> "${factoryType.buildCost}"
                            },
                            color = if (canAfford) HarvestOnSurfaceVariant else HarvestOutlineVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (!isOwned && isUnlocked) {
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "🪙", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
