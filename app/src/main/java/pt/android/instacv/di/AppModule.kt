package pt.android.instacv.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pt.android.instacv.data.repository.NoteRepositoryImpl
import pt.android.instacv.data.repository.SharedPreferencesRepositoryImpl
import pt.android.instacv.data.repository.firebase.FirebaseAuthRepositoryImpl
import pt.android.instacv.data.source.NoteDatabase
import pt.android.instacv.domain.repository.AuthRepository
import pt.android.instacv.domain.repository.NoteRepository
import pt.android.instacv.domain.repository.SharedPreferencesRepository
import pt.android.instacv.domain.use_case.note.DeleteNote
import pt.android.instacv.domain.use_case.note.GetNoteById
import pt.android.instacv.domain.use_case.note.GetNotes
import pt.android.instacv.domain.use_case.note.InsertNote
import pt.android.instacv.domain.use_case.NoteUseCases
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(@ApplicationContext context: Context): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(spRepository: SharedPreferencesRepository): AuthRepository {
        return FirebaseAuthRepositoryImpl(spRepository)
    }

    @Provides
    @Singleton
    fun providesNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun providesNoteUseCases(noteRepository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(noteRepository),
            getNoteById = GetNoteById(noteRepository),
            insertNote = InsertNote(noteRepository),
            deleteNote = DeleteNote(noteRepository),
        )
    }

}