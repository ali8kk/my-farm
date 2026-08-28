package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HarvestOutlineVariant
import com.example.ui.theme.HarvestPrimary
import com.example.ui.theme.HarvestPrimaryContainer
import com.example.ui.theme.HarvestSecondary
import com.example.ui.theme.HarvestSurfaceContainer
import com.example.ui.theme.HarvestSurfaceContainerHigh
import com.example.ui.theme.HarvestSurfaceContainerLow

fun formatSeconds(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}

/**
 * Modern Tactile Soil Canvas (Layered soil texture preview)
 */
@Composable
fun CalmSoilTextureBackground(
    isReady: Boolean = false,
    level: Int = 1
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    ) {
        val width = size.width
        val height = size.height

        // 1. Base Soil Gradient
        val soilBrush = Brush.verticalGradient(
            colors = listOf(
                HarvestSurfaceContainerLow,
                HarvestSurfaceContainer,
                HarvestSurfaceContainerHigh
            )
        )
        drawRect(brush = soilBrush)

        // 2. Grassy Border Accent on Top Edges
        val grassBandHeight = 6.dp.toPx()
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(HarvestPrimary.copy(alpha = 0.25f), Color.Transparent)
            ),
            size = Size(width, grassBandHeight)
        )

        // 3. Tilled Furrows
        val furrowCount = 4
        val furrowSpacing = height / (furrowCount + 1)

        for (i in 1..furrowCount) {
            val y = i * furrowSpacing
            drawLine(
                color = HarvestOutlineVariant.copy(alpha = 0.4f),
                start = Offset(x = 10.dp.toPx(), y = y),
                end = Offset(x = width - 10.dp.toPx(), y = y),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
            )
        }

        // 4. Subtle Ready Glow
        if (isReady) {
            drawRoundRect(
                color = HarvestPrimaryContainer.copy(alpha = 0.15f),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )
        }

        // 5. Border Outline
        drawRoundRect(
            color = HarvestOutlineVariant.copy(alpha = 0.4f),
            style = Stroke(width = 1.dp.toPx()),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )
    }
}

@Composable
fun IngredientPill(
    emoji: String,
    name: String,
    currentQty: Int,
    requiredQty: Int
) {
    val isSufficient = currentQty >= requiredQty
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSufficient) HarvestPrimaryContainer.copy(alpha = 0.4f) else HarvestSurfaceContainerHigh)
            .border(
                width = 1.dp,
                color = if (isSufficient) HarvestPrimary.copy(alpha = 0.6f) else HarvestOutlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$currentQty/$requiredQty",
                color = if (isSufficient) HarvestPrimary else HarvestSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
