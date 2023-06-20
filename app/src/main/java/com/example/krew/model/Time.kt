package com.example.krew.model

data class Time(
    val schedule: String,
    val schedulename: String,
    val admin: String,
    var target: String,
    var date: String,
    var time: String,
): java.io.Serializable{
    constructor():this(
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info"
    )
}
