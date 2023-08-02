package com.example.flamingo.utils

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> ComponentActivity.getViewModel() =
    ViewModelProvider(this)[T::class.java]

/**
 * owner 是 fragment
 */
inline fun <reified T : ViewModel> Fragment.getViewModel() =
    ViewModelProvider(this)[T::class.java]

/**
 * owner 是 activity
 */
inline fun <reified T : ViewModel> Fragment.getViewModelOfActivity() =
    ViewModelProvider(requireActivity())[T::class.java]
