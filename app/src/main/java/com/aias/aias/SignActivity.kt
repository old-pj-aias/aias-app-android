package com.aias.aias

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import java.lang.Exception
import java.net.URLDecoder
import kotlin.concurrent.thread

data class IdResp(val id: Int)

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

    val tokenReqTemplate = """{"token": "TOKEN"}"""

    private val REQUEST_PNG_GET = 1
    private val REQUEST_NOPNG_GET = 2

    val SCAN_QR_CODE = "SCAN QR CODE"

    var ejPubkey : String? = null
    var message : String? = null
    var phoneNumber : String? = null
    var cookieHeader : Map<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        setResult(Activity.RESULT_CANCELED)

        AlertDialog.Builder(this)
            .setTitle("warning")
            .setMessage("Do you trust the caller app?")
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

        for (i in 1 until ejPubkeys.size - 1) {
            adapter.add(i.toString())
        }

        adapter.add(SCAN_QR_CODE)

        spinner.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.submit_phone -> {
                val spinner = findViewById<Spinner>(R.id.spinner)
                val ejIndex = spinner.selectedItem as String;

                if (ejIndex == SCAN_QR_CODE) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/png"

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent, REQUEST_PNG_GET)
                    }
                }

                else {
                    ejPubkey = ejPubkeys[ejIndex.toInt()];

                    val intent = Intent()

                    thread {
                        runOnUiThread {
                            onActivityResult(REQUEST_NOPNG_GET, RESULT_OK, intent);
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val intent = intent
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_PNG_GET) {
            val uri = data!!.data
            val inputStream = contentResolver.openInputStream(uri!!)

            val bitmap = BitmapFactory.decodeStream(inputStream)

            val qrReader = QRReader(bitmap)
            ejPubkey = qrReader.scan()
        }

        Toast.makeText(this, "Just a moments", Toast.LENGTH_LONG * 30).show()

        thread {
            try {
                val password = Crypto.loadPassword(this);
                if (password == null) {
                    runOnUiThread {
                        Toast.makeText(this, "Register first!", Toast.LENGTH_LONG).show()
                    }

                    return@thread
                }
                val tokenReq = tokenReqTemplate.replace("TOKEN", password!!);

                val (_, authResponse, authResult) = Fuel.post("http://10.0.2.2:8080/auth")
                    .body(tokenReq)
                    .response()

                val cookie: String = authResponse.headers["set-cookie"]?.first()!!
                val decodedCookie = cookie.split(' ').first()
                cookieHeader = mapOf("Cookie" to decodedCookie)

                val authenResp = String(authResponse.data)
                val mapper = jacksonObjectMapper()
                val idResp = mapper.readValue<IdResp>(authenResp)

                Aias.new(signerKey, ejPubkey, idResp.id.toString());

                val blindedDigest = Aias.ready(text, ejPubkey);

                val (_, readyResponse, _) = Fuel.post("http://10.0.2.2:8080/ready")
                    .header(cookieHeader)
                    .body(blindedDigest!!)
                    .response()

                val subset = String(readyResponse.data)
                Aias.setSubset(subset)

                val checkParam = Aias.generateCheckParameter()

                val (_, signResponse, _) = Fuel.post("http://10.0.2.2:8080/sign")
                    .header(cookieHeader)
                    .body(checkParam!!)
                    .response()

                val blindSignature = String(signResponse.data)
                val signature = Aias.unblind(blindSignature)

                runOnUiThread {
                    intent.putExtra(Intent.EXTRA_TEXT, signature)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    intent.putExtra(Intent.EXTRA_TEXT, "NG")
                    setResult(Activity.RESULT_CANCELED, intent)
                    Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}