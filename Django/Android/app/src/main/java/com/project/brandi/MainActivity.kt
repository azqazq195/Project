package com.project.brandi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.brandi.data.user.LoginResponse
import com.project.brandi.util.Resource
import com.project.brandi.util.snackBar

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var mainNavHostFragment: NavHostFragment

    lateinit var drawerLayout: DrawerLayout
    lateinit var mainLayoutRoot: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayoutRoot = findViewById(R.id.mainLayoutRoot)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        mainNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(mainNavHostFragment.findNavController())
    }
}