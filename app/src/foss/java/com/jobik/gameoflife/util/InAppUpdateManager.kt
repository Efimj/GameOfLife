package com.jobik.gameoflife.util

import android.content.Context
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
            if (showLatestVersionInstalled) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.installed_latest_version),
                    )
                }
            }
        }
    }
}