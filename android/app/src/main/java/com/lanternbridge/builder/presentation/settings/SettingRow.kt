package com.lanternbridge.builder.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.InkMuted
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.PaperSurface

@Composable
fun SettingRow(
    label: String,
    hint: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val stateLabel = stringResource(if (checked) R.string.state_on else R.string.state_off)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .background(color = PaperSurface.copy(alpha = 0.92f), shape = shape)
            .border(width = 1.5.dp, color = InkText.copy(alpha = 0.18f), shape = shape)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .semantics { stateDescription = stateLabel },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = InkText
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PaperSurface,
                checkedTrackColor = MintAccent,
                uncheckedThumbColor = PaperSurface,
                uncheckedTrackColor = DustyRose
            )
        )
    }
}
