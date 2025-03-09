package com.koineos.app.ui.components.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.presentation.model.practice.FeedbackUiState
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Displays feedback about the user's answer.
 *
 * @param feedback The feedback to display
 * @param isVisible Whether the feedback panel is visible
 * @param modifier Modifier for styling
 */
@Composable
fun FeedbackPanel(
    feedback: FeedbackUiState?,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible && feedback != null,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = modifier
    ) {
        if (feedback != null) {
            val backgroundColor = if (feedback.isCorrect) {
                Colors.SuccessContainer
            } else {
                Colors.ErrorContainer
            }

            val textColor = if (feedback.isCorrect) {
                Colors.OnSuccessContainer
            } else {
                Colors.Error
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimensions.cornerLarge))
                    .background(backgroundColor)
                    .padding(Dimensions.paddingLarge),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.spacingMedium,
                        Alignment.Start
                    )
                ) {
                    IconComponent(
                        icon = if (feedback.isCorrect) AppIcon.Correct else AppIcon.Incorrect,
                        contentDescription = if (feedback.isCorrect) "Correct" else "Incorrect",
                        tint = textColor
                    )

                    // Title
                    Text(
                        text = feedback.message,
                        style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                }

                // Correct answer (if answer was incorrect)
                if (!feedback.isCorrect && feedback.correctAnswer != null) {
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    Text(
                        text = "Correct answer:",
                        style = Typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = feedback.correctAnswer,
                        style = Typography.titleLarge.copy(
                            fontFamily = KoineFont,
                            fontWeight = FontWeight.Bold
                        ),
                        color = textColor,
                        textAlign = TextAlign.Start
                    )
                }

                // Explanation (if provided)
                if (feedback.explanation != null) {
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    Text(
                        text = feedback.explanation,
                        style = Typography.bodyLarge,
                        color = textColor.copy(alpha = 0.8f),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedbackPanelPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingXLarge)
            ) {
                // Basic correct feedback
                Box(modifier = Modifier.fillMaxWidth()) {
                    FeedbackPanel(
                        feedback = FeedbackUiState(
                            isCorrect = true,
                            message = "Correct!"
                        ),
                        isVisible = true
                    )
                }

                // Correct feedback with explanation
                Box(modifier = Modifier.fillMaxWidth()) {
                    FeedbackPanel(
                        feedback = FeedbackUiState(
                            isCorrect = true,
                            message = "Great job!",
                            explanation = "You correctly identified alpha (Α α)."
                        ),
                        isVisible = true
                    )
                }

                // Incorrect feedback
                Box(modifier = Modifier.fillMaxWidth()) {
                    FeedbackPanel(
                        feedback = FeedbackUiState(
                            isCorrect = false,
                            message = "Incorrect",
                            correctAnswer = "Β β",
                            explanation = "This is beta (Β β), which makes the 'b' sound."
                        ),
                        isVisible = true
                    )
                }

                // Partial feedback (for matching exercises)
                Box(modifier = Modifier.fillMaxWidth()) {
                    FeedbackPanel(
                        feedback = FeedbackUiState(
                            isCorrect = false,
                            message = "Give it another try",
                            correctAnswer = null,
                            explanation = null,
                            isPartialFeedback = true
                        ),
                        isVisible = true
                    )
                }

                // Hidden feedback
                Box(modifier = Modifier.fillMaxWidth()) {
                    FeedbackPanel(
                        feedback = FeedbackUiState(
                            isCorrect = true,
                            message = "Correct!"
                        ),
                        isVisible = false
                    )
                }
            }
        }
    }
}