package io.intrepid.skotlinton.testutils

import androidx.test.espresso.IdlingResource
import kotlinx.coroutines.Job

// IdlingResource that blocks if any coroutine job from the dispatcher is running
// see https://github.com/Kotlin/kotlinx.coroutines/issues/242
class CoroutineIdlingResource(dispatcher: InterceptableDispatcher) : IdlingResource {
    private val jobs: MutableSet<Job> = mutableSetOf()
    private var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.onJobAdded = { job ->
            jobs.add(job)
            job.invokeOnCompletion {
                if (allJobsComplete) {
                    callback?.onTransitionToIdle()
                }
            }
        }
    }

    override fun getName(): String {
        return javaClass.simpleName
    }

    override fun isIdleNow(): Boolean {
        return allJobsComplete
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    private val allJobsComplete: Boolean
        get() {
            jobs.removeAll { !it.isActive }
            return jobs.isEmpty()
        }
}
