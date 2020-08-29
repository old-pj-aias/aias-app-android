package com.aias.aias

import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread


class SignActivity : AppCompatActivity() {
    val publicKey = """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxXo2zWkciUEZBcm/Exk8
Zac8NWskP59EAVFlO218xIXOV0FfphPB/tnbQh7GDXddo7XVEptHdHXyJlXXLihb
9vXbUZF2NDFLOhgDv7pa72VNLbw+jKR/FlsDtwv/bv7ZDqq+n79uavuJ8giX3qCf
+mtBmro7hG5AVve3JImhvA0FvTKJ0xCYUYw02st08He5RwFAXQK8G2cwahp+5ECH
MDdfFUaoxMfRN/+Hl9iqiJovKUJQ3545N2fDYdd0eqSlqL1N5xJxYX1GDMtGZgME
hHR6ntdfm7r43HDB4hk/MJIsNay6+K9tJBiz1qXG40G4NjMKzVrX9pi1Bv8G2RnP
/wIDAQAB
-----END PUBLIC KEY-----
"""
    val subset = """{"subset":[2,7,8,11,12,13,14,18,23,25,26,27,28,30,31,33,35,38,39,40,41,42,43,44,46,48,54,55,56,58,62,65,68,69,70,71,74,75,77,78],"complement":[1,3,4,5,6,9,10,15,16,17,19,20,21,22,24,29,32,34,36,37,45,47,49,50,51,52,53,57,59,60,61,63,64,66,67,72,73,76,79,80]}"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        AlertDialog.Builder(this)
            .setTitle("warning")
            .setMessage("Are you trust calling app?")
            .setPositiveButton("OK") { dialog, which ->
                thread {
                    Aias.new(publicKey, publicKey);

                    val text = intent.getStringExtra("message")
                    val blindedDigest = Aias.blind(text);
                    Aias.setSubset(subset)
                    val checkParam = Aias.generateCheckParameter();

                    runOnUiThread {
                        Toast.makeText(this, blindedDigest, Toast.LENGTH_LONG).show();
                        Toast.makeText(this, checkParam, Toast.LENGTH_LONG).show()
                    }
                }

            }
            .setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int ->
                finish()
            })
            .show()

    }
}