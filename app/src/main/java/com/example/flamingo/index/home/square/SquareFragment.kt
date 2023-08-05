package com.example.flamingo.index.home.square

import android.os.Bundle
import android.view.View
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.flamingo.base.fragment.VBaseFragment
import com.example.flamingo.databinding.FragmentSquareBinding
import com.example.flamingo.utils.getViewModel

class SquareFragment : VBaseFragment<FragmentSquareBinding>() {

    private val viewModel by lazy { getViewModel<SquareViewModel>() }

    // `by viewBinding()` 委托在 `onDestroyView` 里执行 `binding = null`
    override val binding by viewBinding<FragmentSquareBinding>(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textSquare
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        viewModel.getSquareList(0)

    }

}