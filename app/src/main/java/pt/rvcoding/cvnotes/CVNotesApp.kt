package pt.rvcoding.cvnotes

import android.app.Application
import androidx.appfunctions.service.AppFunctionConfiguration
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.EntryPointAccessors
import pt.rvcoding.cvnotes.appfunctions.CVNotesAppFunctions
import pt.rvcoding.cvnotes.di.AppFunctionsEntryPoint

@HiltAndroidApp
class CVNotesApp : Application(), AppFunctionConfiguration.Provider {

    override fun getAppFunctionConfiguration(): AppFunctionConfiguration {
        val entryPoint = EntryPointAccessors.fromApplication(
            this,
            AppFunctionsEntryPoint::class.java,
        )
        return AppFunctionConfiguration(
            mapOf(
                CVNotesAppFunctions::class.java to { entryPoint.cvNotesAppFunctions() },
            ),
        )
    }
}
