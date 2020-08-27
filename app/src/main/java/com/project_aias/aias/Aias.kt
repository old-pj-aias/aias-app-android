package com.aias.aias

object Aias {
    external fun new(input: String?)

    init {
        System.loadLibrary("aiascore")
    }
}