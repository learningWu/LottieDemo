package com.ws.lottie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()

    }

    private fun initView() {
        findViewById<Button>(R.id.btn1).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }
}