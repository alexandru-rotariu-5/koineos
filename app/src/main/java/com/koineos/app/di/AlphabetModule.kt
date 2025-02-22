package com.koineos.app.di

import android.content.Context
import com.koineos.app.data.content.AlphabetLocalDataSource
import com.koineos.app.data.datastore.AlphabetMasteryDataStore
import com.koineos.app.data.repository.DefaultAlphabetMasteryRepository
import com.koineos.app.data.repository.DefaultAlphabetRepository
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import com.koineos.app.domain.usecase.GetAlphabetContentUseCase
import com.koineos.app.domain.usecase.UpdateAlphabetEntityMasteryUseCase
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
    fun provideGetAlphabetcontentUseCase(
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
}