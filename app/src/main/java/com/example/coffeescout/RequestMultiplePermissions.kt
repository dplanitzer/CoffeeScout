// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
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
