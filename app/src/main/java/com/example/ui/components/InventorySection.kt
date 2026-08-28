package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.model.InventoryItem
import com.example.model.ItemCategory
import com.example.model.PlayerStats
import com.example.ui.theme.HarvestOnPrimary
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSecondaryContainer
import com.example.ui.theme.HarvestOnSurface
import com.example.ui.theme.HarvestOnSurfaceVariant
import com.example.ui.theme.HarvestOutlineVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSecondaryContainer
import com.example.ui.theme.HarvestSurfaceContainer
import com.example.ui.theme.HarvestSurfaceContainerHigh
import com.example.ui.theme.HarvestSurfaceContainerLow
import com.example.ui.theme.HarvestSurfaceContainerLowest
import com.example.ui.theme.HarvestSurfaceDim
import com.example.ui.theme.HarvestSurfaceVariant

@Composable
fun InventorySection(
    inventory: Map<String, InventoryItem>,
    playerStats: PlayerStats,
    onUpgradeInventory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalStoredCount = inventory.values.sumOf { it.quantity }
    val capacityRatio = (totalStoredCount.toFloat() / playerStats.inventoryCapacity.toFloat()).coerceIn(0f, 1f)

    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("inventory_section")
    ) {
        // Warehouse Capacity Indicator Box (lifted-shadow style)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLow),
            border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "سعة المخزن",
                            color = HarvestOnSurface,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$totalStoredCount / ${playerStats.inventoryCapacity} وحدة",
                            color = HarvestOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onUpgradeInventory,
                        enabled = playerStats.gold >= 250,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HarvestPrimary,
                            contentColor = HarvestOnPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("upgrade_capacity_button_section")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Upgrade,
                                contentDescription = "توسيع",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "+25 (250 🪙)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { capacityRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = HarvestPrimary,
                    trackColor = HarvestSurfaceDim
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Categories filter tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val isAll = selectedCategory == null
            Surface(
                onClick = { selectedCategory = null },
                shape = RoundedCornerShape(50),
                color = if (isAll) HarvestPrimaryContainer else HarvestSurfaceContainerHigh
            ) {
                Text(
                    text = "الكل",
                    color = if (isAll) HarvestOnPrimaryContainer else HarvestOnSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            ItemCategory.values().forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    onClick = { selectedCategory = cat },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) HarvestPrimaryContainer else HarvestSurfaceContainerHigh
                ) {
                    Text(
                        text = cat.displayName,
                        color = if (isSelected) HarvestOnPrimaryContainer else HarvestOnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filtered Inventory Items Grid
        val allItems = inventory.values.toList()
        val filteredItems = if (selectedCategory == null) allItems else allItems.filter { it.category == selectedCategory }

        val chunkedItems = filteredItems.chunked(2)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            chunkedItems.forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            ModernInventoryCard(item = item)
                        }
                    }
                    if (rowItems.size == 1) {
                        // Empty Slot Placeholder
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(130.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = HarvestSurfaceVariant.copy(alpha = 0.3f),
                            border = BorderStroke(1.dp, HarvestOutlineVariant)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(HarvestSurfaceDim),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "فارغ",
                                        tint = HarvestOnSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "مساحة فارغة",
                                    color = HarvestOnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernInventoryCard(item: InventoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .testTag("inventory_item_${item.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Count pill on top-right
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(50),
                color = HarvestPrimaryContainer
            ) {
                Text(
                    text = "x${item.quantity}",
                    color = HarvestOnPrimaryContainer,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(HarvestSurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.emoji, fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.name,
                    color = HarvestOnSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
