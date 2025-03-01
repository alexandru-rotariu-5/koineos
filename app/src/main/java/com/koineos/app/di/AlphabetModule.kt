package com.koineos.app.di

import android.content.Context
import com.koineos.app.data.content.AlphabetLocalDataSource
import com.koineos.app.data.datastore.AlphabetMasteryDataStore
import com.koineos.app.data.repository.DefaultAlphabetMasteryRepository
import com.koineos.app.data.repository.DefaultAlphabetRepository
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import com.koineos.app.domain.usecase.alphabet.GenerateAlphabetPracticeSetUseCase
import com.koineos.app.domain.usecase.alphabet.GetAlphabetContentUseCase
import com.koineos.app.domain.usecase.alphabet.UpdateAlphabetEntityMasteryUseCase
import com.koineos.app.domain.utils.practice.PracticeManager
import com.koineos.app.domain.utils.practice.alphabet.AlphabetExerciseGenerator
import com.koineos.app.domain.utils.practice.alphabet.AlphabetPracticeSetGenerator
import com.koineos.app.domain.utils.practice.alphabet.RandomLetterProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies related to the Alphabet feature
 */
@Module
@InstallIn(SingletonComponent::class)
object AlphabetModule {

    @Provides
    @Singleton
    fun provideAlphabetLocalDataSource(): AlphabetLocalDataSource {
        return AlphabetLocalDataSource()
    }

    @Provides
    @Singleton
    fun provideAlphabetMasteryDataStore(
        @ApplicationContext context: Context
    ): AlphabetMasteryDataStore {
        return AlphabetMasteryDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAlphabetRepository(
        alphabetLocalDataSource: AlphabetLocalDataSource
    ): AlphabetRepository {
        return DefaultAlphabetRepository(alphabetLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideAlphabetMasteryRepository(
        alphabetMasteryDataStore: AlphabetMasteryDataStore
    ): AlphabetMasteryRepository {
        return DefaultAlphabetMasteryRepository(alphabetMasteryDataStore)
    }

    @Provides
    fun provideGetAlphabetContentUseCase(
        alphabetRepository: AlphabetRepository,
        alphabetMasteryRepository: AlphabetMasteryRepository
    ): GetAlphabetContentUseCase {
        return GetAlphabetContentUseCase(alphabetRepository, alphabetMasteryRepository)
    }

    @Provides
    fun provideUpdateAlphabetMasteryUseCase(
        alphabetMasteryRepository: AlphabetMasteryRepository
    ): UpdateAlphabetEntityMasteryUseCase {
        return UpdateAlphabetEntityMasteryUseCase(alphabetMasteryRepository)
    }

    @Provides
    @Singleton
    fun provideLetterProvider(
        alphabetRepository: AlphabetRepository
    ): RandomLetterProvider {
        return RandomLetterProvider(alphabetRepository)
    }

    @Provides
    @Singleton
    fun provideAlphabetExerciseGenerator(
        letterProvider: RandomLetterProvider
    ): AlphabetExerciseGenerator {
        return AlphabetExerciseGenerator(letterProvider)
    }

    @Provides
    @Singleton
    fun provideAlphabetPracticeSetGenerator(
        alphabetExerciseGenerator: AlphabetExerciseGenerator,
        letterProvider: RandomLetterProvider
    ): AlphabetPracticeSetGenerator {
        return AlphabetPracticeSetGenerator(alphabetExerciseGenerator, letterProvider)
    }

    @Provides
    fun provideGenerateAlphabetPracticeSetUseCase(
        practiceManager: PracticeManager
    ): GenerateAlphabetPracticeSetUseCase {
        return GenerateAlphabetPracticeSetUseCase(practiceManager)
    }
}