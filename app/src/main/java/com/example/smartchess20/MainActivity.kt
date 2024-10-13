package com.example.smartchess20

import NavigationAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartchess20.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    /* logic */
    private lateinit var navigationAdapter: NavigationAdapter

    /* binding */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        navigationAdapter = NavigationAdapter { fragmentClass ->
            displayFragment(fragmentClass)
        }
        binding.navigationRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = navigationAdapter
        }
    }

    // TODO fragment factory
    private fun displayFragment(fragmentClass: Class<out Fragment>) {
        val fragment = fragmentClass.newInstance();
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment
            )
            .addToBackStack(null) // Add to back stack to allow back navigation
            .commit()
    }
}
