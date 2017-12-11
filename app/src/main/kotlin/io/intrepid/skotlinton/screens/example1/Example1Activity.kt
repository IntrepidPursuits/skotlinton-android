package io.intrepid.skotlinton.screens.example1

import android.content.Intent
import butterknife.OnClick
import io.intrepid.skotlinton.R
import io.intrepid.skotlinton.base.BaseActivity
import io.intrepid.skotlinton.base.PresenterConfiguration
import io.intrepid.skotlinton.screens.example2.Example2Activity

class Example1Activity : BaseActivity<Example1Screen, Example1Presenter>(), Example1Screen {

    override val layoutResourceId: Int = R.layout.activity_example1

    override fun createPresenter(configuration: PresenterConfiguration): Example1Presenter {
        return Example1Presenter(this, configuration)
    }

    @OnClick(R.id.example1_button)
    internal fun onButtonClick() {
        presenter.onButtonClick()
    }

    override fun gotoExample2() {
        val intent = Intent(this, Example2Activity::class.java)
        startActivity(intent)
    }
}
