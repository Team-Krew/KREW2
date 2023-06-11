package com.example.krew.model

data class Time(
    var schedule: Schedule?,
    var user: User?,
    var time: String,
): java.io.Serializable{
    constructor():this(
        null,
        null,
        "no_info",
    )
}
