package io.intrepid.skotlinton.screens.example2

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import io.intrepid.skotlinton.R
import io.intrepid.skotlinton.base.BaseFragment
import io.intrepid.skotlinton.base.CommonViewModelDependencies
import io.intrepid.skotlinton.di.ActivityComponent
import io.intrepid.skotlinton.settings.UserSettings
import javax.inject.Inject

class Example2Fragment : BaseFragment<Example2ViewModel>() {

    @BindView(R.id.example2_current_ip)
    internal lateinit var currentIpView: TextView
    @BindView(R.id.example2_previous_ip)
    internal lateinit var previousIpView: TextView

    @Inject
    lateinit var userSettings: UserSettings

    override val layoutResourceId: Int = R.layout.fragment_example2

    override val viewModelClass = Example2ViewModel::class.java

    override fun injectDagger(component: ActivityComponent) {
        component.inject(this)
    }

    override fun createViewModel(commonDependencies: CommonViewModelDependencies): Example2ViewModel {
        return Example2ViewModel(commonDependencies, userSettings)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)

        with(viewModel) {
            currentIpAddressText.bindToText(currentIpView)
            previousIpAddressText.bindToText(previousIpView)
            previousIpAddressVisible.bindToVisibility(previousIpView)
        }
    }
}
