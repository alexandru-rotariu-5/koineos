package com.koineos.app.di

import android.content.Context
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.domain.utils.practice.PracticeManager
import com.koineos.app.domain.utils.practice.alphabet.AlphabetPracticeSetGenerator
import com.koineos.app.domain.utils.practice.alphabet.RandomLetterProvider
import com.koineos.app.ui.utils.AndroidStringProvider
import com.koineos.app.ui.utils.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing general app dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStringProvider(
        @ApplicationContext context: Context
    ): StringProvider {
        return AndroidStringProvider(context)
    }

    @Provides
    @Singleton
    fun providePracticeManager(
        alphabetPracticeSetGenerator: AlphabetPracticeSetGenerator,
        letterProvider: RandomLetterProvider
    ): PracticeManager {
        return PracticeManager(alphabetPracticeSetGenerator, letterProvider)
    }

    @Provides
    fun provideValidateExerciseAnswerUseCase(): ValidateExerciseAnswerUseCase {
        return ValidateExerciseAnswerUseCase()
    }

    @Provides
    fun provideCompletePracticeSetUseCase(): CompletePracticeSetUseCase {
        return CompletePracticeSetUseCase()
    }
}