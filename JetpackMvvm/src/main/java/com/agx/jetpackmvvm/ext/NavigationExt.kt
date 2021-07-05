package com.agx.jetpackmvvm.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment

fun Fragment.nav(): NavController? {
    return if (isAdded) {
        NavHostFragment.findNavController(this)
    } else {
        null
    }
}

fun nav(view: View): NavController {
    return Navigation.findNavController(view)
}