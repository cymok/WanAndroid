package com.example.flamingo.index.home.person

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.flamingo.base.fragment.VBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.databinding.FragmentPersonBinding
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent

class PersonFragment : VBaseFragment<FragmentPersonBinding>() {

    private val viewModel: PersonViewModel get() = getViewModel()
    override val binding: FragmentPersonBinding by viewBinding(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        val textView: TextView = binding.textPerson
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    private fun initImmersion() {
        val statusBarHeight = BarUtils.getStatusBarHeight()
//        binding.rv.topPadding = statusBarHeight
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB_CHANGE) {

        }
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            val index = arguments?.getInt("homeIndex")
            if (it == index && lifecycle.currentState == Lifecycle.State.RESUMED) {

            }
        }
    }

}