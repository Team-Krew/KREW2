package com.example.krew.model

data class User(
    var user_id: String,
    var name: String,
    var address: String,
    var comment: String,
    var profile_img: String,
    var time: String,
){
    constructor():this(
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
    )
}
