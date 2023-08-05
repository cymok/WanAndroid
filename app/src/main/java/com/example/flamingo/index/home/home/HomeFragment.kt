package com.example.flamingo.index.home.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.flamingo.base.fragment.VBaseFragment
import com.example.flamingo.databinding.FragmentHomeBinding
import com.example.flamingo.utils.getViewModel

class HomeFragment : VBaseFragment<FragmentHomeBinding>() {

    private val viewModel by lazy { getViewModel<HomeViewModel>() }
    override val binding by viewBinding<FragmentHomeBinding>(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

}