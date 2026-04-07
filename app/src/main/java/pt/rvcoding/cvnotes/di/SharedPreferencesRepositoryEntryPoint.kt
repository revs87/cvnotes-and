package pt.rvcoding.cvnotes.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SharedPreferencesRepositoryEntryPoint {
    fun sharedPreferencesRepository(): SharedPreferencesRepository
}
