package com.koineos.app.di

import android.content.Context
import com.koineos.app.data.content.LetterJsonManager
import com.koineos.app.data.datastore.LetterMasteryDataStore
import com.koineos.app.data.repository.DefaultLetterMasteryRepository
import com.koineos.app.data.repository.DefaultLetterRepository
import com.koineos.app.domain.repository.LetterMasteryRepository
import com.koineos.app.domain.repository.LetterRepository
import com.koineos.app.domain.usecase.GetAllLettersUseCase
import com.koineos.app.domain.usecase.GetLetterByIdUseCase
import com.koineos.app.domain.usecase.GetLettersByRangeUseCase
import com.koineos.app.domain.usecase.UpdateLetterMasteryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlphabetModule {

    @Provides
    @Singleton
    fun provideLetterJsonManager(
        @ApplicationContext context: Context
    ): LetterJsonManager {
        return LetterJsonManager(context)
    }

    @Provides
    @Singleton
    fun provideLetterMasteryDataStore(
        @ApplicationContext context: Context
    ): LetterMasteryDataStore {
        return LetterMasteryDataStore(context)
    }

    @Provides
    @Singleton
    fun provideLetterRepository(
        letterJsonManager: LetterJsonManager
    ): LetterRepository {
        return DefaultLetterRepository(letterJsonManager)
    }

    @Provides
    @Singleton
    fun provideLetterMasteryRepository(
        letterMasteryDataStore: LetterMasteryDataStore
    ): LetterMasteryRepository {
        return DefaultLetterMasteryRepository(letterMasteryDataStore)
    }

    @Provides
    fun provideGetAllLettersUseCase(
        letterRepository: LetterRepository,
        letterMasteryRepository: LetterMasteryRepository
    ): GetAllLettersUseCase {
        return GetAllLettersUseCase(letterRepository, letterMasteryRepository)
    }

    @Provides
    fun provideGetLetterByIdUseCase(
        letterRepository: LetterRepository,
        letterMasteryRepository: LetterMasteryRepository
    ): GetLetterByIdUseCase {
        return GetLetterByIdUseCase(letterRepository, letterMasteryRepository)
    }

    @Provides
    fun provideGetLettersByRangeUseCase(
        letterRepository: LetterRepository,
        letterMasteryRepository: LetterMasteryRepository
    ): GetLettersByRangeUseCase {
        return GetLettersByRangeUseCase(letterRepository, letterMasteryRepository)
    }

    @Provides
    fun provideUpdateLetterMasteryUseCase(
        letterMasteryRepository: LetterMasteryRepository
    ): UpdateLetterMasteryUseCase {
        return UpdateLetterMasteryUseCase(letterMasteryRepository)
    }
}