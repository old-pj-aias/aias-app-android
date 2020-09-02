package com.aias.aias

import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

class QRReader(bMap: Bitmap) {
    val bMap = bMap;

    fun scan(): String? {
        var contents: String? = null

        val intArray = IntArray(bMap.width * bMap.height)
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)

        val source: LuminanceSource =
            RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val reader: Reader = MultiFormatReader()
        try {
            val result = reader.decode(bitmap)
            contents = result.text
        } catch (e: Exception) {
            Log.e("QrTest", "Error decoding barcode", e)
        }
        return contents
    }
}