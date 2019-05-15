package io.intrepid.skotlinton.screens.example2

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import io.intrepid.skotlinton.R
import io.intrepid.skotlinton.base.BaseFragment
import io.intrepid.skotlinton.di.ActivityComponent
import io.intrepid.skotlinton.settings.UserSettings
import javax.inject.Inject

class Example2Fragment : BaseFragment() {

    @BindView(R.id.example2_current_ip)
    internal lateinit var currentIpView: TextView
    @BindView(R.id.example2_previous_ip)
    internal lateinit var previousIpView: TextView

    @Inject
    lateinit var userSettings: UserSettings

    override val layoutResourceId: Int = R.layout.fragment_example2

    override val viewModel: Example2ViewModel by viewModelFactory {
        Example2ViewModel(commonViewModelDependencies, userSettings)
    }

    override fun injectDagger(component: ActivityComponent) {
        component.inject(this)
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
