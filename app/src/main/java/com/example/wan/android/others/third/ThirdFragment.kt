package com.example.wan.android.others.third

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.example.wan.android.base.fragment.BaseFragment
import com.example.wan.android.databinding.FragmentThirdBinding
import com.example.wan.android.utils.getViewModel
import kotlinx.coroutines.launch

class ThirdFragment : BaseFragment() {

    companion object {
        fun newInstance() = ThirdFragment()
    }

    private val viewModel by lazy {
        getViewModel<ThirdViewModel>()
    }

    private val binding by lazy {
        FragmentThirdBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
        viewModel.viewModelScope.launch {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}