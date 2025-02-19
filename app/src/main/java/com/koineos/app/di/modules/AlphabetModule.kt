package com.koineos.app.di.modules

import android.content.Context
import com.koineos.app.data.content.LetterJsonManager
import com.koineos.app.data.content.mapper.LetterMapper
import com.koineos.app.data.datastore.LetterMasteryDataStore
import com.koineos.app.data.repository.DefaultLetterRepository
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
    fun provideLetterMapper(): LetterMapper {
        return LetterMapper()
    }

    @Provides
    @Singleton
    fun provideLetterRepository(
        letterJsonManager: LetterJsonManager,
        letterMasteryDataStore: LetterMasteryDataStore,
        letterMapper: LetterMapper
    ): LetterRepository {
        return DefaultLetterRepository(
            letterJsonManager = letterJsonManager,
            letterMasteryDataStore = letterMasteryDataStore,
            letterMapper = letterMapper
        )
    }

    @Provides
    fun provideGetAllLettersUseCase(
        letterRepository: LetterRepository
    ): GetAllLettersUseCase {
        return GetAllLettersUseCase(letterRepository)
    }

    @Provides
    fun provideGetLetterByIdUseCase(
        letterRepository: LetterRepository
    ): GetLetterByIdUseCase {
        return GetLetterByIdUseCase(letterRepository)
    }

    @Provides
    fun provideGetLettersByRangeUseCase(
        letterRepository: LetterRepository
    ): GetLettersByRangeUseCase {
        return GetLettersByRangeUseCase(letterRepository)
    }

    @Provides
    fun provideUpdateLetterMasteryUseCase(
        letterRepository: LetterRepository
    ): UpdateLetterMasteryUseCase {
        return UpdateLetterMasteryUseCase(letterRepository)
    }
}