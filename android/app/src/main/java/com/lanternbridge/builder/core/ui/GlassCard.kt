package com.lanternbridge.builder.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.PaperLight
import com.lanternbridge.builder.core.theme.PaperSurface

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    dark: Boolean = true,
    radius: Dp = 18.dp,
    innerPadding: Dp = 16.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    val fill = if (dark) PaperLight.copy(alpha = 0.10f) else PaperSurface.copy(alpha = 0.93f)
    val stroke = if (dark) PaperLight.copy(alpha = 0.18f) else InkText.copy(alpha = 0.20f)
    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = shape, clip = false)
            .background(color = fill, shape = shape)
            .border(width = 1.5.dp, color = stroke, shape = shape)
            .padding(innerPadding),
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}
