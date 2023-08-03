package com.example.flamingo.index.home.subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flamingo.databinding.FragmentSecondBinding
import com.example.flamingo.databinding.FragmentSubscribeBinding

class SubscribeFragment : Fragment() {

    private var _binding: FragmentSubscribeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val subscribeViewModel =
            ViewModelProvider(this).get(SubscribeViewModel::class.java)

        _binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSubscribe
        subscribeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}