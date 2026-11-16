package com.lanternbridge.builder.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.MintAccent

@Composable
fun PlankPips(
    used: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val description = stringResource(R.string.planks_left_state, (total - used).coerceAtLeast(0))
    Row(
        modifier = modifier.semantics { stateDescription = description },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until total) {
            val spent = index < used
            val tint by animateColorAsState(
                targetValue = if (spent) DustyRose else MintAccent,
                animationSpec = tween(durationMillis = 220),
                label = "pip"
            )
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(5.dp)
                    .background(color = tint, shape = RoundedCornerShape(3.dp))
            )
        }
    }
}
