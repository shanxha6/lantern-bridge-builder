package com.lanternbridge.builder.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.core.theme.InkMuted
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.PaperSurface

@Composable
fun StatCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val description = value + " " + label
    Column(
        modifier = modifier
            .heightIn(min = 68.dp)
            .shadow(elevation = 3.dp, shape = shape, clip = false)
            .background(color = PaperSurface.copy(alpha = 0.93f), shape = shape)
            .border(width = 1.5.dp, color = InkText.copy(alpha = 0.18f), shape = shape)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                stateDescription = description
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(8.dp).background(color = valueColor, shape = CircleShape))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = InkMuted,
            textAlign = TextAlign.Center
        )
    }
}
