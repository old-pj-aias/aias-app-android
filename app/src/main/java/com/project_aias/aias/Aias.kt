package com.aias.aias

object Aias {
    external fun new(input: String?)
    external fun blind(input: String?): String?
    external fun setSubset(input: String?)
    external fun generateCheckParameter(): String?

    init {
        System.loadLibrary("aiascore")
    }
}