package com.jurengis.rollcall.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jurengis.rollcall.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_rollcall.*

@AndroidEntryPoint
class RollCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rollcall)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        bottomNavigationMenu.setupWithNavController(rollCallNavHostFragment.findNavController())

        rollCallNavHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.courseListFragment -> bottomNavigationMenu.visibility = View.VISIBLE
                    else -> bottomNavigationMenu.visibility = View.GONE
                }
            }
    }
}