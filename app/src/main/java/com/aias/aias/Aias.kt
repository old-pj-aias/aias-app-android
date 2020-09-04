package com.aias.aias

object Aias {
    external fun new(input1: String?, input2: String?, input3: String?)
    external fun ready(input1: String?, input2: String?): String?
    external fun setSubset(input: String?)
    external fun generateCheckParameter(): String?
    external fun unblind(input: String?): String?

    init {
        System.loadLibrary("aiascoreandroid")
    }
}