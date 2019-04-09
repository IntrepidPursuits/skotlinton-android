package io.intrepid.skotlinton.screens.example2

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import io.intrepid.skotlinton.R
import io.intrepid.skotlinton.base.BaseFragment
import io.intrepid.skotlinton.base.ViewModelConfiguration

class Example2Fragment : BaseFragment<Example2ViewModel>() {

    @BindView(R.id.example2_current_ip)
    internal lateinit var currentIpView: TextView
    @BindView(R.id.example2_previous_ip)
    internal lateinit var previousIpView: TextView

    override val layoutResourceId: Int = R.layout.fragment_example2

    override val viewModelClass = Example2ViewModel::class.java

    override fun createViewModel(configuration: ViewModelConfiguration): Example2ViewModel {
        return Example2ViewModel(configuration)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)

        viewModel.apply {
            currentIpAddressText.bindToText(currentIpView)
            previousIpAddressText.bindToText(previousIpView)
            previousIpAddressVisible.bindToVisibility(previousIpView)
        }
    }
}
