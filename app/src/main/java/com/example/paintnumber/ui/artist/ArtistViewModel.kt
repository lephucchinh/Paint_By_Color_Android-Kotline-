package com.example.paintnumber.ui.artist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.paintnumber.data.models.Artist
import com.example.paintnumber.data.models.Artwork
import com.example.paintnumber.utils.StoryRepository

class ArtistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StoryRepository(application.applicationContext)

    private val _artists = MutableLiveData<List<Artist>>(repository.getArtists())
    val artists: LiveData<List<Artist>> = _artists

    private val _artworks = MutableLiveData<List<Artwork>>(repository.getArtworks())
    val artworks: LiveData<List<Artwork>> = _artworks
}


