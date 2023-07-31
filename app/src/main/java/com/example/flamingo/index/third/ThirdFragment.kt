package com.example.flamingo.index.third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flamingo.base.BaseFragment
import com.example.flamingo.databinding.FragmentThirdBinding
import com.example.flamingo.utils.getViewModel

class ThirdFragment : BaseFragment() {

    companion object {
        fun newInstance() = ThirdFragment()
    }

    private  val viewModel by lazy {
        getViewModel(ThirdViewModel::class.java)
    }

    private val binding by lazy {
        FragmentThirdBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}