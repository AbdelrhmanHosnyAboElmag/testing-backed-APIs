package com.example.testbackendapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testbackendapi.databinding.ActivityCacheBinding
import com.example.testbackendapi.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_cache.*


class CacheActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCacheBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache)
        binding = ActivityCacheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences =
            applicationContext.getSharedPreferences("response_cache", MODE_PRIVATE)
        val response_data_get= sharedPreferences.getString("response_key_get", "no cahe until now")
        val response_data_post= sharedPreferences.getString("response_key_post", "no cahe until now")
        val type = sharedPreferences.getString("type_key", "no cahe until now")

        binding.rbget.setOnClickListener {
            binding.tvcahce.setText(response_data_get)
        }
        binding.rbpost.setOnClickListener {
            binding.tvcahce.setText(response_data_post)
        }

    }

}