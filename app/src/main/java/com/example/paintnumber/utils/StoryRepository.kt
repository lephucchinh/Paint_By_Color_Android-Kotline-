package com.example.paintnumber.utils

import android.content.Context
import com.example.paintnumber.data.models.Artist
import com.example.paintnumber.data.models.Artwork
import org.json.JSONArray
import org.json.JSONObject

/**
 * Repository đọc dữ liệu Artist/Artwork từ res/raw (JSON)
 */
class StoryRepository(private val context: Context) {
    private var cachedArtists: List<Artist>? = null
    private var cachedArtworks: List<Artwork>? = null

    fun getArtists(): List<Artist> {
        if (cachedArtists == null) {
            cachedArtists = readArtistsFromRaw()
        }
        return cachedArtists!!
    }

    fun getArtworks(): List<Artwork> {
        if (cachedArtworks == null) {
            cachedArtworks = readArtworksFromRaw()
        }
        return cachedArtworks!!
    }

    fun getArtworksByArtist(artistId: String): List<Artwork> =
        getArtworks().filter { it.artistId == artistId }

    fun getArtistById(artistId: String): Artist? =
        getArtists().find { it.id == artistId }

    private fun readArtistsFromRaw(): List<Artist> {
        val resId = context.resources.getIdentifier("artists", "raw", context.packageName)
        if (resId == 0) return emptyList()
        val jsonString = context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
        val array = JSONArray(jsonString)
        val result = mutableListOf<Artist>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                Artist(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    avatarResName = obj.optString("avatarResName", ""),
                    bio = obj.optString("bio", "")
                )
            )
        }
        return result
    }

    private fun readArtworksFromRaw(): List<Artwork> {
        val resId = context.resources.getIdentifier("artworks", "raw", context.packageName)
        if (resId == 0) return emptyList()
        val jsonString = context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
        val array = JSONArray(jsonString)
        val result = mutableListOf<Artwork>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(obj.toArtwork())
        }
        return result
    }
}

/**
 * Trợ giúp nhanh để lấy tên/ảnh tác giả theo id từ JSON đã cache,
 * dùng tại adapter mà không truyền repository quanh app.
 */
object StoryProvider {
    private var repo: StoryRepository? = null
    private fun getRepo(ctx: Context): StoryRepository {
        if (repo == null) repo = StoryRepository(ctx.applicationContext)
        return repo!!
    }
    fun getArtistName(ctx: Context, artistId: String): String {
        return getRepo(ctx).getArtistById(artistId)?.name ?: ""
    }
    fun getArtistAvatarResId(ctx: Context, artistId: String): Int {
        val avatarName = getRepo(ctx).getArtistById(artistId)?.avatarResName ?: return 0
        return if (avatarName.isNotEmpty()) ctx.resources.getIdentifier(avatarName, "drawable", ctx.packageName) else 0
    }
}

private fun JSONObject.toArtwork(): Artwork = Artwork(
    id = getString("id"),
    title = getString("title"),
    artistId = getString("artistId"),
    year = optString("year", null),
    category = optString("category", ""),
    story = optString("story", ""),
    previewResName = optString("previewResName", ""),
    lineArtResName = optString("lineArtResName", ""),
    svgResName = optString("svgResName", ""),
    isNew = optBoolean("isNew", false),
    isSpecial = optBoolean("isSpecial", false),
    hasVideo = optBoolean("hasVideo", false)
)

