package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FrontHand
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundEffectManager
import com.example.model.CropType
import com.example.model.FarmPlot
import com.example.model.PlotStatus
import com.example.ui.theme.HarvestOnPrimary
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSecondaryContainer
import com.example.ui.theme.HarvestOnSurface
import com.example.ui.theme.HarvestOnSurfaceVariant
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
import kotlinx.coroutines.launch

@Composable
fun FarmFieldsSection(
    plots: List<FarmPlot>,
    playerGold: Int,
    onHarvestPlot: (Int) -> Unit,
    onHarvestAllReady: () -> Unit,
    onOpenPlantModal: (Int) -> Unit,
    onUpgradePlot: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val readyPlotsCount = plots.count { it.status == PlotStatus.READY }
    val growingPlotsCount = plots.count { it.status == PlotStatus.GROWING }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("farm_fields_section")
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Header: "الحقول النشطة"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "الحقول النشطة",
                    color = HarvestPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "قم بإدارة المحاصيل الخاصة بك وحصادها في الوقت المناسب.",
                    color = HarvestOnSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Actions Row (ري الكل / حصاد الكل)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = HarvestSurfaceContainerLowest,
                border = BorderStroke(1.dp, HarvestOutlineVariant.copy(alpha = 0.5f)),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "ري الكل",
                        tint = HarvestTertiaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ري الكل",
                        color = HarvestPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = onHarvestAllReady,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("harvest_all_ready_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HarvestPrimary,
                    contentColor = HarvestOnPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Agriculture,
                        contentDescription = "حصاد الكل",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (readyPlotsCount > 0) "حصاد الكل ($readyPlotsCount)" else "حصاد الكل",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2-Column Grid of Tactile Crop Cards
        val chunkedPlots = plots.chunked(2)
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            chunkedPlots.forEach { rowPlots ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowPlots.forEach { plot ->
                        Box(modifier = Modifier.weight(1f)) {
                            ModernHarvestPlotTile(
                                plot = plot,
                                playerGold = playerGold,
                                onHarvest = onHarvestPlot,
                                onPlant = onOpenPlantModal,
                                onUpgrade = onUpgradePlot
                            )
                        }
                    }
                    if (rowPlots.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ModernHarvestPlotTile(
    plot: FarmPlot,
    playerGold: Int,
    onHarvest: (Int) -> Unit,
    onPlant: (Int) -> Unit,
    onUpgrade: (Int) -> Unit
) {
    val isReady = plot.status == PlotStatus.READY
    val isGrowing = plot.status == PlotStatus.GROWING
    val isEmpty = plot.status == PlotStatus.EMPTY

    val scope = rememberCoroutineScope()
    val harvestScale = remember { Animatable(1f) }

    // Ripe Pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "cropPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isReady) 1.06f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cropPulseScale"
    )

    val triggerHarvest = {
        SoundEffectManager.playHarvestSound()
        scope.launch {
            harvestScale.animateTo(1.2f, animationSpec = tween(70, easing = FastOutSlowInEasing))
            harvestScale.animateTo(0.92f, animationSpec = tween(70, easing = FastOutSlowInEasing))
            harvestScale.animateTo(1.0f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
        onHarvest(plot.id)
    }

    if (isEmpty) {
        // Empty Plot Slot using the custom 3D Plowed Soil Graphic
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onPlant(plot.id) }
                .testTag("plot_card_${plot.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
            border = BorderStroke(1.5.dp, HarvestOutlineVariant.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Badge: Empty Plot + Level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = HarvestSurfaceContainerHigh
                    ) {
                        Text(
                            text = "حقل فارغ",
                            color = HarvestOnSurfaceVariant,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "مستوى ${plot.level}",
                        color = HarvestOnSurfaceVariant,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Middle: Custom 3D Plowed Soil Image Tile
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(98.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(HarvestSurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_empty_soil_plot_v2),
                        contentDescription = "تربة الحقل الفارغ",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Floating Add Action Icon on top of the soil
                    Surface(
                        shape = CircleShape,
                        color = HarvestPrimary,
                        shadowElevation = 3.dp,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "زرع بذور",
                                tint = HarvestOnPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // Bottom: Title & Plant Button
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "حقل #${plot.id}",
                            color = HarvestOnSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "جاهز للزراعة",
                            color = HarvestPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { onPlant(plot.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("plant_button_${plot.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HarvestPrimary,
                            contentColor = HarvestOnPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "زرع بذور", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    } else {
        // Active Plot Card (Growing or Ready)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .testTag("plot_card_${plot.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = HarvestSurfaceContainerLowest),
            border = BorderStroke(1.5.dp, if (isReady) HarvestPrimary.copy(alpha = 0.8f) else HarvestSurfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isReady) 4.dp else 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Status Badge + Level/Upgrade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Badge (قيد النمو / جاهز للحصاد)
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isReady) HarvestPrimary else HarvestSecondaryContainer
                    ) {
                        Text(
                            text = if (isReady) "جاهز للحصاد ✓" else "قيد النمو",
                            color = if (isReady) HarvestOnPrimary else HarvestOnSecondaryContainer,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    // Level or Upgrade Button
                    if (plot.canUpgrade && playerGold >= plot.upgradeCost) {
                        Surface(
                            onClick = { onUpgrade(plot.id) },
                            shape = RoundedCornerShape(6.dp),
                            color = HarvestSurfaceContainerLow,
                            border = BorderStroke(1.dp, HarvestOutlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Upgrade,
                                    contentDescription = "ترقية",
                                    tint = HarvestPrimary,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "${plot.upgradeCost} 🪙",
                                    color = HarvestPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "مستوى ${plot.level}",
                            color = HarvestOnSurfaceVariant,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Middle: Visual Display Area with Soft Tint
                val crop = plot.cropType ?: CropType.WHEAT
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(98.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isReady) HarvestPrimaryContainer.copy(alpha = 0.25f)
                            else HarvestSurfaceContainer
                        )
                        .clickable(enabled = isReady) { triggerHarvest() },
                    contentAlignment = Alignment.Center
                ) {
                    val displayEmoji = when {
                        isReady -> crop.iconEmoji
                        plot.growthStage == 1 -> crop.growingEmoji
                        else -> crop.sproutEmoji
                    }

                    Box(
                        modifier = Modifier.scale(if (isReady) pulseScale * harvestScale.value else 1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = displayEmoji, fontSize = if (isReady) 40.sp else 34.sp)
                    }
                }

                // Bottom Content: Title & Action/Progress
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = crop.displayName,
                            color = HarvestOnSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (isReady) {
                            Text(
                                text = "+${crop.harvestGold * plot.yieldMultiplier} 🪙",
                                color = HarvestPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = formatSeconds(plot.secondsRemaining),
                                color = HarvestOnSurfaceVariant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (isReady) {
                        Button(
                            onClick = { triggerHarvest() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .testTag("harvest_button_${plot.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HarvestPrimary,
                                contentColor = HarvestOnPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FrontHand,
                                    contentDescription = "حصاد",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "حصاد المحصول",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Progress Bar with tactile style
                        val animatedProgress by animateFloatAsState(
                            targetValue = plot.progress,
                            label = "plotProgress"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = HarvestPrimary,
                                trackColor = HarvestSurfaceContainerHigh
                            )
                        }
                    }
                }
            }
        }
    }
}
