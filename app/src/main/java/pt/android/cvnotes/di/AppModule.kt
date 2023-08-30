package pt.android.cvnotes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pt.android.cvnotes.data.repository.NoteRepositoryImpl
import pt.android.cvnotes.data.repository.SectionRepositoryImpl
import pt.android.cvnotes.data.repository.SharedPreferencesRepositoryImpl
import pt.android.cvnotes.data.repository.firebase.FirebaseAuthRepositoryImpl
import pt.android.cvnotes.data.source.NoteDatabase
import pt.android.cvnotes.data.source.SectionDatabase
import pt.android.cvnotes.domain.repository.AuthRepository
import pt.android.cvnotes.domain.repository.NoteRepository
import pt.android.cvnotes.domain.repository.SectionRepository
import pt.android.cvnotes.domain.repository.SharedPreferencesRepository
import pt.android.cvnotes.domain.use_case.NoteUseCases
import pt.android.cvnotes.domain.use_case.SectionUseCases
import pt.android.cvnotes.domain.use_case.note.DeleteNote
import pt.android.cvnotes.domain.use_case.note.GetNoteById
import pt.android.cvnotes.domain.use_case.note.GetNotes
import pt.android.cvnotes.domain.use_case.note.GetNotesBySectionId
import pt.android.cvnotes.domain.use_case.note.InsertNote
import pt.android.cvnotes.domain.use_case.section.DeleteSection
import pt.android.cvnotes.domain.use_case.section.DeleteSelectedSections
import pt.android.cvnotes.domain.use_case.section.GetSectionById
import pt.android.cvnotes.domain.use_case.section.GetSections
import pt.android.cvnotes.domain.use_case.section.GetSectionsWithNotes
import pt.android.cvnotes.domain.use_case.section.HasSelectedSections
import pt.android.cvnotes.domain.use_case.section.InsertSection
import pt.android.cvnotes.domain.use_case.section.SelectSection
import pt.android.cvnotes.domain.use_case.section.UnselectAllSections
import pt.android.cvnotes.ui.dashboard.DashboardViewModel
import pt.android.cvnotes.ui.section_details.SectionDetailsViewModel
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
            getNotesBySectionId = GetNotesBySectionId(noteRepository),
            getNoteById = GetNoteById(noteRepository),
            insertNote = InsertNote(noteRepository),
            deleteNote = DeleteNote(noteRepository),
        )
    }

    @Provides
    @Singleton
    fun providesSectionDatabase(app: Application): SectionDatabase {
        return Room.databaseBuilder(
            app,
            SectionDatabase::class.java,
            SectionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesSectionRepository(db: SectionDatabase): SectionRepository {
        return SectionRepositoryImpl(db.sectionDao)
    }

    @Provides
    @Singleton
    fun providesSectionUseCases(
        sectionRepository: SectionRepository,
        noteRepository: NoteRepository
    ): SectionUseCases {
        return SectionUseCases(
            getSectionsWithNotes = GetSectionsWithNotes(sectionRepository, noteRepository),
            getSections = GetSections(sectionRepository),
            getSectionById = GetSectionById(sectionRepository),
            insertSection = InsertSection(sectionRepository),
            selectSection = SelectSection(sectionRepository),
            deleteSection = DeleteSection(sectionRepository),
            deleteSelectedSections = DeleteSelectedSections(sectionRepository),
            hasSelectedSections = HasSelectedSections(sectionRepository),
            unselectAllSections = UnselectAllSections(sectionRepository),
        )
    }

    @Provides
    @Singleton
    fun providesDashboardViewModel(
        sectionUseCases: SectionUseCases,
        noteUseCases: NoteUseCases,
    ): DashboardViewModel {
        return DashboardViewModel(sectionUseCases, noteUseCases)
    }

    @Provides
    @Singleton
    fun providesSectionDetailsViewModel(
        sectionUseCases: SectionUseCases,
        noteUseCases: NoteUseCases,
    ): SectionDetailsViewModel {
        return SectionDetailsViewModel(sectionUseCases, noteUseCases)
    }
}