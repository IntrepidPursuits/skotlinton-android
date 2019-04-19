package io.intrepid.skotlinton.screens.example1

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import butterknife.OnClick
import io.intrepid.skotlinton.R
import io.intrepid.skotlinton.base.BaseMvvmActivity
import io.intrepid.skotlinton.base.CommonViewModelDependencies
import io.intrepid.skotlinton.di.ActivityComponent
import io.intrepid.skotlinton.screens.example2.Example2Activity
import io.intrepid.skotlinton.utils.TimeProvider
import io.intrepid.skotlinton.utils.ViewEvent
import javax.inject.Inject

@Suppress("ProtectedInFinal")
class Example1Activity : BaseMvvmActivity<Example1ViewModel>() {

    @Inject
    lateinit var timeProvider: TimeProvider

    override val layoutResourceId: Int = R.layout.activity_example1

    override val viewModelClass: Class<out ViewModel> = Example1ViewModel::class.java

    override fun injectDagger(component: ActivityComponent) {
        component.inject(this)
    }

    override fun createViewModel(commonDependencies: CommonViewModelDependencies): Example1ViewModel {
        return Example1ViewModel(commonDependencies, timeProvider)
    }

    override fun onViewEvent(event: ViewEvent) {
        when (event) {
            is Example1ViewEvent.GotoExample2 -> gotoExample2()
            is Example1ViewEvent.ShowCurrentTimeToast -> showToast(event.message)
        }
    }

    @OnClick(R.id.next_button)
    protected fun onButtonClick() {
        viewModel.onNextClick()
    }

    @OnClick(R.id.show_toast_button)
    protected fun onShowToastClick() {
        viewModel.onShowToastClick()
    }

    private fun gotoExample2() {
        val intent = Intent(this, Example2Activity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
