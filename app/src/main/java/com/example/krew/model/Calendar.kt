package com.example.krew.model

data class Calendar(
    val calendar_id: String,
    var name: String,
    var comment: String,
    var label: Int,
    var admin: String?,
    var Participant: ArrayList<String>?,

){
    constructor(): this(
        "no_info",
        "no_info",
        "no_info",
        0,
        null,
        null,
        )
}
