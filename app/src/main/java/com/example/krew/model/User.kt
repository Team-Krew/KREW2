package com.example.krew.model

data class User(
    var user_id: String,
    var name: String,
    var address: String,
    var comment: String,
    var time: String,
): java.io.Serializable{
    constructor():this(
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
    )
}
