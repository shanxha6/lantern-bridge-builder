package com.lanternbridge.builder.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.PaperSurfaceAlt

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    stateHint: String? = null
) {
    val shape = RoundedCornerShape(16.dp)
    val semanticsModifier = if (stateHint != null) {
        Modifier.semantics { stateDescription = stateHint }
    } else {
        Modifier
    }
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (enabled) DustyRose else DustyRose.copy(alpha = 0.45f)
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = InkText,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = InkText.copy(alpha = 0.40f)
        ),
        modifier = modifier
            .heightIn(min = 52.dp)
            .background(
                color = if (enabled) {
                    PaperSurfaceAlt.copy(alpha = 0.72f)
                } else {
                    DustyRose.copy(alpha = 0.26f)
                },
                shape = shape
            )
            .then(semanticsModifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
