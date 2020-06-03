package com.souza.billsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.souza.billsapp.connectivity.Connectivity

class HomeActivity : AppCompatActivity() {

    private lateinit var connectivitySnackbar: Snackbar
    private lateinit var connectivity: Connectivity
    private var hasNetworkConnectivity = true
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.fragment_container_main_activity)
        NavigationUI.setupActionBarWithNavController(this, navController)

        initConnectivityCallback()
        initConnectivitySnackbar()
        initConnectivityObserver()
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragment_container_main_activity)
        return navController.navigateUp()
    }

    private fun initConnectivityObserver() {
        connectivity.observe(this@HomeActivity, Observer { hasNetworkConnectivity ->
            this.hasNetworkConnectivity = hasNetworkConnectivity
            viewModel.mustShowConnectivitySnackbar(hasNetworkConnectivity = hasNetworkConnectivity)
        })

        viewModel.apply {
            showConnectivityOnSnackbar().observe(this@HomeActivity, Observer {
                this@HomeActivity.showConnectivityOnSnackbar()
            })

            showConnectivityOffSnackbar().observe(this@HomeActivity, Observer {
                this@HomeActivity.showConnectivityOffSnackbar()
            })
        }
    }

    private fun initConnectivityCallback() {
        connectivity = Connectivity(application)
    }

    private fun initConnectivitySnackbar() {
        connectivitySnackbar =
            Snackbar.make(
                findViewById(R.id.fragment_container_main_activity),
                "internet voltou",//getString(R.string.snackbar_message_internet_back),
                Snackbar.LENGTH_INDEFINITE
            )
    }

    private fun showConnectivityOnSnackbar() {
        connectivitySnackbar.duration = BaseTransientBottomBar.LENGTH_SHORT
        connectivitySnackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        connectivitySnackbar.setText("internet voltou")//(getString(R.string.snackbar_message_internet_back))
        connectivitySnackbar.show()
    }

    private fun showConnectivityOffSnackbar() {
        connectivitySnackbar.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        connectivitySnackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        connectivitySnackbar.setText("internet off")//(getString(R.string.snackbar_internet_off))
        connectivitySnackbar.show()
    }
}

