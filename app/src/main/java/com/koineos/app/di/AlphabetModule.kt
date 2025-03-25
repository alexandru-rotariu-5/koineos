package com.koineos.app.di

import android.content.Context
import android.util.Log
import com.koineos.app.data.content.AlphabetLocalDataSource
import com.koineos.app.data.datastore.AlphabetMasteryDataStore
import com.koineos.app.data.repository.DefaultAlphabetMasteryRepository
import com.koineos.app.data.repository.DefaultAlphabetRepository
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import com.koineos.app.domain.service.BatchManagementService
import com.koineos.app.domain.service.EntitySelectionService
import com.koineos.app.domain.service.MasteryUpdateService
import com.koineos.app.domain.service.VariantSelectionService
import com.koineos.app.domain.usecase.alphabet.GenerateAlphabetPracticeSetUseCase
import com.koineos.app.domain.usecase.alphabet.GetAlphabetContentUseCase
import com.koineos.app.domain.usecase.alphabet.UpdateAlphabetEntityMasteryLevelsUseCase
import com.koineos.app.domain.utils.practice.EntityTargetIdentifier
import com.koineos.app.domain.utils.practice.PracticeManager
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityProvider
import com.koineos.app.domain.utils.practice.alphabet.AlphabetExerciseGenerator
import com.koineos.app.domain.utils.practice.alphabet.AlphabetPracticeSetGenerator
import com.koineos.app.domain.utils.practice.alphabet.BatchAwareAlphabetEntityProvider
import com.koineos.app.domain.utils.practice.alphabet.DefaultLetterCaseProvider
import com.koineos.app.domain.utils.practice.alphabet.LetterCaseProvider
import com.koineos.app.domain.utils.practice.alphabet.LetterGroupProvider
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
    private const val TAG = "AlphabetModule"

    @Provides
    @Singleton
    fun provideAlphabetLocalDataSource(): AlphabetLocalDataSource {
        Log.d(TAG, "Providing AlphabetLocalDataSource")
        return AlphabetLocalDataSource()
    }

    @Provides
    @Singleton
    fun provideAlphabetMasteryDataStore(
        @ApplicationContext context: Context
    ): AlphabetMasteryDataStore {
        Log.d(TAG, "Providing AlphabetMasteryDataStore")
        return AlphabetMasteryDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAlphabetRepository(
        alphabetLocalDataSource: AlphabetLocalDataSource
    ): AlphabetRepository {
        Log.d(TAG, "Providing AlphabetRepository")
        return DefaultAlphabetRepository(alphabetLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideAlphabetMasteryRepository(
        alphabetMasteryDataStore: AlphabetMasteryDataStore
    ): AlphabetMasteryRepository {
        Log.d(TAG, "Providing AlphabetMasteryRepository")
        return DefaultAlphabetMasteryRepository(alphabetMasteryDataStore)
    }

    @Provides
    @Singleton
    fun provideGetAlphabetContentUseCase(
        alphabetRepository: AlphabetRepository,
        alphabetMasteryRepository: AlphabetMasteryRepository
    ): GetAlphabetContentUseCase {
        Log.d(TAG, "Providing GetAlphabetContentUseCase")
        return GetAlphabetContentUseCase(alphabetRepository, alphabetMasteryRepository)
    }

    @Provides
    @Singleton
    fun provideBatchManagementService(): BatchManagementService {
        Log.d(TAG, "Providing BatchManagementService")
        return BatchManagementService()
    }

    @Provides
    @Singleton
    fun provideEntitySelectionService(): EntitySelectionService {
        Log.d(TAG, "Providing EntitySelectionService")
        return EntitySelectionService()
    }

    @Provides
    @Singleton
    fun provideAlphabetEntityProvider(
        alphabetRepository: AlphabetRepository,
        alphabetMasteryRepository: AlphabetMasteryRepository,
        batchManagementService: BatchManagementService,
        entitySelectionService: EntitySelectionService,
        variantSelectionService: VariantSelectionService
    ): AlphabetEntityProvider {
        Log.d(TAG, "Providing BatchAwareAlphabetEntityProvider")
        return BatchAwareAlphabetEntityProvider(
            alphabetRepository,
            alphabetMasteryRepository,
            batchManagementService,
            entitySelectionService,
            variantSelectionService
        )
    }

    @Provides
    @Singleton
    fun provideLetterCaseProvider(): LetterCaseProvider {
        Log.d(TAG, "Providing LetterCaseProvider")
        return DefaultLetterCaseProvider()
    }

    @Provides
    @Singleton
    fun provideLetterGroupProvider(): LetterGroupProvider {
        Log.d(TAG, "Providing LetterGroupProvider")
        return LetterGroupProvider()
    }

    @Provides
    @Singleton
    fun provideAlphabetExerciseGenerator(
        entityProvider: BatchAwareAlphabetEntityProvider,
        letterCaseProvider: LetterCaseProvider,
        letterGroupProvider: LetterGroupProvider
    ): AlphabetExerciseGenerator {
        Log.d(TAG, "Providing AlphabetExerciseGenerator")
        return AlphabetExerciseGenerator(
            entityProvider, letterCaseProvider, letterGroupProvider
        )
    }

    @Provides
    @Singleton
    fun provideAlphabetPracticeSetGenerator(
        alphabetExerciseGenerator: AlphabetExerciseGenerator,
        entityProvider: AlphabetEntityProvider
    ): AlphabetPracticeSetGenerator {
        Log.d(TAG, "Providing AlphabetPracticeSetGenerator")
        return AlphabetPracticeSetGenerator(alphabetExerciseGenerator, entityProvider)
    }

    @Provides
    @Singleton
    fun provideGenerateAlphabetPracticeSetUseCase(
        practiceManager: PracticeManager
    ): GenerateAlphabetPracticeSetUseCase {
        Log.d(TAG, "Providing GenerateAlphabetPracticeSetUseCase")
        return GenerateAlphabetPracticeSetUseCase(practiceManager)
    }

    @Provides
    @Singleton
    fun provideMasteryUpdateService(): MasteryUpdateService {
        Log.d(TAG, "Providing MasteryUpdateService")
        return MasteryUpdateService()
    }

    @Provides
    @Singleton
    fun provideEntityTargetIdentifier(): EntityTargetIdentifier {
        Log.d(TAG, "Providing EntityTargetIdentifier")
        return EntityTargetIdentifier()
    }

    @Provides
    fun provideUpdateAlphabetEntityMasteryLevelsUseCase(
        masteryUpdateService: MasteryUpdateService,
        entityTargetIdentifier: EntityTargetIdentifier,
        alphabetMasteryRepository: AlphabetMasteryRepository
    ): UpdateAlphabetEntityMasteryLevelsUseCase {
        Log.d(TAG, "Providing UpdateAlphabetEntityMasteryLevelsUseCase")
        return UpdateAlphabetEntityMasteryLevelsUseCase(
            masteryUpdateService,
            entityTargetIdentifier,
            alphabetMasteryRepository
        )
    }

    @Provides
    @Singleton
    fun provideVariantSelectionService(): VariantSelectionService {
        Log.d(TAG, "Providing VariantSelectionService")
        return VariantSelectionService()
    }
}