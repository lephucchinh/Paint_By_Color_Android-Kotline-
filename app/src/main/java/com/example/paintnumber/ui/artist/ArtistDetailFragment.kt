package com.example.paintnumber.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paintnumber.databinding.FragmentArtistDetailBinding
import com.example.paintnumber.utils.StoryRepository
import com.example.paintnumber.data.models.Artwork
import androidx.navigation.fragment.findNavController
import com.example.paintnumber.R
import androidx.core.os.bundleOf

class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: StoryRepository
    private lateinit var adapter: ArtworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        repository = StoryRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val artistId = requireArguments().getString("artistId") ?: return

        val artist = repository.getArtistById(artistId)
        binding.artistName.text = artist?.name ?: ""
        binding.artistBio.text = artist?.bio ?: ""

        adapter = ArtworkAdapter(onClick = { artwork ->
            findNavController().navigate(
                R.id.action_artistDetail_to_story,
                bundleOf("artworkId" to artwork.id)
            )
        })
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        val artworks = repository.getArtworksByArtist(artistId)
        adapter.submitList(artworks)
    }
}

