package com.example.testbackendapi

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HttpHandler(url: String?, paramater: String?) {
    private var url: String? = null
    private var paramater: String? = null
    private var response_get: String? = null
    private var response_post: String? = null
    private var responseCode: Int? = null
    private lateinit var conn: HttpURLConnection
    private var headerfield: String? = null
    var jsonInputString: String? = null

    init {
        this.url = url
        this.paramater = paramater
    }

    fun geturl(): String? {
        return url
    }

    fun getparamter(): String? {
        return paramater
    }

    fun getServiceCall(): String {
        try {
            val myurl = URL(url + paramater)
            conn = myurl.openConnection() as HttpURLConnection
            conn.setUseCaches(true)
            conn.requestMethod = "GET"
            val input: InputStream = BufferedInputStream(conn.inputStream)
            response_get = convertStreamToString(input)
        } catch (e: Exception) {
            Log.e("testttt", "Exception" + e.message)
        }
        return response_get.toString()
    }

    fun postServiceCall(key: String, value: String): String {
        try {
            val myurl = URL(url)
            conn = myurl.openConnection() as HttpURLConnection
            conn.setUseCaches(true)
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            conn.doOutput = true
            conn.doOutput = true
            val os: OutputStream = conn.getOutputStream()
            val osw = OutputStreamWriter(os, "UTF-8")
            addBodyToPost(key, value)
            conn.outputStream.use { os ->
                val input: ByteArray = jsonInputString!!.toByteArray()
                os.write(input, 0, input.size)
            }
            osw.flush()
            osw.close()
            os.close()
            Log.d("hzm", conn.responseCode.toString())
            val input = BufferedInputStream(conn.getInputStream())
            response_post = convertStreamToString(input)
            println("Response : $response_post")
        } catch (e: Exception) {
            Log.e("error", "Exception" + e.message)
        }
        return response_post.toString()
    }

    fun getResponseCode(): String {
        responseCode = conn.responseCode
        return " \n responseCode:" + responseCode
    }

    fun getHeaderField(): String {
        headerfield =
            conn.getHeaderField(0) + conn.getHeaderField(1) + conn.getHeaderField(2) + conn.getHeaderField(
                3
            ) + conn.getHeaderField(4)
        return "\n header field:" + headerfield
    }

    fun getErrorMessage(): String {
        var errorBody: String
        try {
            errorBody = conn.errorStream.read().toString()
        } catch (e: Exception) {
            errorBody = " NO ERROR HAPPENED"
        }
        return errorBody
    }

    fun closeConn() {
        conn.disconnect()
    }


    private fun addBodyToPost(key: String?, values: String?) {
        jsonInputString = "{ \"" + key + "\" : \"" + values + "\"}"
    }

    private fun convertStreamToString(input: InputStream): String {
        val reader = BufferedReader(InputStreamReader(input))
        val stringbuilder = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                stringbuilder.append(line).append('\n')
            }
        } catch (e: IOException) {
            Log.e("convertStreamToString", "Exception" + e.message)
        } finally {
            try {
                input.close()
            } catch (e: IOException) {
                Log.e("convertStreamToString", "Exception" + e.message)
            }
        }
        return stringbuilder.toString()
    }

}