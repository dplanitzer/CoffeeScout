package com.example.coffeescout

// See: https://medium.com/@wind.orca.pe/handling-android-runtime-permissions-with-coroutines-and-suspend-functions-5b4aa4e74ee5
interface PermissionChecker {
    class Result(m: Map<String, State>) : Map<String, State> by HashMap(m) {
        operator fun component1(): Set<String> = granted()
        operator fun component2(): Set<String> = denied()

        private fun denied() = filterValues(State::isDenied).keys
        private fun granted() = filterValues(State::isGranted).keys
    }

    sealed interface State {
        val shouldShowRequestPermissionRationale: Boolean

        fun isGranted() = this is Granted
        fun isDenied() = this is Denied

        data object Granted : State {
            override val shouldShowRequestPermissionRationale: Boolean = false
        }

        data class Denied(
            override val shouldShowRequestPermissionRationale: Boolean
        ) : State
    }
}
