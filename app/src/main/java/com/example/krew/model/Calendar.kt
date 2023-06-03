package com.example.krew.model

data class Calendar(
    val calendar_id: String,
    var name: String,
    var comment: String,
    var label: String,
    var admin: User?,
    var Participant: ArrayList<User>?,


){
    constructor(): this(
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        null,
        null,
        )
}
