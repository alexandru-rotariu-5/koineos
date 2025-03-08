package com.koineos.app.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.koineos.app.domain.model.practice.PracticeFlowState
import com.koineos.app.domain.utils.practice.ExerciseContentFactory
import com.koineos.app.presentation.model.practice.ActionButtonFactory
import com.koineos.app.presentation.model.practice.ActionButtonType
import com.koineos.app.presentation.model.practice.ActionButtonUiState
import com.koineos.app.presentation.model.practice.FeedbackUiState
import com.koineos.app.presentation.model.practice.PracticeScreenUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import com.koineos.app.presentation.viewmodel.BasePracticeSessionViewModel
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.practice.FeedbackPanel
import com.koineos.app.ui.components.practice.PracticeActionButton
import com.koineos.app.ui.components.practice.PracticeTopBar
import com.koineos.app.ui.screens.practice.components.AnimatedExerciseContent
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Main practice session screen that orchestrates the practice flow.
 *
 * @param viewModel The ViewModel to use
 * @param onNavigateToResults Navigation callback for the results screen
 * @param onClose Callback when the practice session is closed
 */
@Composable
fun PracticeSessionScreen(
    viewModel: BasePracticeSessionViewModel,
    onNavigateToResults: (String) -> Unit = {},
    onClose: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
        topBar = {
            if (uiState is PracticeScreenUiState.Loaded) {
                val state = uiState as PracticeScreenUiState.Loaded
                PracticeTopBar(
                    progress = state.progressPercentage,
                    onClose = onClose
                )
            }
        },
        containerColor = Colors.Surface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is PracticeScreenUiState.Loading -> LoadingState()
                is PracticeScreenUiState.Error -> ErrorState(
                    message = (uiState as PracticeScreenUiState.Error).message,
                    onRetry = (uiState as PracticeScreenUiState.Error).retry
                )

                is PracticeScreenUiState.Loaded -> PracticeContentState(
                    state = uiState as PracticeScreenUiState.Loaded,
                    onAnswerSelected = { answer -> viewModel.onAnswerProvided(answer) },
                    onActionButtonClick = { viewModel.onActionButtonClick() }
                )

                is PracticeScreenUiState.Completed -> PracticeCompletedState(
                    state = uiState as PracticeScreenUiState.Completed,
                    onDone = onClose
                )
            }
        }
    }
}

/**
 * Loading state with spinner.
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Colors.Primary)
    }
}

/**
 * Error state with message and retry button.
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong",
            style = Typography.headlineSmall,
            color = Colors.Error,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        Text(
            text = message,
            style = Typography.bodyLarge,
            color = Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))

            RegularButton(
                onClick = onRetry,
                text = "Try Again"
            )
        }
    }
}

/**
 * Main practice content when in progress.
 */
@Composable
private fun PracticeContentState(
    state: PracticeScreenUiState.Loaded,
    onAnswerSelected: (Any) -> Unit,
    onActionButtonClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    val buttonHeightPx = remember { mutableIntStateOf(0) }
    val buttonDensity = LocalDensity.current

    // Main container
    Box(modifier = Modifier.fillMaxSize()) {
        // Main screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Instructions
            Text(
                text = state.currentExercise.instructions,
                style = Typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Start,
                color = Colors.OnSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.paddingLarge),
            )

            // Exercise content
            AnimatedExerciseContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                currentIndex = state.currentExerciseIndex,
            ) { index ->
                if (index < state.exercises.size) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ExerciseContentFactory.CreateExerciseContent(
                            exerciseState = state.exercises[index],
                            onAnswerSelected = onAnswerSelected,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

            PracticeActionButton(
                buttonState = state.actionButtonState,
                onClick = onActionButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.paddingLarge)
                    .padding(horizontal = Dimensions.paddingLarge)
                    .onGloballyPositioned { coordinates ->
                        buttonHeightPx.intValue = coordinates.size.height
                    }
                    .zIndex(0f)
            )
        }

        // Feedback panel overlay
        FeedbackPanel(
            feedback = state.feedback,
            isVisible = state.flowState == PracticeFlowState.FEEDBACK,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = Dimensions.paddingLarge)
                .padding(
                    bottom = with(buttonDensity) {
                        (buttonHeightPx.intValue / density).dp + Dimensions.paddingLarge + Dimensions.spacingLarge
                    }
                )
                .zIndex(1f)
        )
    }
}

/**
 * Practice completed state showing results.
 */
@Composable
private fun PracticeCompletedState(
    state: PracticeScreenUiState.Completed,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Practice Completed!",
            style = Typography.headlineMedium,
            color = Colors.Primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))

        Text(
            text = "Score: ${state.correctAnswers} / ${state.totalExercises}",
            style = Typography.titleLarge,
            color = Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        Text(
            text = "Accuracy: ${state.accuracyPercentage.toInt()}%",
            style = Typography.titleMedium,
            color = Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingXXLarge))

        RegularButton(
            onClick = onDone,
            text = "Done"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeScreenLoadingPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Scaffold {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    LoadingState()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeScreenErrorPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Scaffold {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    ErrorState(
                        message = "Failed to load practice exercises. Please check your connection and try again.",
                        onRetry = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeContentStatePreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Scaffold(
                topBar = {
                    PracticeTopBar(
                        progress = 0.3f,
                        onClose = {}
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    PracticeContentState(
                        state = PracticeScreenUiState.Loaded(
                            currentExerciseIndex = 2,
                            exercises = listOf(
                                SelectTransliterationExerciseUiState(
                                    id = "exercise1",
                                    instructions = "What sound does this make?",
                                    letterDisplay = "Γ γ",
                                    letterName = "gamma",
                                    options = listOf("a", "b", "g", "d"),
                                    selectedAnswer = null
                                )
                            ),
                            userAnswers = emptyMap(),
                            exerciseResults = emptyMap(),
                            flowState = PracticeFlowState.IN_PROGRESS,
                            feedback = null,
                            actionButtonState = ActionButtonFactory.check(false)
                        ),
                        onAnswerSelected = {},
                        onActionButtonClick = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeContentWithFeedbackPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Scaffold(
                topBar = {
                    PracticeTopBar(
                        progress = 0.3f,
                        onClose = {}
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    PracticeContentState(
                        state = PracticeScreenUiState.Loaded(
                            currentExerciseIndex = 2,
                            exercises = listOf(
                                SelectTransliterationExerciseUiState(
                                    id = "exercise1",
                                    instructions = "What sound does this make?",
                                    letterDisplay = "Γ γ",
                                    letterName = "gamma",
                                    options = listOf("a", "b", "g", "d"),
                                    selectedAnswer = "g"
                                )
                            ),
                            userAnswers = mapOf("exercise1" to "g"),
                            exerciseResults = mapOf("exercise1" to true),
                            flowState = PracticeFlowState.FEEDBACK,
                            feedback = FeedbackUiState(
                                isCorrect = true,
                                message = "Correct!",
                                explanation = "The letter gamma (Γ γ) makes the 'g' sound."
                            ),
                            actionButtonState = ActionButtonUiState(
                                text = "Continue",
                                isEnabled = true,
                                type = ActionButtonType.CONTINUE
                            )
                        ),
                        onAnswerSelected = {},
                        onActionButtonClick = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeCompletedStatePreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Scaffold {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    PracticeCompletedState(
                        state = PracticeScreenUiState.Completed(
                            totalExercises = 15,
                            correctAnswers = 12,
                            incorrectAnswers = 3,
                            completionTimeMs = 300000, // 5 minutes
                            accuracyPercentage = 80f
                        ),
                        onDone = {}
                    )
                }
            }
        }
    }
}