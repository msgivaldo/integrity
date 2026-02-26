package dev.givaldo.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.givaldo.integrity.SecurityCheck

@Composable
fun SecurityCheckItem(
    title: String,
    state: SecurityCheck,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SecurityCheck.Secure -> SecureItem(title, modifier)
        is SecurityCheck.Flagged -> FlaggedItem(title, state, modifier)
        is SecurityCheck.Error -> ErrorItem(title, state, modifier)
    }
}

@Composable
fun SecureItem(
    title: String,
    modifier: Modifier = Modifier
) {

    ListItem(
        modifier = modifier,
        title = title,
        message = "Secure",
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

@Composable
fun FlaggedItem(
    title: String,
    flagged: SecurityCheck.Flagged,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        title = title, message = "${flagged.code}: ${flagged.message}",
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFFFA000),
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

@Composable
fun ErrorItem(
    title: String,
    error: SecurityCheck.Error,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        title = title,
        message = error.exception.localizedMessage ?: "Check failed to run",
        icon = {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(24.dp)
            )
        }
    )
}

@Composable
private fun ListItem(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                icon()
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true, name = "Security Check States")
@Composable
fun PreviewSecurityCheckItems() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SecurityCheckItem(
                title = "Root Detection",
                state = SecurityCheck.Secure
            )
            SecurityCheckItem(
                title = "Emulator Check",
                state = SecurityCheck.Flagged(
                    code = 2,
                    message = "Device is running on a generic x86_64 emulator."
                )
            )
            SecurityCheckItem(
                title = "Play Integrity API",
                state = SecurityCheck.Error(
                    exception = Exception("Network timeout while contacting Google Play Services")
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Individual - Secure")
@Composable
fun PreviewSecureItem() {
    MaterialTheme {
        SecureItem(title = "Debuggable Check")
    }
}

@Preview(showBackground = true, name = "Individual - Flagged")
@Composable
fun PreviewFlaggedItem() {
    MaterialTheme {
        FlaggedItem(
            title = "Developer Options",
            flagged = SecurityCheck.Flagged(code = 1, "USB Debugging is enabled")
        )
    }
}