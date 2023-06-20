package com.example.krew.model

import java.io.Serializable

data class Invitation(
    val calendar_id: String,
    val calendar_name: String,
    val admin: String,
    var target: String
): Serializable {
    constructor(): this(
        "no_info",
        "no_info",
        "no_info",
        "no_info"
    )
}
