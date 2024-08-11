package pt.rvcoding.cvnotes.ui.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions {

    fun handle(
        activity: Activity,
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        launcherMultiple: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
        onGranted: () -> Unit,
        onShowRequestPermissionRationale: () -> Unit
    ) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> onGranted.invoke()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                requestPermission(
                    activity = activity,
                    launcher = launcher,
                    onGranted = onGranted,
                    onShowRequestPermissionRationale = onShowRequestPermissionRationale
                )
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                requestMultiplePermissions(
                    activity = activity,
                    launcher = launcherMultiple,
                    onGranted = onGranted,
                    onShowRequestPermissionRationale = onShowRequestPermissionRationale
                )
            }
            else -> {
                requestPermission(
                    activity = activity,
                    launcher = launcher,
                    onGranted = onGranted,
                    onShowRequestPermissionRationale = onShowRequestPermissionRationale
                )
            }
        }
    }

    private fun requestMultiplePermissions(
        activity: Activity,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
        onGranted: () -> Unit,
        onShowRequestPermissionRationale: () -> Unit,
        permissions: Array<String> = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
    ) {
        when {
            checkPermission(activity.applicationContext, READ_EXTERNAL_STORAGE) && checkPermission(activity.applicationContext, WRITE_EXTERNAL_STORAGE) -> onGranted.invoke()
            shouldShowRequestPermissionRationale(activity, READ_EXTERNAL_STORAGE, 0) || shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE, 0) -> onShowRequestPermissionRationale.invoke()
            else -> launcher.launch(arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE))
        }
    }
    private fun requestPermission(
        activity: Activity,
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        onGranted: () -> Unit,
        onShowRequestPermissionRationale: () -> Unit,
        permission: String = WRITE_EXTERNAL_STORAGE
    ) {
        when {
            checkPermission(activity, permission) -> onGranted.invoke()
            shouldShowRequestPermissionRationale(activity, permission, 0) -> onShowRequestPermissionRationale.invoke()
            else -> launcher.launch(permission)
        }
    }
    private fun checkPermission(context: Context, permission: String): Boolean = ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    private fun shouldShowRequestPermissionRationale(activity: Activity, permission: String, i: Int): Boolean = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}