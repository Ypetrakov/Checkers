package com.example.checkers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.checkers.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.startButton.setOnClickListener {
            val myIntent =  Intent(this, GameActivity::class.java)
            startActivity(myIntent)
        }

        setContentView(binding.root)

    }
}