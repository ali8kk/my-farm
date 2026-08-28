package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnimalCardState
import com.example.model.AnimalStatus
import com.example.ui.theme.HarvestError
import com.example.ui.theme.HarvestErrorContainer
import com.example.ui.theme.HarvestOnErrorContainer
import com.example.ui.theme.HarvestOnPrimary
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSecondary
import com.example.ui.theme.HarvestOnSurface
import com.example.ui.theme.HarvestOnSurfaceVariant
import com.example.ui.theme.HarvestOnTertiary
import com.example.ui.theme.HarvestOutlineVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSecondary
import com.example.ui.theme.HarvestSurfaceContainer
import com.example.ui.theme.HarvestSurfaceContainerHigh
import com.example.ui.theme.HarvestSurfaceContainerLow
import com.example.ui.theme.HarvestSurfaceContainerLowest
import com.example.ui.theme.HarvestSurfaceVariant
import com.example.ui.theme.HarvestTertiary
import com.example.ui.theme.HarvestTertiaryContainer

@Composable
fun LivestockSection(
    animals: List<AnimalCardState>,
    playerGold: Int,
    onFeedAnimal: (String) -> Unit,
    onCollectAnimal: (String) -> Unit,
    onUpgradeAnimal: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("livestock_section")
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "الحظيرة",
                    color = HarvestOnSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "اعتني بحيواناتك لزيادة الإنتاج",
                    color = HarvestOnSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = HarvestSurfaceContainer,
                border = BorderStroke(1.dp, HarvestOutlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "السعة",
                        tint = HarvestSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "السعة: ${animals.size}/20",
                        color = HarvestSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bento-style animal cards
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            animals.forEach { animal ->
                ModernLivestockCard(
                    animal = animal,
                    playerGold = playerGold,
                    onFeed = onFeedAnimal,
                    onCollect = onCollectAnimal,
                    onUpgrade = onUpgradeAnimal
                )
            }
        }
    }
}

@Composable
fun ModernLivestockCard(
    animal: AnimalCardState,
    playerGold: Int,
    onFeed: (String) -> Unit,
    onCollect: (String) -> Unit,
    onUpgrade: (String) -> Unit
) {
    val isNeedsFeed = animal.status == AnimalStatus.NEEDS_FEED
    val isProducing = animal.status == AnimalStatus.PRODUCING
    val isReady = animal.status == AnimalStatus.READY_TO_COLLECT

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("animal_card_${animal.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
        border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Animal Image Box + Details + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D-Style Image Box
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(HarvestSurfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = animal.type.iconEmoji, fontSize = 42.sp)
                }

                // Info & Progress Bars
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${animal.type.displayName} (مستوى ${animal.level})",
                            color = HarvestOnSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when {
                                isNeedsFeed -> HarvestErrorContainer
                                isReady -> HarvestPrimaryContainer.copy(alpha = 0.25f)
                                else -> HarvestSurfaceContainerHigh
                            }
                        ) {
                            Text(
                                text = when {
                                    isNeedsFeed -> "جائعة"
                                    isReady -> "جاهز"
                                    else -> "تنتج"
                                },
                                color = when {
                                    isNeedsFeed -> HarvestOnErrorContainer
                                    isReady -> HarvestPrimary
                                    else -> HarvestOnSurfaceVariant
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Production progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "إنتاج",
                            tint = HarvestTertiaryContainer,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val progressValue = if (isReady) 1f else if (isProducing) animal.progress else 0f
                        val animatedProgress by animateFloatAsState(targetValue = progressValue, label = "animalProd")
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isReady) HarvestPrimary else HarvestTertiaryContainer,
                            trackColor = HarvestSurfaceContainerHigh
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isReady) "جاهز" else if (isProducing) formatSeconds(animal.secondsRemaining) else "0%",
                            color = HarvestOnSurfaceVariant,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isNeedsFeed) {
                    Button(
                        onClick = { onFeed(animal.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("feed_animal_${animal.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HarvestPrimary,
                            contentColor = HarvestOnPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = "إطعام",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "إطعام (-${animal.type.feedRequired})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else if (isReady) {
                    Button(
                        onClick = { onCollect(animal.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("collect_animal_${animal.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HarvestTertiary,
                            contentColor = HarvestOnTertiary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "جمع",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "جمع ${animal.type.outputItemName} (+${animal.type.goldValue * animal.outputQuantity} 🪙)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = HarvestSurfaceContainer,
                            disabledContentColor = HarvestOnSurfaceVariant
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "جاري الإنتاج... (${formatSeconds(animal.secondsRemaining)})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
