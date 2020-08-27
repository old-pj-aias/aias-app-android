package com.aias.aias

object Aias {
    external fun new(input: String?)
    external fun blind(input: String?): String?

    init {
        System.loadLibrary("aiascore")
    }
}