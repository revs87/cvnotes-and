package pt.rvcoding.cvnotes.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pt.rvcoding.cvnotes.appfunctions.CVNotesAppFunctions

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppFunctionsEntryPoint {
    fun cvNotesAppFunctions(): CVNotesAppFunctions
}
