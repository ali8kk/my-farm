package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerStats
import com.example.ui.theme.HarvestBarBorder
import com.example.ui.theme.HarvestBarInnerCard
import com.example.ui.theme.HarvestBarSurface
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSecondaryContainer
import com.example.ui.theme.HarvestOnSurface
import com.example.ui.theme.HarvestOnSurfaceVariant
import com.example.ui.theme.HarvestOutlineVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSecondaryContainer
import com.example.ui.theme.HarvestSurface
import com.example.ui.theme.HarvestSurfaceContainer
import com.example.ui.theme.HarvestSurfaceContainerHigh
import com.example.ui.theme.HarvestSurfaceContainerLow
import com.example.ui.theme.HarvestSurfaceContainerLowest
import com.example.ui.theme.HarvestTertiary
import com.example.ui.theme.HarvestTertiaryContainer

@Composable
fun TopHeaderBar(
    playerStats: PlayerStats,
    usedInventoryCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_header_bar")
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        color = HarvestBarSurface,
        shadowElevation = 4.dp,
        border = BorderStroke(1.5.dp, HarvestBarBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Top Row: Weather / Degree, Title "هارفست هارموني", Gold & Gems
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weather indicator (Modern light pill)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = HarvestBarInnerCard,
                    border = BorderStroke(1.dp, HarvestBarBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "☀️", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "25°",
                            color = HarvestOnSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Brand Headline
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "هارفست هارموني",
                        color = HarvestPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Level + Gold Plaque
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Level Badge
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = HarvestPrimaryContainer.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, HarvestPrimary.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "مستوى ${playerStats.level}",
                            color = HarvestPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Gold Pill
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = HarvestSecondaryContainer.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, HarvestBarBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${"%,d".format(playerStats.gold)}",
                                color = HarvestOnSecondaryContainer,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "🪙", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub Row: EXP progress & Storage Capacity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // EXP Bar
                val expRatio = (playerStats.currentExp.toFloat() / playerStats.maxExpForLevel.toFloat()).coerceIn(0f, 1f)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = HarvestBarInnerCard,
                    border = BorderStroke(1.dp, HarvestBarBorder),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "الخبرة (XP)", color = HarvestOnSurfaceVariant, fontSize = 9.sp, fontWeight = FontWeight.Medium)
                            Text(
                                text = "${playerStats.currentExp}/${playerStats.maxExpForLevel}",
                                color = HarvestPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        LinearProgressIndicator(
                            progress = { expRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = HarvestPrimary,
                            trackColor = HarvestBarBorder.copy(alpha = 0.5f)
                        )
                    }
                }

                // Storage Pill
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = HarvestBarInnerCard,
                    border = BorderStroke(1.dp, HarvestBarBorder),
                    modifier = Modifier.weight(0.8f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "📦", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$usedInventoryCount / ${playerStats.inventoryCapacity}",
                            color = HarvestOnSurface,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
