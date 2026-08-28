package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InventoryItem
import com.example.model.VillageOrder
import com.example.ui.theme.HarvestOnPrimary
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSurface
import com.example.ui.theme.HarvestOnSurfaceVariant
import com.example.ui.theme.HarvestOutlineVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSecondaryContainer
import com.example.ui.theme.HarvestSurfaceContainer
import com.example.ui.theme.HarvestSurfaceContainerHigh
import com.example.ui.theme.HarvestSurfaceContainerLowest

@Composable
fun VillageOrdersBoardSection(
    orders: List<VillageOrder>,
    inventory: Map<String, InventoryItem>,
    onFulfillOrder: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("village_orders_section")
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Title: "طلبات القرية"
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = "طلبات القرية",
                color = HarvestOnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "سلم الطلبات لسكان القرية واكسب العملات والخبرة",
                color = HarvestOnSurfaceVariant,
                fontSize = 13.sp
            )
        }

        if (orders.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = HarvestSurfaceContainerLowest,
                border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📜", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لا توجد طلبات جديدة حالياً",
                        color = HarvestOnSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "سيقوم القرويون بتقديم طلبات جديدة قريباً!",
                        color = HarvestOnSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                orders.forEach { order ->
                    ModernVillageOrderCard(
                        order = order,
                        inventory = inventory,
                        onDeliver = { onFulfillOrder(order.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ModernVillageOrderCard(
    order: VillageOrder,
    inventory: Map<String, InventoryItem>,
    onDeliver: () -> Unit
) {
    val canDeliver = order.requiredItems.all { (item, needed) ->
        (inventory[item]?.quantity ?: 0) >= needed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(
            width = 1.dp,
            color = if (canDeliver) HarvestPrimary.copy(alpha = 0.5f) else HarvestOutlineVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Villager Avatar + Name & Reward
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(HarvestSecondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = order.requesterAvatar, fontSize = 24.sp)
                    }

                    Column {
                        Text(
                            text = order.requesterName,
                            color = HarvestOnSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "طلب مخصص",
                            color = HarvestOnSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }

                // Reward Badges
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = HarvestPrimaryContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+${order.rewardGold} 🪙",
                                color = HarvestOnPrimaryContainer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = HarvestSurfaceContainerHigh
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "خبرة",
                                tint = HarvestPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+${order.rewardExp} XP",
                                color = HarvestOnSurfaceVariant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Middle: Required Ingredients Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                order.requiredItems.forEach { (itemName, neededQty) ->
                    val userQty = inventory[itemName]?.quantity ?: 0
                    val emoji = inventory[itemName]?.emoji ?: "📦"
                    val isSufficient = userQty >= neededQty

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(HarvestSurfaceContainer)
                                .border(
                                    width = 1.dp,
                                    color = if (isSufficient) HarvestPrimary.copy(alpha = 0.5f) else HarvestOutlineVariant,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSufficient) HarvestPrimary else HarvestSurfaceContainerHigh
                        ) {
                            Text(
                                text = "$userQty/$neededQty",
                                color = if (isSufficient) HarvestOnPrimary else HarvestOnSurfaceVariant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Delivery Action Button
            Button(
                onClick = onDeliver,
                enabled = canDeliver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("deliver_order_${order.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HarvestPrimary,
                    contentColor = HarvestOnPrimary,
                    disabledContainerColor = HarvestSurfaceContainerHigh,
                    disabledContentColor = HarvestOnSurfaceVariant
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (canDeliver) Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                        contentDescription = "تسليم الطلب",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (canDeliver) "تسليم الطلب واكتماله ✓" else "المكونات غير مكتملة",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
