package com.example.testbackendapi

import android.view.Menu
import android.view.MenuItem
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testbackendapi.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    //url that i test it:https://httpbin.org
    private lateinit var binding: ActivityMainBinding
    private lateinit var executorservice: ExecutorService
    private lateinit var networkChangeListener: NetworkChangeListener
    private val response_cache = "response_cache"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkChangeListener = NetworkChangeListener()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorservice = Executors.newSingleThreadExecutor()
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        binding.edurl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (Patterns.WEB_URL.matcher(edurl.text.toString()).matches()) {
                    binding.btget.isEnabled = true
                    binding.btpost.isEnabled = true
                } else {
                    binding.edurl.setError("invalid url")
                    binding.btget.isEnabled = false
                    binding.btpost.isEnabled = false
                }
            }
        })


        binding.btget.setOnClickListener {
            if (TextUtils.isEmpty(binding.edurl.text.toString())) {
                Toast.makeText(this, "URL is empty", Toast.LENGTH_LONG)
                    .show()
            } else {
                onEventStart()
                executorservice.execute {
                    val httphandler =
                        HttpHandler(binding.edurl.text.toString(), binding.edheader.text.toString())
                    val response =
                        "Get:" + httphandler.getServiceCall() + " ResponseCode:\n" + httphandler.getResponseCode() + "\n HeaderField:" + httphandler.getHeaderField() + "\n ErrorMessage:" + httphandler.getErrorMessage() + " \n URL:" + httphandler.geturl() + "\n Parameters:" + httphandler.getparamter()
                    runOnUiThread {
                        ResponseCacheGet(response,"GET")
                        onEventEnd()
                        binding.tvview.text = response
                        httphandler.closeConn()
                    }
                }
            }
        }

        binding.btpost.setOnClickListener {
            if (TextUtils.isEmpty(binding.edbody.text.toString()) && TextUtils.isEmpty(binding.edjsonkey.text.toString())) {
                Toast.makeText(this, "please check the body and key are fill", Toast.LENGTH_LONG)
                    .show()
            } else {
                onEventStart()
                executorservice.execute {
                    val httphandler =
                        HttpHandler(binding.edurl.text.toString(), binding.edheader.text.toString())
                    val response =
                        "Post:" + httphandler.postServiceCall(
                            binding.edjsonkey.text.toString(),
                            binding.edbody.text.toString()
                        ) + " ResponseCode:\n" + httphandler.getResponseCode() + "\n HeaderField:" + httphandler.getHeaderField() + "\n ErrorMessage:" + httphandler.getErrorMessage() + " \n URL:" + httphandler.geturl() + "\n Parameters" + httphandler.getparamter()
                    runOnUiThread {
                        ResponseCachePost(response,"POST")
                        onEventEnd()
                        binding.tvview.text = response
                        httphandler.closeConn()
                    }
                }
            }
        }
    }

    fun onEventEnd() {
        binding.btpost.isEnabled = true
        binding.btget.isEnabled = true
        binding.progressBar.visibility = View.INVISIBLE
    }

    fun onEventStart() {
        binding.btpost.isEnabled = false
        binding.btget.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("edurl", binding.edurl.text.toString())
        outState.putString("edheader", binding.edheader.text.toString())
        outState.putString("edjsonkey", binding.edjsonkey.text.toString())
        outState.putString("edbody", binding.edbody.text.toString())
        outState.putString("tvview", binding.tvview.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.edurl.setText(savedInstanceState.getString("edurl"))
        binding.edheader.setText(savedInstanceState.getString("edheader"))
        binding.edjsonkey.setText(savedInstanceState.getString("edjsonkey"))
        binding.edbody.setText(savedInstanceState.getString("edbody"))
        binding.tvview.setText(savedInstanceState.getString("tvview"))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cache -> {
                val intent = Intent(this@MainActivity, CacheActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun ResponseCacheGet(response: String,type:String) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(response_cache, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("response_key_get", response)
        editor.putString("type_key", type)
        editor.commit()
    }

    fun ResponseCachePost(response: String,type:String) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(response_cache, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("response_key_post", response)
        editor.putString("type_key", type)
        editor.commit()
    }



}