package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.NotificationsPaused
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Timelapse
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
import com.example.model.FactoryRecipe
import com.example.model.FactoryState
import com.example.model.FactoryStatus
import com.example.model.FactoryType
import com.example.model.InventoryItem
import com.example.ui.theme.HarvestOnPrimary
import com.example.ui.theme.HarvestOnPrimaryContainer
import com.example.ui.theme.HarvestOnSecondary
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

@Composable
fun FactorySection(
    factories: List<FactoryState>,
    recipes: List<FactoryRecipe>,
    inventory: Map<String, InventoryItem>,
    onStartProduction: (FactoryType, String) -> Unit,
    onCollectProduct: (FactoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    val unlockedFactories = factories.filter { it.isUnlocked }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("factory_section")
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Title
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = "آلات الإنتاج",
                color = HarvestOnSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "أدر مصانعك وزد إنتاجك",
                color = HarvestOnSurfaceVariant,
                fontSize = 13.sp
            )
        }

        if (unlockedFactories.isEmpty()) {
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
                    Text(text = "🏭", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "لا توجد آلات مشيدة حالياً",
                        color = HarvestOnSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "توجه إلى قسم المتجر لشراء وتشييد المصانع والمعامل الجديدة 🏪",
                        color = HarvestOnSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                unlockedFactories.forEach { factoryState ->
                    val factoryRecipes = recipes.filter { it.factoryId == factoryState.type.id }
                    ModernFactoryCard(
                        factoryState = factoryState,
                        recipes = factoryRecipes,
                        inventory = inventory,
                        onStartRecipe = { recipeId -> onStartProduction(factoryState.type, recipeId) },
                        onCollect = { onCollectProduct(factoryState.type) }
                    )
                }
            }
        }
    }
}

@Composable
fun ModernFactoryCard(
    factoryState: FactoryState,
    recipes: List<FactoryRecipe>,
    inventory: Map<String, InventoryItem>,
    onStartRecipe: (String) -> Unit,
    onCollect: () -> Unit
) {
    val isIdle = factoryState.status == FactoryStatus.IDLE
    val isProcessing = factoryState.status == FactoryStatus.PROCESSING
    val isReady = factoryState.status == FactoryStatus.READY_TO_COLLECT

    var selectedRecipeId by remember { mutableStateOf(recipes.firstOrNull()?.id ?: "") }
    val currentRecipe = recipes.find { it.id == (if (isProcessing || isReady) factoryState.activeRecipeId else selectedRecipeId) }
        ?: recipes.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("factory_card_${factoryState.type.id}"),
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
            // Header: Machine Icon + Name + Status Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(HarvestSurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = factoryState.type.iconEmoji, fontSize = 34.sp)
                    }

                    Column {
                        Text(
                            text = factoryState.type.displayName,
                            color = HarvestOnSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = when {
                                    isProcessing -> Icons.Default.Timelapse
                                    isReady -> Icons.Default.NotificationsPaused
                                    else -> Icons.Default.Block
                                },
                                contentDescription = "الحالة",
                                tint = when {
                                    isProcessing -> HarvestPrimary
                                    isReady -> HarvestSecondary
                                    else -> HarvestOnSurfaceVariant
                                },
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = when {
                                    isProcessing -> "قيد العمل"
                                    isReady -> "جاهز للاستلام"
                                    else -> "متوقف"
                                },
                                color = when {
                                    isProcessing -> HarvestPrimary
                                    isReady -> HarvestSecondary
                                    else -> HarvestOnSurfaceVariant
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = HarvestSurfaceContainerHigh
                ) {
                    Text(
                        text = "مستوى ${factoryState.level}",
                        color = HarvestOnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Production Queue Area
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when {
                    isReady -> HarvestSecondaryContainer.copy(alpha = 0.35f)
                    else -> HarvestSurfaceContainerLow
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    currentRecipe?.let { recipe ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    isReady -> "اكتمل الإنتاج: ${recipe.displayName}"
                                    isProcessing -> "جاري الإنتاج: ${recipe.displayName}"
                                    else -> "المنتج: ${recipe.displayName}"
                                },
                                color = HarvestOnSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (isProcessing) {
                                Text(
                                    text = formatSeconds(factoryState.secondsRemaining),
                                    color = HarvestPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (isProcessing) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val animatedProgress by animateFloatAsState(
                                targetValue = factoryState.progress,
                                label = "machProgress"
                            )
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = HarvestPrimary,
                                trackColor = HarvestSurfaceContainerHigh
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Buttons
                        when {
                            isReady -> {
                                Button(
                                    onClick = onCollect,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                        .testTag("collect_product_${factoryState.type.id}"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = HarvestSecondary,
                                        contentColor = HarvestOnSecondary
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Inventory,
                                            contentDescription = "استلام",
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "جمع ${recipe.outputItemName} (+${recipe.goldValue} 🪙)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            isProcessing -> {
                                Text(
                                    text = "جاري تحضير المنتج بالكامل...",
                                    color = HarvestOnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }

                            else -> {
                                val hasIngredients = recipe.requiredIngredients.all { (item, needed) ->
                                    (inventory[item]?.quantity ?: 0) >= needed
                                }

                                Button(
                                    onClick = { onStartRecipe(recipe.id) },
                                    enabled = hasIngredients,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                        .testTag("start_factory_${factoryState.type.id}"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = HarvestPrimary,
                                        contentColor = HarvestOnPrimary,
                                        disabledContainerColor = HarvestSurfaceContainerHigh,
                                        disabledContentColor = HarvestOnSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "بدء",
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (hasIngredients) "بدء إنتاج (${recipe.displayName})" else "المكونات غير كافية",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
