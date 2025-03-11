package com.koineos.app.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.components.core.NestedScreenScaffold
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography
import java.util.concurrent.TimeUnit

/**
 * Screen that displays practice session results.
 */
@Composable
fun PracticeSessionResultsScreen(
    totalExercises: Int,
    correctAnswers: Int,
    incorrectAnswers: Int,
    completionTimeMs: Long,
    accuracyPercentage: Float,
    onDone: () -> Unit
) {
    NestedScreenScaffold {
        PracticeSessionResultsContent(
            totalExercises = totalExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            completionTimeMs = completionTimeMs,
            accuracyPercentage = accuracyPercentage,
            paddingValues = it,
            onDone = onDone
        )
    }
}

@Composable
private fun PracticeSessionResultsContent(
    totalExercises: Int,
    correctAnswers: Int,
    incorrectAnswers: Int,
    completionTimeMs: Long,
    accuracyPercentage: Float,
    paddingValues: PaddingValues,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            Dimensions.spacingXLarge,
            alignment = Alignment.CenterVertically
        )
    ) {
        ScoreHeader(
            correctAnswers = correctAnswers,
            totalExercises = totalExercises,
            accuracyPercentage = accuracyPercentage
        )

        TimeCard(completionTimeMs)

        AccuracyCard(
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            totalExercises = totalExercises
        )

        RegularButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDone,
            text = "Done"
        )
    }
}

/**
 * Displays the score header with a visual indicator.
 */
@Composable
private fun ScoreHeader(
    correctAnswers: Int,
    totalExercises: Int,
    accuracyPercentage: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Great Job!",
            style = Typography.headlineLarge,
            color = Colors.Primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        Text(
            text = "$correctAnswers out of $totalExercises correct",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Colors.OnSurface
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Colors.PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${accuracyPercentage.toInt()}%",
                style = Typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Colors.Primary
            )
        }
    }
}

/**
 * Card displaying time taken to complete practice.
 */
@Composable
private fun TimeCard(completionTimeMs: Long) {
    // Convert milliseconds to minutes and seconds
    val minutes = TimeUnit.MILLISECONDS.toMinutes(completionTimeMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(completionTimeMs) % 60

    RegularCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.paddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconComponent(
                icon = AppIcon.Timer,
                isSelected = true,
                contentDescription = "Time",
                tint = Colors.Primary,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${minutes}m ${seconds}s",
                style = Typography.titleMedium,
                color = Colors.OnSurface
            )
        }
    }
}

/**
 * Card displaying accuracy breakdown.
 */
@Composable
private fun AccuracyCard(
    correctAnswers: Int,
    incorrectAnswers: Int,
    totalExercises: Int
) {
    RegularCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            Text(
                text = "Accuracy",
                style = Typography.titleMedium,
                color = Colors.OnSurface,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { correctAnswers.toFloat() / totalExercises },
                modifier = Modifier.fillMaxWidth(),
                color = Colors.Success,
                trackColor = Colors.Error,
                drawStopIndicator = {},
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "Correct",
                        tint = Colors.Success,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "Correct",
                        style = Typography.bodyMedium,
                        color = Colors.OnSurface
                    )

                    Text(
                        text = "$correctAnswers",
                        style = Typography.titleLarge,
                        color = Colors.Success,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Incorrect",
                        tint = Colors.Error,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "Incorrect",
                        style = Typography.bodyMedium,
                        color = Colors.OnSurface
                    )

                    Text(
                        text = "$incorrectAnswers",
                        style = Typography.titleLarge,
                        color = Colors.Error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeResultsScreenPreview() {
    KoineosTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PracticeSessionResultsScreen(
                totalExercises = 10,
                correctAnswers = 7,
                incorrectAnswers = 3,
                completionTimeMs = 120000,
                accuracyPercentage = 70f,
                onDone = {},
            )
        }
    }
}