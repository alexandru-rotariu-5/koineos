package com.koineos.app.ui.screens.practice.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedExerciseContent(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    exerciseContent: @Composable (Int) -> Unit,
) {
    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                    (slideOutHorizontally { width -> -width } + fadeOut())
        },
        label = "exercise_transition",
        modifier = modifier
    ) { targetIndex ->
        exerciseContent(targetIndex)
    }
}