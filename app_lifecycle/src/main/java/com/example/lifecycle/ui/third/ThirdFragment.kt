package com.example.lifecycle.ui.third

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifecycle.R

class ThirdFragment : Fragment() {

    val testVM = ThirdViewModel()

    companion object {
        fun newInstance() = ThirdFragment()
    }

    private lateinit var viewModel: ThirdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThirdViewModel::class.java)
        // TODO: Use the ViewModel
        testVM.test.observe(this) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

}