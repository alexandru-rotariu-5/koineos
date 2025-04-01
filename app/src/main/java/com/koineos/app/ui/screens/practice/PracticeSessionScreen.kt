package com.koineos.app.ui.screens.practice

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.koineos.app.domain.model.practice.PracticeFlowState
import com.koineos.app.domain.utils.practice.ExerciseContentFactory
import com.koineos.app.presentation.model.practice.PracticeScreenUiState
import com.koineos.app.presentation.viewmodel.BasePracticeSessionViewModel
import com.koineos.app.ui.components.core.HeadlineSmall
import com.koineos.app.ui.components.core.NestedScreenScaffold
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.practice.FeedbackPanel
import com.koineos.app.ui.components.practice.PracticeActionButton
import com.koineos.app.ui.components.practice.PracticeTopBar
import com.koineos.app.ui.components.practice.QuitPracticeConfirmationDialog
import com.koineos.app.ui.screens.practice.components.AnimatedExerciseContent
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.Typography

/**
 * Main practice session screen that orchestrates the practice flow.
 *
 * @param viewModel The ViewModel to use
 * @param onNavigateToResults Callback when the practice session is completed
 * @param onClose Callback when the practice session is closed
 */
@Composable
fun PracticeSessionScreen(
    viewModel: BasePracticeSessionViewModel,
    onNavigateToResults: (totalExercises: Int, correctAnswers: Int, incorrectAnswers: Int, completionTimeMs: Long, accuracyPercentage: Float) -> Unit = { _, _, _, _, _ -> },
    onClose: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showQuitConfirmation by remember { mutableStateOf(false) }

    BackHandler {
        showQuitConfirmation = true
    }

    if (showQuitConfirmation) {
        QuitPracticeConfirmationDialog(
            onConfirm = onClose,
            onDismiss = { showQuitConfirmation = false }
        )
    }

    NestedScreenScaffold {
        PracticeSessionContent(
            uiState = uiState,
            onNavigateToResults = onNavigateToResults,
            onAnswerSelected = viewModel::onAnswerProvided,
            onActionButtonClick = viewModel::onActionButtonClick,
            onClose = { showQuitConfirmation = true }
        )
    }
}

@Composable
private fun PracticeSessionContent(
    uiState: PracticeScreenUiState,
    onNavigateToResults: (totalExercises: Int, correctAnswers: Int, incorrectAnswers: Int, completionTimeMs: Long, accuracyPercentage: Float) -> Unit = { _, _, _, _, _ -> },
    onAnswerSelected: (Any) -> Unit = {},
    onActionButtonClick: () -> Unit = {},
    onClose: () -> Unit,
) {
    LaunchedEffect(uiState) {
        if (uiState is PracticeScreenUiState.Completed) {
            onNavigateToResults(
                uiState.totalExercises,
                uiState.correctAnswers,
                uiState.incorrectAnswers,
                uiState.completionTimeMs,
                uiState.accuracyPercentage
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
        topBar = {
            if (uiState is PracticeScreenUiState.Loaded) {
                PracticeTopBar(
                    progress = uiState.progressPercentage,
                    onClose = onClose
                )
            }
        },
        containerColor = Colors.Background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is PracticeScreenUiState.Loading -> LoadingState()
                is PracticeScreenUiState.Error -> ErrorState(
                    message = uiState.message,
                    onRetry = uiState.retry
                )

                is PracticeScreenUiState.Loaded -> PracticeContentState(
                    state = uiState,
                    onAnswerSelected = onAnswerSelected,
                    onActionButtonClick = { onActionButtonClick() }
                )

                is PracticeScreenUiState.Completed -> {
                    // The navigation is handled in the LaunchedEffect
                }
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
            HeadlineSmall(
                text = state.currentExercise.instructions,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.paddingLarge)
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
                .padding(bottom = with(buttonDensity) {
                    (buttonHeightPx.intValue / density).dp + Dimensions.paddingLarge + Dimensions.spacingLarge
                })
                .zIndex(1f)
        )
    }
}