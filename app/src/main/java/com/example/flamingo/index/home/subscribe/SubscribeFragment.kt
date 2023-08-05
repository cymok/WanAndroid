package com.example.flamingo.index.home.subscribe

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.flamingo.base.fragment.VBaseFragment
import com.example.flamingo.databinding.FragmentSubscribeBinding
import com.example.flamingo.utils.getViewModel

class SubscribeFragment : VBaseFragment<FragmentSubscribeBinding>() {

    private val viewModel by lazy { getViewModel<SubscribeViewModel>() }
    override val binding by viewBinding<FragmentSubscribeBinding>(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textSubscribe
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

}