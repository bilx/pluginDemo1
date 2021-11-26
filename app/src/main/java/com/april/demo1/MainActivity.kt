package com.april.demo1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn1).setOnClickListener {


            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("router://com.demo1?token=b36a1a07-c24a-4a4a-8ace-683ca5e786c9&userid=223")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )

        }

    }


}