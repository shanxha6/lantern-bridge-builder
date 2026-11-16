package com.lanternbridge.builder.presentation.tutorial

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.theme.BambooGold
import com.lanternbridge.builder.core.theme.InkMuted
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.LanternAmber
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.PaperLight
import com.lanternbridge.builder.core.ui.GlassCard
import com.lanternbridge.builder.core.ui.PlankPips

@Composable
fun TutorialCard(
    step: TutorialStep,
    index: Int,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier, dark = true, radius = 18.dp, innerPadding = 16.dp) {
        Text(
            text = stringResource(R.string.tutorial_step, index + 1),
            style = MaterialTheme.typography.labelSmall,
            color = MintAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(titleRes(step)),
            style = MaterialTheme.typography.headlineSmall,
            color = PaperLight
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(bodyRes(step)),
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted.copy(alpha = 0.95f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        TutorialDiagram(step = step)
    }
}

@Composable
private fun TutorialDiagram(step: TutorialStep) {
    when (step) {
        TutorialStep.TAP_POSTS -> Row(
            modifier = Modifier.fillMaxWidth().height(76.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(54.dp)
            )
            Canvas(modifier = Modifier.weight(1f).height(20.dp)) {
                drawLine(
                    color = MintAccent,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(54.dp)
            )
        }

        TutorialStep.LIMITED_PLANKS -> Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlankPips(used = 5, total = 9)
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
        }

        TutorialStep.LONG_SPANS -> Box(modifier = Modifier.fillMaxWidth().height(96.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
                val widths = floatArrayOf(0.35f, 0.62f, 0.9f)
                widths.forEachIndexed { row, fraction ->
                    val y = 18f + row * 30f
                    drawLine(
                        color = InkText.copy(alpha = 0.6f),
                        start = Offset(10f, y),
                        end = Offset(size.width * fraction, y),
                        strokeWidth = 14f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = if (row == 2) LanternAmber else BambooGold,
                        start = Offset(10f, y),
                        end = Offset(size.width * fraction, y),
                        strokeWidth = 9f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        TutorialStep.HEAVY_CARTS -> Row(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val scales = floatArrayOf(0.7f, 0.9f, 1.1f)
            scales.forEach { factor ->
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            scaleX = factor
                            scaleY = factor
                        }
                )
            }
        }
    }
}

private fun titleRes(step: TutorialStep): Int = when (step) {
    TutorialStep.TAP_POSTS -> R.string.tutorial_1_title
    TutorialStep.LIMITED_PLANKS -> R.string.tutorial_2_title
    TutorialStep.LONG_SPANS -> R.string.tutorial_3_title
    TutorialStep.HEAVY_CARTS -> R.string.tutorial_4_title
}

private fun bodyRes(step: TutorialStep): Int = when (step) {
    TutorialStep.TAP_POSTS -> R.string.tutorial_1_body
    TutorialStep.LIMITED_PLANKS -> R.string.tutorial_2_body
    TutorialStep.LONG_SPANS -> R.string.tutorial_3_body
    TutorialStep.HEAVY_CARTS -> R.string.tutorial_4_body
}
