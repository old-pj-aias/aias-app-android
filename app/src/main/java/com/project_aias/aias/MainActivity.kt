package com.aias.aias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

object HelloWorld {
    external fun hello(input: String?): String?

    init {
        System.loadLibrary("samplers")
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val msg = HelloWorld.hello("Rust");
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}