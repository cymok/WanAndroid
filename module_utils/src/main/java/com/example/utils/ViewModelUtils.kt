package com.example.utils

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T : ViewModel> ComponentActivity.getViewModel(clazz: Class<T>) =
    ViewModelProvider(this)[clazz]

/**
 * owner 是 fragment
 */
fun <T : ViewModel> Fragment.getViewModel(clazz: Class<T>) =
    ViewModelProvider(this)[clazz]

/**
 * owner 是 activity
 */
fun <T : ViewModel> Fragment.getViewModelOfActivity(clazz: Class<T>) =
    ViewModelProvider(requireActivity())[clazz]
