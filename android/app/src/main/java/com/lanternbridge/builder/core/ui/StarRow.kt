package com.lanternbridge.builder.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.theme.DustyRose

@Composable
fun StarRow(
    earned: Int,
    modifier: Modifier = Modifier,
    total: Int = 3,
    size: Dp = 56.dp
) {
    val description = stringResource(R.string.stars_of, earned, total)
    Row(
        modifier = modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            stateDescription = description
        },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until total) {
            val filled = index < earned
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = tween(durationMillis = 240))
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alpha = if (filled) 1f else 0.26f,
                    colorFilter = if (filled) null else ColorFilter.tint(DustyRose),
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}
