package com.example.flamingo.index.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flamingo.R

class SecondFragment : Fragment() {

    val testVM = SecondViewModel()

    companion object {
        fun newInstance() = SecondFragment()
    }

    private lateinit var viewModel: SecondViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SecondViewModel::class.java]
        // TODO: Use the ViewModel
        testVM.test.observe(this){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

}