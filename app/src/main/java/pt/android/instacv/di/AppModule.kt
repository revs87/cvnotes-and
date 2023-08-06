package pt.android.instacv.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pt.android.instacv.domain.repository.AuthRepository
import pt.android.instacv.data.local.SharedPreferencesRepository
import pt.android.instacv.data.local.SharedPreferencesRepositoryImpl
import pt.android.instacv.data.remote.firebase.FirebaseAuthRepositoryImpl
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
    fun providesAuthenticationRepository(spRepository: SharedPreferencesRepository): AuthRepository {
        return FirebaseAuthRepositoryImpl(spRepository)
    }

    
}