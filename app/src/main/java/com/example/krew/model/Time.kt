package com.example.krew.model

data class Time(
    var schedule: Schedule?,
    var user: User?,
    var time: String,
){
    constructor():this(
        null,
        null,
        "no_info",
    )
}
