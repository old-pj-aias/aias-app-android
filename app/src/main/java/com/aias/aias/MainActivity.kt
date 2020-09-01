package com.aias.aias

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, SignActivity::class.java)
        intent.putExtra("message", "message")
        startActivityForResult(intent, 9)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var result = "";

        if (requestCode != 9) { return }

        if (resultCode == Activity.RESULT_OK && data != null) {
            val message = data.getStringExtra("message")
            result = message

        } else if(resultCode == Activity.RESULT_CANCELED) {
            result = "CANCELED"
        }

        Toast.makeText(this,  result, Toast.LENGTH_LONG).show();
    }
}