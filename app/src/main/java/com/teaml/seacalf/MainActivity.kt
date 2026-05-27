package com.teaml.seacalf

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var coinsAmountText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesManager = PreferencesManager.getInstance(this)
        coinsAmountText = findViewById(R.id.coins_amount)

        refreshHeader()

        val navView: BottomNavigationView = findViewById(R.id.nav_menu)
        navView.itemIconTintList = null

        if (savedInstanceState == null) {
            showFragment(TasksTabFragment())
        }

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.task_tab -> {
                    showFragment(TasksTabFragment())
                    true
                }
                R.id.pet_tab -> {
                    showFragment(FragmentPetTab())
                    true
                }
                R.id.stats_tab -> {
                    showFragment(FragmentStatsTab())
                    true
                }
                else -> false
            }
        }
    }

    fun refreshHeader() {
        coinsAmountText.text = preferencesManager.getCoinsAmount().toString()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }
}