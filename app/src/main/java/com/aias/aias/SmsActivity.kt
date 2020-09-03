package com.aias.aias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import kotlin.concurrent.thread

data class TokenResp(val token: String)

class SmsActivity : AppCompatActivity(), View.OnClickListener{
    var phoneNumber : String? = null
    val phoneReqTemplate = """{"phone_number":"PHONE_NUMBER"}"""
    val secretCodeTemaplate = """{"code":SECRET_CODE}"""

    var cookieHeader : Map<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.submit_phone -> thread {
                phoneNumber = findViewById<EditText>(R.id.secret_code).text.toString();
                val phoneReq = phoneReqTemplate.replace("PHONE_NUMBER", phoneNumber!!)

                val (_, smsResponse, smsResult) = Fuel.post("http://192.168.0.24:8080/send_sms")
                    .body(phoneReq)
                    .response()

                val cookie : String = smsResponse.headers["set-cookie"]?.first()!!
                val decodedCookie = cookie.split(' ').first()
                cookieHeader = mapOf("Cookie" to decodedCookie)

                runOnUiThread {
                    setContentView(R.layout.activity_sms_code)
                }
            }

            R.id.submit_code -> thread {
                val secretCode = findViewById<EditText>(R.id.secret_code).text.toString();
                val verifyReq = secretCodeTemaplate.replace("SECRET_CODE", secretCode!!)

                thread {
                    val (_, verifyResponse, verifyResult) = Fuel.post("http://192.168.0.24:8080/verify_code")
                        .body(verifyReq)
                        .header(cookieHeader)
                        .response()

                    val verifyRespStr = String(verifyResponse.data)
                    val mapper = jacksonObjectMapper()
                    val verifyRespJson = mapper.readValue<TokenResp>(verifyRespStr)

                    Crypto.savePassword(this, verifyRespJson.token);
                    finish()
                }
            }
        }
    }
}