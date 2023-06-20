package com.example.krew.model

data class TempUser(
    var address: String,
    var comment: String,
    var name: String,
    var time: String,
    var user_email: String,
    var user_id: String,
    var user_token: String,
): java.io.Serializable{
    constructor():this(
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info",
        "no_info"
    )
}
