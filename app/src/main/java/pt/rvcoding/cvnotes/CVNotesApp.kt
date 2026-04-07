package pt.rvcoding.cvnotes

import android.app.Application
import androidx.appfunctions.service.AppFunctionConfiguration
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import pt.rvcoding.cvnotes.appfunctions.CVNotesAppFunctions
import pt.rvcoding.cvnotes.di.AppFunctionsEntryPoint

@HiltAndroidApp
class CVNotesApp : Application(), AppFunctionConfiguration.Provider {

    override val appFunctionConfiguration: AppFunctionConfiguration
        get() {
            val entryPoint = EntryPointAccessors.fromApplication(
                this,
                AppFunctionsEntryPoint::class.java,
            )
            return AppFunctionConfiguration.Builder()
                .addEnclosingClassFactory(CVNotesAppFunctions::class.java) {
                    entryPoint.cvNotesAppFunctions()
                }
                .build()
        }
}
