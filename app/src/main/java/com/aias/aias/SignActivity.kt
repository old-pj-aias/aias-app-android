package com.aias.aias

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import org.w3c.dom.Text
import java.io.File
import kotlin.concurrent.thread


class SignActivity : AppCompatActivity(), View.OnClickListener {
    val signerKey = """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxXo2zWkciUEZBcm/Exk8
Zac8NWskP59EAVFlO218xIXOV0FfphPB/tnbQh7GDXddo7XVEptHdHXyJlXXLihb
9vXbUZF2NDFLOhgDv7pa72VNLbw+jKR/FlsDtwv/bv7ZDqq+n79uavuJ8giX3qCf
+mtBmro7hG5AVve3JImhvA0FvTKJ0xCYUYw02st08He5RwFAXQK8G2cwahp+5ECH
MDdfFUaoxMfRN/+Hl9iqiJovKUJQ3545N2fDYdd0eqSlqL1N5xJxYX1GDMtGZgME
hHR6ntdfm7r43HDB4hk/MJIsNay6+K9tJBiz1qXG40G4NjMKzVrX9pi1Bv8G2RnP
/wIDAQAB
-----END PUBLIC KEY-----"""

    val ejPubkeys = listOf("""-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxXo2zWkciUEZBcm/Exk8
Zac8NWskP59EAVFlO218xIXOV0FfphPB/tnbQh7GDXddo7XVEptHdHXyJlXXLihb
9vXbUZF2NDFLOhgDv7pa72VNLbw+jKR/FlsDtwv/bv7ZDqq+n79uavuJ8giX3qCf
+mtBmro7hG5AVve3JImhvA0FvTKJ0xCYUYw02st08He5RwFAXQK8G2cwahp+5ECH
MDdfFUaoxMfRN/+Hl9iqiJovKUJQ3545N2fDYdd0eqSlqL1N5xJxYX1GDMtGZgME
hHR6ntdfm7r43HDB4hk/MJIsNay6+K9tJBiz1qXG40G4NjMKzVrX9pi1Bv8G2RnP
/wIDAQAB
-----END PUBLIC KEY-----""",
        """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxXo2zWkciUEZBcm/Exk8
Zac8NWskP59EAVFlO218xIXOV0FfphPB/tnbQh7GDXddo7XVEptHdHXyJlXXLihb
9vXbUZF2NDFLOhgDv7pa72VNLbw+jKR/FlsDtwv/bv7ZDqq+n79uavuJ8giX3qCf
+mtBmro7hG5AVve3JImhvA0FvTKJ0xCYUYw02st08He5RwFAXQK8G2cwahp+5ECH
MDdfFUaoxMfRN/+Hl9iqiJovKUJQ3545N2fDYdd0eqSlqL1N5xJxYX1GDMtGZgME
hHR6ntdfm7r43HDB4hk/MJIsNay6+K9tJBiz1qXG40G4NjMKzVrX9pi1Bv8G2RnP
/wIDAQAB
-----END PUBLIC KEY-----""",
        """-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxXo2zWkciUEZBcm/Exk8
Zac8NWskP59EAVFlO218xIXOV0FfphPB/tnbQh7GDXddo7XVEptHdHXyJlXXLihb
9vXbUZF2NDFLOhgDv7pa72VNLbw+jKR/FlsDtwv/bv7ZDqq+n79uavuJ8giX3qCf
+mtBmro7hG5AVve3JImhvA0FvTKJ0xCYUYw02st08He5RwFAXQK8G2cwahp+5ECH
MDdfFUaoxMfRN/+Hl9iqiJovKUJQ3545N2fDYdd0eqSlqL1N5xJxYX1GDMtGZgME
hHR6ntdfm7r43HDB4hk/MJIsNay6+K9tJBiz1qXG40G4NjMKzVrX9pi1Bv8G2RnP
/wIDAQAB
-----END PUBLIC KEY-----""",
    "SCAN QR CODE");

    private val REQUEST_PNG_GET = 1
    val SCAN_QR_CODE = "SCAN QR CODE"

    var ejPubkey : String? = null
    var message : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        setResult(Activity.RESULT_CANCELED);

        AlertDialog.Builder(this)
            .setTitle("warning")
            .setMessage("Are you trust calling app?")
            .setPositiveButton("OK") { _, _ ->

            }
            .setNegativeButton("Cancel") { _: DialogInterface, i: Int ->
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            .show()

        val spinner = findViewById<Spinner>(R.id.spinner)

        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        for (i in 1..ejPubkeys.size) {
            adapter.add(i.toString())
        }

        adapter.add(SCAN_QR_CODE)

        spinner.adapter = adapter
    }

    override fun onClick(v: View?) {
        val intent = intent
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

        intent.putExtra(Intent.EXTRA_TEXT, "piyo")
        setResult(Activity.RESULT_OK, intent)
        finish()

//        when (v!!.id) {
//            R.id.submit_phone -> {
//                val spinner = findViewById<Spinner>(R.id.spinner)
//                val ejIndex = spinner.selectedItem as String;
//
//                if (ejIndex == SCAN_QR_CODE) {
//                    val intent = Intent(Intent.ACTION_GET_CONTENT)
//                    intent.type = "image/png"
//
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivityForResult(intent, REQUEST_PNG_GET)
//                    }
//                }
//
//                else {
//                    ejPubkey = ejPubkeys[ejIndex.toInt()];
//
//                }
//            }
//
//            R.id.submit_code -> {
//                thread {
//                    Aias.new(signerKey, ejPubkey);
//
//                    val blindedDigest = Aias.blind(text);
//
//                    val (_, readyResponse, _) = Fuel.post("http://192.168.0.24:8080/ready")
//                        .body(blindedDigest!!)
//                        .response()
//
//                    val subset = String(readyResponse.data)
//                    Aias.setSubset(subset)
//
//                    val checkParam = Aias.generateCheckParameter();
//
//                    val (_, signResponse, _) = Fuel.post("http://192.168.0.24:8080/sign")
//                        .body(checkParam!!)
//                        .response()
//
//                    val blindSignature = String(signResponse.data)
//                    val signature = Aias.unblind(blindSignature)
//
//                    runOnUiThread {
//                        Toast.makeText(this, signature, Toast.LENGTH_LONG).show();
//
//                        intent.putExtra("message", signature)
//                        setResult(Activity.RESULT_OK, intent)
//                        finish()
//                    }
//                }
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PNG_GET && resultCode == RESULT_OK) {
            val uri = data!!.data
            val inputStream = contentResolver.openInputStream(uri!!)

            val bitmap = BitmapFactory.decodeStream(inputStream)

            val qrReader = QRReader(bitmap)
            ejPubkey = qrReader.scan()

            setContentView(R.layout.activity_sms_code)
        }
    }
}