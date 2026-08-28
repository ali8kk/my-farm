package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val HarvestHarmonyColorScheme = lightColorScheme(
    primary = HarvestPrimary,
    onPrimary = HarvestOnPrimary,
    primaryContainer = HarvestPrimaryContainer,
    onPrimaryContainer = HarvestOnPrimaryContainer,
    secondary = HarvestSecondary,
    onSecondary = HarvestOnSecondary,
    secondaryContainer = HarvestSecondaryContainer,
    onSecondaryContainer = HarvestOnSecondaryContainer,
    tertiary = HarvestTertiary,
    onTertiary = HarvestOnTertiary,
    tertiaryContainer = HarvestTertiaryContainer,
    onTertiaryContainer = HarvestOnTertiaryContainer,
    background = HarvestBackground,
    onBackground = HarvestOnSurface,
    surface = HarvestSurface,
    onSurface = HarvestOnSurface,
    surfaceVariant = HarvestSurfaceVariant,
    onSurfaceVariant = HarvestOnSurfaceVariant,
    surfaceContainer = HarvestSurfaceContainer,
    surfaceContainerHigh = HarvestSurfaceContainerHigh,
    surfaceContainerHighest = HarvestSurfaceContainerHighest,
    surfaceContainerLow = HarvestSurfaceContainerLow,
    surfaceContainerLowest = HarvestSurfaceContainerLowest,
    outline = HarvestOutline,
    outlineVariant = HarvestOutlineVariant,
    error = HarvestError,
    errorContainer = HarvestErrorContainer,
    onError = HarvestOnError,
    onErrorContainer = HarvestOnErrorContainer
)

@Composable
fun CyberFarmTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HarvestHarmonyColorScheme,
        typography = Typography,
        content = content
    )
}
