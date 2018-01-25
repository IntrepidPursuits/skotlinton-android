package io.intrepid.skotlinton.screens.example1

import android.content.Intent

import butterknife.OnClick
import io.intrepid.skotlinton.R
import io.intrepid.skotlinton.base.BaseMvpActivity
import io.intrepid.skotlinton.base.PresenterConfiguration
import io.intrepid.skotlinton.screens.example2.Example2Activity

class Example1Activity : BaseMvpActivity<Example1Contract.Presenter>(), Example1Contract.View {

    override val layoutResourceId: Int = R.layout.activity_example1

    override fun createPresenter(configuration: PresenterConfiguration): Example1Contract.Presenter {
        return Example1Presenter(this, configuration)
    }

    @Suppress("ProtectedInFinal")
    @OnClick(R.id.example1_button)
    protected fun onButtonClick() {
        presenter.onButtonClick()
    }

    override fun gotoExample2() {
        val intent = Intent(this, Example2Activity::class.java)
        startActivity(intent)
    }
}
