package com.lanternbridge.builder.presentation.gallery

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.InkMuted
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.LanternAmber
import com.lanternbridge.builder.core.ui.GlassCard
import com.lanternbridge.builder.core.ui.Labels
import com.lanternbridge.builder.domain.model.LanternDesign

@Composable
fun LanternCard(
    design: LanternDesign,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "lanternCard")
    val glow by transition.animateFloat(
        initialValue = 0.86f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    val description = if (design.unlocked) {
        stringResource(R.string.cd_lantern_design)
    } else {
        stringResource(R.string.locked_state, design.districtId)
    }
    GlassCard(
        modifier = modifier.semantics { stateDescription = description },
        dark = false,
        radius = 16.dp,
        innerPadding = 12.dp,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (design.unlocked) {
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .graphicsLayer {
                            scaleX = glow
                            scaleY = glow
                        }
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    LanternAmber.copy(alpha = 0.32f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alpha = if (design.unlocked) 1f else 0.28f,
                colorFilter = if (design.unlocked) null else ColorFilter.tint(DustyRose),
                modifier = Modifier.size(92.dp)
            )
            if (!design.unlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = InkMuted,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Labels.lanternName(design.id)),
            style = MaterialTheme.typography.labelSmall,
            color = if (design.unlocked) InkText else InkMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(Labels.districtName(design.districtId)),
            style = MaterialTheme.typography.labelSmall,
            color = InkMuted.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
