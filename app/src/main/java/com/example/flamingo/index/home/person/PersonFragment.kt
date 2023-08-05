package com.example.flamingo.index.home.person

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.flamingo.base.fragment.VBaseFragment
import com.example.flamingo.databinding.FragmentPersonBinding
import com.example.flamingo.utils.getViewModel

class PersonFragment : VBaseFragment<FragmentPersonBinding>() {

    private val viewModel by lazy { getViewModel<PersonViewModel>() }
    override val binding by viewBinding<FragmentPersonBinding>(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textPerson
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

}