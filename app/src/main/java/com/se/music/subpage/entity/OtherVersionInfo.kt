package com.se.music.subpage.entity

import com.google.gson.JsonElement
import com.se.music.retrofit.base.ConvertData
import com.se.music.singleton.GsonFactory

/**
 *Author: gaojin
 *Time: 2018/10/14 下午9:10
 */

class OtherVersionInfo : ConvertData<OtherVersionInfo?> {

    var track: List<TrackBean>? = null

    class TrackBean {
        var name: String? = null
        var artist: String? = null
        var url: String? = null
        var streamable: String? = null
        var listeners: String? = null
        var mbid: String? = null
        var image: List<ImageBean>? = null
    }

    override fun convertData(jsonElement: JsonElement): OtherVersionInfo? {
        val root = jsonElement.asJsonObject
        if (!root.has("results")) {
            return null
        }
        val trackMatch = root.get("results").asJsonObject
        if (!trackMatch.has("trackmatches")) {
            return null
        }
        return GsonFactory.instance.fromJson(trackMatch.get("trackmatches"), OtherVersionInfo::class.java)
    }
}