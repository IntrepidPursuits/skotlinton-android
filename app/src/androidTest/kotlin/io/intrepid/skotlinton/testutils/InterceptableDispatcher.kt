package io.intrepid.skotlinton.testutils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// Wrapper dispatcher that adds hooks for when new jobs are added.
// This is a temporary solution until an official way is provided
// see https://github.com/Kotlin/kotlinx.coroutines/issues/242
class InterceptableDispatcher(private val parent: CoroutineDispatcher) :
    CoroutineDispatcher() {

    var onJobAdded: ((Job) -> Unit)? = null

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        context[Job]?.let { onJobAdded?.invoke(it) }
        parent.dispatch(context, block)
    }

    @InternalCoroutinesApi
    override fun dispatchYield(context: CoroutineContext, block: Runnable) {
        context[Job]?.let { onJobAdded?.invoke(it) }
        parent.dispatchYield(context, block)
    }

    @ExperimentalCoroutinesApi
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        context[Job]?.let { onJobAdded?.invoke(it) }
        return parent.isDispatchNeeded(context)
    }
}
