package com.koineos.app.ui.components.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

data class PasswordRequirement(
    val message: String,
    val isValid: Boolean
)

@Composable
fun PasswordRequirements(
    requirements: List<PasswordRequirement>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Password must include:",
            style = Typography.bodySmall,
            color = Colors.TextSecondary,
            modifier = Modifier.padding(bottom = Dimensions.paddingSmall)
        )

        requirements.forEach { requirement ->
            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconComponent(
                    icon = AppIcon.Correct,
                    contentDescription = null,
                    isSelected = requirement.isValid,
                    tint = if (requirement.isValid) Colors.Success else Colors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = requirement.message,
                    style = Typography.bodySmall,
                    color = if (requirement.isValid) Colors.Success else Colors.TextSecondary,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
        }
    }
}

@Preview
@Composable
fun PasswordRequirementsPreview() {
    KoineosTheme {
        Surface {
            PasswordRequirements(
                requirements = listOf(
                    PasswordRequirement("Lowercase character", true),
                    PasswordRequirement("Uppercase character", true),
                    PasswordRequirement("Numeric character", false),
                    PasswordRequirement("Special character", false),
                    PasswordRequirement("At least 10 characters", false)
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}