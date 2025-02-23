package com.nguyenmoclam.simpleapp.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * A singleton object that provides a shared CoroutineScope for handling asynchronous operations in data binding.
 * This scope is tied to the main dispatcher and uses a single job that can be cancelled to cleanup resources.
 *
 * Important: The scope should be cancelled when it's no longer needed (typically in onDestroy of the host Activity)
 * to prevent memory leaks and ensure proper cleanup of coroutine jobs.
 */
object CoroutineScopeSingleton {
  private val job = Job()
  val scope = CoroutineScope(Dispatchers.Main + job)

  fun cancel() {
    job.cancel()
  }
}
