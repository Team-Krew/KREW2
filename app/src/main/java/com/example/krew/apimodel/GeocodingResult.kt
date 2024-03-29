package com.example.krew.apimodel

import java.io.Serializable
import java.util.*

class GeocodingResult : Serializable {
    var formatted_address: String? = null
    var postcodeLocalities: Array<String>? = null

    /** Location information for this result.  */
    var geometry: Geometry? = null
    var types: Array<AddressType>? = null
    var partialMatch = false
    var placeId: String? = null
    var plusCode: PlusCode? = null

    override fun toString(): String {
        val sb = StringBuilder("[GeocodingResult")
        if (partialMatch) {
            sb.append(" PARTIAL MATCH")
        }
        sb.append(" placeId=").append(placeId)
        sb.append(" ").append(geometry)
        sb.append(", formattedAddress=").append(formatted_address)
        sb.append(", types=").append(Arrays.toString(types))
        if (postcodeLocalities != null && postcodeLocalities!!.size > 0) {
            sb.append(", postcodeLocalities=").append(Arrays.toString(postcodeLocalities))
        }
        sb.append("]")
        return sb.toString()
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}