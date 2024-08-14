package com.jobik.gameoflife.util

import android.app.Activity
import android.content.Context
import androidx.compose.material3.SnackbarResult
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.jobik.gameoflife.R
import com.jobik.gameoflife.util.SnackbarHostUtil.snackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @property onUpdateDownloaded Called to suggest restarting the application and installing the downloaded update.
 */
class InAppUpdateManager(
    private val context: Context,
    private val onUpdateDownloaded: (completeUpdate: () -> Unit) -> Unit
) {
    companion object {
        fun checkAppUpdate(
            context: Context,
            scope: CoroutineScope,
            showLatestVersionInstalled: Boolean = true
        ) {
            val updateManager = InAppUpdateManager(
                context = context,
                onUpdateDownloaded = { completeUpdate ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.getString(R.string.update_downloaded),
                            actionLabel = context.getString(R.string.restart),
                            withDismissAction = true
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> {}
                            SnackbarResult.ActionPerformed -> {
                                completeUpdate()
                            }
                        }
                    }
                })

            updateManager.checkForUpdates(
                onUpdateAvailable = { update ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "${context.getString(R.string.update_available)} ${update.availableVersionCode()}",
                            actionLabel = context.getString(R.string.download),
                            withDismissAction = true
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> {}
                            SnackbarResult.ActionPerformed -> {
                                updateManager.startUpdate(update)
                            }
                        }
                    }
                },
                onUpdateUnavailable = {
                    if (showLatestVersionInstalled) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.installed_latest_version),
                            )
                        }
                    }
                })
        }
    }

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            onUpdateDownloaded { appUpdateManager.completeUpdate() }
            unregisterUpdateStatusListener()
        }
    }

    private fun unregisterUpdateStatusListener() {
        appUpdateManager.unregisterListener(listener)
    }

    private fun registerUpdateStatusListener() {
        appUpdateManager.registerListener(listener)
    }

    fun checkForUpdates(
        onUpdateAvailable: (AppUpdateInfo) -> Unit,
        onUpdateUnavailable: (Exception?) -> Unit
    ) {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                onUpdateAvailable(appUpdateInfo)
            } else {
                onUpdateUnavailable(null)
            }
        }
            .addOnFailureListener { onUpdateUnavailable(it) }
    }

    fun startUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlow(
            appUpdateInfo,
            context as Activity,
            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
        )
        registerUpdateStatusListener()
    }
}