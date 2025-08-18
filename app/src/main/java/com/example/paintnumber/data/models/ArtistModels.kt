package com.example.paintnumber.data.models

/**
 * Mô hình dữ liệu cho tác giả và tác phẩm (dùng cho Artist/Daily/Story)
 */
data class Artist(
    val id: String,
    val name: String,
    val avatarResName: String,
    val bio: String
)

data class Artwork(
    val id: String,
    val title: String,
    val artistId: String,
    val year: String?,
    val category: String,
    val story: String,
    val previewResName: String,
    val lineArtResName: String,
    val svgResName: String,
    val isNew: Boolean = false,
    val isSpecial: Boolean = false,
    val hasVideo: Boolean = false
)

