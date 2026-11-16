package com.lanternbridge.builder.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.di.AppViewModelFactory
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.LanternAmber
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.NightRiver
import com.lanternbridge.builder.core.theme.PaperLight
import com.lanternbridge.builder.core.theme.PlumDeep
import com.lanternbridge.builder.core.ui.ScreenBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    val transition = rememberInfiniteTransition(label = "splash")
    val lanternA by transition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lanternA"
    )
    val lanternB by transition.animateFloat(
        initialValue = 6f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lanternB"
    )
    val lanternC by transition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lanternC"
    )
    val halo by transition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )
    val shimmer by transition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                Color(0xFF24181E).copy(alpha = 0.94f),
                NightRiver.copy(alpha = 0.90f),
                PlumDeep.copy(alpha = 0.92f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                SplashLantern(size = 64, drift = lanternA)
                SplashLantern(size = 48, drift = lanternB)
                SplashLantern(size = 40, drift = lanternC)
            }
            Spacer(modifier = Modifier.height(34.dp))
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(216.dp)
                        .graphicsLayer {
                            scaleX = halo
                            scaleY = halo
                        }
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    LanternAmber.copy(alpha = 0.30f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(164.dp)
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = stringResource(R.string.title_main),
                style = MaterialTheme.typography.displaySmall,
                color = PaperLight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelLarge,
                color = DustyRose,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))
            LinearProgressIndicator(
                color = MintAccent,
                trackColor = DustyRose.copy(alpha = 0.28f),
                modifier = Modifier
                    .width(168.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = DustyRose.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .graphicsLayer { translationX = shimmer }
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                MintAccent.copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun SplashLantern(size: Int, drift: Float) {
    Image(
        painter = painterResource(R.drawable.sprite_3),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(size.dp)
            .graphicsLayer { translationY = drift * density }
    )
}
