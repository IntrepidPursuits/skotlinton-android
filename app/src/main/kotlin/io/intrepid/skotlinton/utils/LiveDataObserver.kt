package io.intrepid.skotlinton.utils

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Interface for common utility methods used by LifecyleOwners (Activities and Fragments) relating to LiveData
 */
interface LiveDataObserver {

    val liveDataLifecycleOwner: LifecycleOwner

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(liveDataLifecycleOwner, Observer { observer(it) })
    }

    fun LiveData<Boolean>.bindToVisibility(view: View) {
        observe { view.visibility = if (it) View.VISIBLE else View.GONE }
    }

    fun LiveData<String>.bindToText(view: TextView) {
        observe { view.text = it }
    }
}
