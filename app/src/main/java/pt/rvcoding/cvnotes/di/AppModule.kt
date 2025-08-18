package pt.rvcoding.cvnotes.di

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pt.rvcoding.cvnotes.BuildConfig
import pt.rvcoding.cvnotes.data.repository.NoteRepositoryImpl
import pt.rvcoding.cvnotes.data.repository.SectionRepositoryImpl
import pt.rvcoding.cvnotes.data.repository.SharedPreferencesRepositoryImpl
import pt.rvcoding.cvnotes.data.repository.firebase.FirebaseAuthRepositoryImpl
import pt.rvcoding.cvnotes.data.source.NoteDatabase
import pt.rvcoding.cvnotes.data.source.SectionDatabase
import pt.rvcoding.cvnotes.domain.CVGenerativeLLM.Companion.GEMINI_LLM_VERSION
import pt.rvcoding.cvnotes.domain.repository.AuthRepository
import pt.rvcoding.cvnotes.domain.repository.NoteRepository
import pt.rvcoding.cvnotes.domain.repository.SectionRepository
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import pt.rvcoding.cvnotes.domain.use_case.NoteUseCases
import pt.rvcoding.cvnotes.domain.use_case.SectionUseCases
import pt.rvcoding.cvnotes.domain.use_case.note.DeleteNote
import pt.rvcoding.cvnotes.domain.use_case.note.DeleteSelectedNotes
import pt.rvcoding.cvnotes.domain.use_case.note.GenerateNotesUseCase
import pt.rvcoding.cvnotes.domain.use_case.note.GetNoteById
import pt.rvcoding.cvnotes.domain.use_case.note.GetNotes
import pt.rvcoding.cvnotes.domain.use_case.note.GetNotesBySectionId
import pt.rvcoding.cvnotes.domain.use_case.note.HasSelectedNotes
import pt.rvcoding.cvnotes.domain.use_case.note.InsertNote
import pt.rvcoding.cvnotes.domain.use_case.note.UnselectAllNotes
import pt.rvcoding.cvnotes.domain.use_case.section.DeleteSection
import pt.rvcoding.cvnotes.domain.use_case.section.DeleteSelectedSections
import pt.rvcoding.cvnotes.domain.use_case.section.GenerateSectionsUseCase
import pt.rvcoding.cvnotes.domain.use_case.section.GetSectionById
import pt.rvcoding.cvnotes.domain.use_case.section.GetSections
import pt.rvcoding.cvnotes.domain.use_case.section.GetSectionsWithNotes
import pt.rvcoding.cvnotes.domain.use_case.section.HasSelectedSections
import pt.rvcoding.cvnotes.domain.use_case.section.InsertSection
import pt.rvcoding.cvnotes.domain.use_case.section.SelectSection
import pt.rvcoding.cvnotes.domain.use_case.section.UnselectAllSections
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePackageInfo(@ApplicationContext context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(@ApplicationContext context: Context): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(@ApplicationContext context: Context, spRepository: SharedPreferencesRepository): AuthRepository {
        FirebaseApp.initializeApp(context)
        return FirebaseAuthRepositoryImpl(spRepository)
    }

    @Provides
    @Singleton
    fun providesNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
            .addMigrations(
                NoteDatabase.MIGRATION_1_2,
            )
            .build()
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
            getNotesBySectionId = providesGetNotesBySectionId(noteRepository),
            getNoteById = GetNoteById(noteRepository),
            insertNote = InsertNote(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            hasSelectedNotes = HasSelectedNotes(noteRepository),
            unselectAllNotes = UnselectAllNotes(noteRepository),
            deleteSelectedNotes = DeleteSelectedNotes(noteRepository),
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
        noteRepository: NoteRepository,
        spRepository: SharedPreferencesRepository
    ): SectionUseCases {
        return SectionUseCases(
            getSectionsWithNotes = GetSectionsWithNotes(spRepository, sectionRepository, noteRepository),
            getSections = GetSections(spRepository, sectionRepository),
            getSectionById = GetSectionById(sectionRepository),
            insertSection = InsertSection(spRepository, sectionRepository),
            selectSection = SelectSection(sectionRepository),
            deleteSection = DeleteSection(sectionRepository),
            deleteSelectedSections = DeleteSelectedSections(spRepository, sectionRepository),
            hasSelectedSections = HasSelectedSections(spRepository, sectionRepository),
            unselectAllSections = UnselectAllSections(spRepository, sectionRepository),
        )
    }

    @Provides
    @Singleton
    fun provideGenerativeLLM() = GenerativeModel(
        modelName = GEMINI_LLM_VERSION,
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    @Provides
    @Singleton
    fun providesGenerateSections() = GenerateSectionsUseCase(
        model = provideGenerativeLLM()
    )

    @Provides
    @Singleton
    fun providesGetNotesBySectionId(noteRepository: NoteRepository) = GetNotesBySectionId(
        noteRepository = noteRepository
    )

    @Provides
    @Singleton
    fun providesGenerateNotes(noteRepository: NoteRepository) = GenerateNotesUseCase(
        model = provideGenerativeLLM(),
        noteRepository = noteRepository
    )
}