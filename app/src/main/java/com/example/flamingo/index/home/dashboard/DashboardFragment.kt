package com.example.flamingo.index.home.dashboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.flamingo.base.fragment.VBaseFragment
import com.example.flamingo.databinding.FragmentDashboardBinding
import com.example.flamingo.utils.getViewModel

class DashboardFragment : VBaseFragment<FragmentDashboardBinding>() {

    private val viewModel by lazy { getViewModel<DashboardViewModel>() }
    override val binding by viewBinding<FragmentDashboardBinding>(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textDashboard
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

}