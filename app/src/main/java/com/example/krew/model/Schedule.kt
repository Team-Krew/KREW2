package com.example.krew.model

data class Schedule(
    val schedule_id: String,
    var calendar_list: ArrayList<Calendar>?,
    var title: String,
    var content: String,
    var date: String,
    var time: String,
    var place: String,
){
    constructor():this(
        "no_info",
        null,
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
    )
}
