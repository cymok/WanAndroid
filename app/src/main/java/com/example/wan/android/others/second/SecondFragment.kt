package com.example.wan.android.others.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.example.wan.android.R
import com.example.wan.android.base.fragment.BaseFragment
import com.example.wan.android.databinding.FragmentSecondBinding
import com.example.wan.android.utils.getViewModel

class SecondFragment : BaseFragment() {

    companion object {
        fun newInstance() = SecondFragment()
    }

    private val viewModel by lazy {
        getViewModel<SecondViewModel>()
    }

    private val binding by lazy {
        FragmentSecondBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.test.observe(this) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.iv.load("https://pic1.zhimg.com/v2-087b76d6a57befbf484e3aa8138037fa_1440w.jpg?source=172ae18b") {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.mipmap.ic_launcher)
//            transformations(RoundedCornersTransformation(ConvertUtils.dp2px(16f).toFloat()))
        }
    }

}