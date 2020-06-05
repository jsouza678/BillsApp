package com.souza.billsapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.souza.billsapp.databinding.FragmentSplashBinding
import com.souza.billsapp.extensions.visible
import com.souza.billsapp.login.presentation.LoginViewModel

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        bottomNavigationView = activity?.findViewById(R.id.bottom_nav_menu)!!
        (activity as AppCompatActivity).supportActionBar?.hide()

        return binding.root
    }

    private fun turnOnBottomNavigation() {
        bottomNavigationView.visible()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Handler().postDelayed({
                        navController.navigate(R.id.action_splashFragment_to_billFragment)
                        turnOnBottomNavigation()
                    }, 1000)
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    Handler().postDelayed({
                        navController.navigate(R.id.action_splashFragment_to_loginFragment)
                    }, 1000)
                }
                else -> Log.e(
                    "fragment", "New $authenticationState state that doesn't require any UI change"
                )
            }
        })
    }
}
