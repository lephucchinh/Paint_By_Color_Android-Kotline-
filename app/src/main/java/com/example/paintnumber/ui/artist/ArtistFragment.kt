package com.example.paintnumber.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paintnumber.databinding.FragmentArtistBinding
import com.example.paintnumber.utils.StoryRepository
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.paintnumber.R
import androidx.fragment.app.viewModels

class ArtistFragment : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: StoryRepository
    private lateinit var authorsAdapter: ArtistAdapter
    private lateinit var artworksAdapter: ArtworkAdapter
    private val viewModel: ArtistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        repository = StoryRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authorsAdapter = ArtistAdapter(onClick = { artist ->
            findNavController().navigate(
                R.id.action_artist_to_artistDetail,
                bundleOf("artistId" to artist.id)
            )
        })
        binding.recyclerAuthors.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerAuthors.adapter = authorsAdapter

        artworksAdapter = ArtworkAdapter(onClick = { artwork ->
            findNavController().navigate(
                R.id.storyFragment,
                bundleOf("artworkId" to artwork.id)
            )
        })
        binding.recyclerArtworks.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerArtworks.adapter = artworksAdapter

        viewModel.artists.observe(viewLifecycleOwner) { authorsAdapter.submitList(it) }
        viewModel.artworks.observe(viewLifecycleOwner) { artworksAdapter.submitList(it) }
    }
}