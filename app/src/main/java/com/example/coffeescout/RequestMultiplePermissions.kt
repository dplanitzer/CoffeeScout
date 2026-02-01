package com.example.coffeescout

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import kotlinx.coroutines.channels.Channel

// See: https://medium.com/@wind.orca.pe/handling-android-runtime-permissions-with-coroutines-and-suspend-functions-5b4aa4e74ee5
class RequestMultiplePermissions(componentActivity: ComponentActivity) : PermissionChecker {
    private val activityResultLauncher = with(componentActivity) {
        registerForActivityResult(RequestMultiplePermissions()) { result ->
            val m = result.mapValues { (key, value) ->
                if (value) {
                    PermissionChecker.State.Granted
                } else {
                    PermissionChecker.State.Denied(shouldShowRequestPermissionRationale(key))
                }
            }

            channel.trySend(PermissionChecker.Result(m))
        }
    }

    private val channel = Channel<PermissionChecker.Result>(1)

    suspend fun request(permissions: Set<String>): PermissionChecker.Result {
        activityResultLauncher.launch(permissions.toTypedArray())

        return channel.receive()
    }
}
