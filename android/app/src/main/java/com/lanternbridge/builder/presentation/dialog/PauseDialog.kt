package com.lanternbridge.builder.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.theme.InkMuted
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.PaperSurface

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = PaperSurface,
        titleContentColor = InkText,
        textContentColor = InkMuted,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_body),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onRestart) {
                    Text(
                        text = stringResource(R.string.restart),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                TextButton(onClick = onMenu) {
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    )
}
