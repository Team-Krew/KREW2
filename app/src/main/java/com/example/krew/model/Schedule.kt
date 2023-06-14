package com.example.krew.model

data class Schedule(
    val schedule_id: String,
    var calendar_list: ArrayList<Calendar>?,
    var title: String,
    var date: String,
    var time: String,
    var place: String,
): java.io.Serializable{
    constructor():this(
        "no_info",
        null,
        "no_info",
        "no_info",
        "no_info",
        "no_info",
    )
}
