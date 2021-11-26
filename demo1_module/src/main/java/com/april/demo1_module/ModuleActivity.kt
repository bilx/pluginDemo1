package com.april.demo1_module

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class ModuleActivity : AppCompatActivity() {

    private val TAG = "native demo 1"
    var token: String? = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module)

        setListener()

        getParams(intent)

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getParams(intent)
    }


    ///读取deeplink 参数
    private fun getParams(intent: Intent?) {

        intent?.data?.let { uri ->

            Log.d(TAG, "deeplink uri  = $uri")
            Log.d(
                TAG, "param = ${uri.queryParameterNames} , path = ${uri.path} , host = ${uri.host}"
            )

            uri.queryParameterNames.forEach {

                Log.d(
                    TAG, "param $it -> ${uri.getQueryParameter(it)}"
                )
            }


            token = uri.getQueryParameter("token")

            //TODO：自己根据传递的参数进行逻辑处理
        }


    }


    private fun setListener() {
        findViewById<Button?>(R.id.btn1)?.setOnClickListener {

            thread {

                val client: OkHttpClient =
                    OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build()
                val request = Request.Builder().header("cookie", "token=$token")
                    .url("http://192.168.2.213:4900/api/system/ext/status/list").build()
                val call: Call = client.newCall(request)
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace();

                        Log.d(TAG,"请求失败 -> ${e.message}")

                        this@ModuleActivity.runOnUiThread {
                            Toast.makeText(
                                this@ModuleActivity,
                                "请求失败-${e.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body =  response.body?.string()
                        Log.d(TAG,"请求成功 -> $body")
                        this@ModuleActivity.runOnUiThread {
                            Toast.makeText(
                                this@ModuleActivity,
                                "请求成功-${body}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

            }
        }
    }
}
