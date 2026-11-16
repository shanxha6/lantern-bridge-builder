package com.lanternbridge.builder.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
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
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.PaperSurface
import com.lanternbridge.builder.core.theme.PaperSurfaceAlt
import com.lanternbridge.builder.core.theme.PlumPrimary
import com.lanternbridge.builder.domain.model.DistrictProgress

@Composable
fun DistrictTile(
    district: DistrictProgress,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val description = if (district.unlocked) {
        stringResource(
            R.string.district_state,
            district.id,
            district.completed,
            district.total
        )
    } else {
        stringResource(R.string.district_locked_state, district.id)
    }
    val fill = when {
        !district.unlocked -> DustyRose.copy(alpha = 0.28f)
        selected -> PaperSurface.copy(alpha = 0.97f)
        else -> PaperSurfaceAlt.copy(alpha = 0.85f)
    }
    val stroke = when {
        !district.unlocked -> InkText.copy(alpha = 0.12f)
        selected -> PlumPrimary
        else -> InkText.copy(alpha = 0.18f)
    }
    Column(
        modifier = modifier
            .heightIn(min = 104.dp)
            .shadow(elevation = if (selected) 5.dp else 2.dp, shape = shape, clip = false)
            .background(color = fill, shape = shape)
            .border(width = if (selected) 2.dp else 1.5.dp, color = stroke, shape = shape)
            .clickable(enabled = district.unlocked, onClick = onClick)
            .semantics { stateDescription = description }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.district_short, district.id),
            style = MaterialTheme.typography.labelSmall,
            color = if (district.unlocked) InkText else InkMuted,
            textAlign = TextAlign.Center
        )
        if (district.unlocked) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alpha = if (district.completed > 0) 1f else 0.6f,
                colorFilter = if (district.completed > 0) null else ColorFilter.tint(DustyRose),
                modifier = Modifier.size(34.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = InkMuted.copy(alpha = 0.7f),
                modifier = Modifier.size(26.dp)
            )
        }
        Text(
            text = stringResource(R.string.fraction, district.completed, district.total),
            style = MaterialTheme.typography.labelSmall,
            color = if (district.unlocked) InkMuted else InkMuted.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(color = DustyRose.copy(alpha = 0.45f), shape = shape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        fraction = if (district.total == 0) {
                            0f
                        } else {
                            district.completed.toFloat() / district.total.toFloat()
                        }
                    )
                    .height(4.dp)
                    .background(color = MintAccent, shape = shape)
            )
        }
    }
}
