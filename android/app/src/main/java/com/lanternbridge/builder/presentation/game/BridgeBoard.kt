package com.lanternbridge.builder.presentation.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.core.theme.BambooGold
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.LanternAmber
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.NightDeep
import com.lanternbridge.builder.core.theme.NightRiver
import com.lanternbridge.builder.domain.model.GamePhase
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min

@Composable
fun BridgeBoard(
    state: GameUiState,
    onPostTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmerTransition = rememberInfiniteTransition(label = "water")
    val shimmer by shimmerTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )
    val litLabel = stringResource(R.string.cd_lantern_lit)
    val unlitLabel = stringResource(R.string.cd_lantern_unlit)
    val cartLabel = stringResource(R.string.cd_cart)
    val boardShape = RoundedCornerShape(20.dp)

    BoxWithConstraints(modifier = modifier) {
        val frame = 16.dp
        val availableW = (maxWidth - frame * 2).value
        val availableH = (maxHeight - frame * 2).value
        val scale = min(
            availableW / GameConfig.BOARD_W,
            availableH / GameConfig.BOARD_H
        ).coerceAtLeast(0.01f)
        val contentW = GameConfig.BOARD_W * scale
        val contentH = GameConfig.BOARD_H * scale
        val originX = (maxWidth.value - contentW) / 2f
        val originY = (maxHeight.value - contentH) / 2f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(boardShape)
                .background(brush = Brush.verticalGradient(listOf(NightRiver, NightDeep)))
                .border(width = 2.dp, color = InkText.copy(alpha = 0.55f), shape = boardShape)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val unit = scale * density
                val baseX = originX * density
                val baseY = originY * density
                val toPx: (Float, Float) -> Offset = { x, y ->
                    Offset(baseX + x * unit, baseY + y * unit)
                }

                for (line in 0 until 4) {
                    val y = baseY + (120f + line * 130f) * unit
                    drawLine(
                        color = MintAccent.copy(alpha = 0.12f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 2f * unit,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = MintAccent.copy(alpha = 0.18f),
                        start = Offset(size.width * 0.18f + shimmer * unit, y),
                        end = Offset(size.width * 0.62f + shimmer * unit, y),
                        strokeWidth = 2f * unit,
                        cap = StrokeCap.Round
                    )
                }

                val bankWidth = GameConfig.BANK_WIDTH_UNITS * unit
                drawRect(
                    color = DustyRose.copy(alpha = 0.80f),
                    topLeft = Offset(0f, 0f),
                    size = Size(baseX + bankWidth, size.height)
                )
                drawRect(
                    color = DustyRose.copy(alpha = 0.80f),
                    topLeft = Offset(
                        baseX + GameConfig.BOARD_W * unit - bankWidth,
                        0f
                    ),
                    size = Size(size.width, size.height)
                )

                state.litPosts.forEach { index ->
                    if (index in state.posts.indices) {
                        val post = state.posts[index]
                        val center = toPx(post.x, post.y)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    LanternAmber.copy(alpha = 0.45f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = 40f * unit
                            ),
                            radius = 40f * unit,
                            center = center
                        )
                    }
                }

                if (state.selectedPost in state.posts.indices) {
                    val post = state.posts[state.selectedPost]
                    drawCircle(
                        color = MintAccent,
                        radius = 30f * unit,
                        center = toPx(post.x, post.y),
                        style = Stroke(width = 3f * unit)
                    )
                    state.posts.forEachIndexed { index, candidate ->
                        if (index != state.selectedPost) {
                            val near = state.reachable.contains(index)
                            drawCircle(
                                color = if (near) {
                                    MintAccent.copy(alpha = 0.55f)
                                } else {
                                    DustyRose.copy(alpha = 0.45f)
                                },
                                radius = 26f * unit,
                                center = toPx(candidate.x, candidate.y),
                                style = Stroke(
                                    width = 2f * unit,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(8f * unit, 7f * unit)
                                    )
                                )
                            )
                        }
                    }
                }

                state.planks.forEachIndexed { index, plank ->
                    val from = state.posts[plank.from]
                    val to = state.posts[plank.to]
                    val start = toPx(from.x, from.y)
                    val end = toPx(to.x, to.y)
                    val span = hypot(to.x - from.x, to.y - from.y)
                    val stressed = span * state.weight >= GameConfig.SNAP_LOAD * 0.8f
                    if (index == state.snappedPlank) {
                        val drop = state.fallProgress * 240f * unit
                        val mid = Offset((start.x + end.x) / 2f, (start.y + end.y) / 2f)
                        drawLine(
                            color = BambooGold.copy(alpha = 1f - state.fallProgress * 0.6f),
                            start = Offset(start.x, start.y + drop * 0.4f),
                            end = Offset(mid.x, mid.y + drop),
                            strokeWidth = 14f * unit,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = BambooGold.copy(alpha = 1f - state.fallProgress * 0.6f),
                            start = Offset(mid.x, mid.y + drop),
                            end = Offset(end.x, end.y + drop * 0.4f),
                            strokeWidth = 14f * unit,
                            cap = StrokeCap.Round
                        )
                    } else {
                        val sag = if (index == state.activePlank) state.activeSag else 0f
                        val path = Path()
                        path.moveTo(start.x, start.y)
                        path.quadraticTo(
                            (start.x + end.x) / 2f,
                            (start.y + end.y) / 2f + 2f * sag * unit,
                            end.x,
                            end.y
                        )
                        drawPath(
                            path = path,
                            color = InkText.copy(alpha = 0.75f),
                            style = Stroke(width = 20f * unit, cap = StrokeCap.Round)
                        )
                        drawPath(
                            path = path,
                            color = if (stressed) LanternAmber else BambooGold,
                            style = Stroke(width = 14f * unit, cap = StrokeCap.Round)
                        )
                    }
                }

                if (state.phase == GamePhase.SETTLING && !state.crossed) {
                    val center = toPx(state.cartX, state.cartY + 120f * state.fallProgress)
                    for (ring in 0 until 2) {
                        val progress = (state.fallProgress - ring * 0.2f).coerceIn(0f, 1f)
                        if (progress > 0f) {
                            drawCircle(
                                color = MintAccent.copy(alpha = 0.5f * (1f - progress)),
                                radius = (14f + 46f * progress) * unit,
                                center = center,
                                style = Stroke(width = 2.5f * unit)
                            )
                        }
                    }
                }
            }

            state.planks.forEachIndexed { index, plank ->
                if (index != state.snappedPlank && index != state.activePlank) {
                    val from = state.posts[plank.from]
                    val to = state.posts[plank.to]
                    val span = hypot(to.x - from.x, to.y - from.y)
                    val angle = Math.toDegrees(
                        atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble())
                    ).toFloat()
                    val midX = originX + (from.x + to.x) / 2f * scale
                    val midY = originY + (from.y + to.y) / 2f * scale
                    val widthDp = (span * scale).dp
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .offset(x = midX.dp - widthDp / 2, y = midY.dp - 9.dp)
                            .size(width = widthDp, height = 18.dp)
                            .graphicsLayer { rotationZ = angle }
                    )
                }
            }

            state.posts.forEachIndexed { index, post ->
                val lit = state.litPosts.contains(index)
                val label = if (lit) litLabel else unlitLabel
                val touch = (GameConfig.POST_HIT_RADIUS_UNITS * 2f * scale).dp
                    .coerceAtLeast(48.dp)
                Box(
                    modifier = Modifier
                        .offset(
                            x = (originX + post.x * scale).dp - touch / 2,
                            y = (originY + post.y * scale).dp - touch / 2
                        )
                        .size(touch)
                        .clickable(enabled = state.phase == GamePhase.BUILD) { onPostTap(index) }
                        .semantics {
                            contentDescription = label
                            stateDescription = label
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        alpha = if (lit) 1f else 0.72f,
                        colorFilter = if (lit) {
                            null
                        } else {
                            ColorFilter.tint(DustyRose, BlendMode.Modulate)
                        },
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            val cartDrop = if (state.phase == GamePhase.SETTLING && !state.crossed) {
                state.fallProgress * 150f
            } else {
                0f
            }
            if (state.loaded) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = cartLabel,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(
                            x = (originX + state.cartX * scale).dp - 27.dp,
                            y = (originY + (state.cartY + cartDrop) * scale).dp - 27.dp
                        )
                        .size(54.dp)
                        .graphicsLayer {
                            rotationZ = state.cartAngle + cartDrop * 0.12f
                        }
                )
            }
        }
    }
}
