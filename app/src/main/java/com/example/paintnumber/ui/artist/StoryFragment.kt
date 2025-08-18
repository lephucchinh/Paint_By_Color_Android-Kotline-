package com.example.paintnumber.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.paintnumber.databinding.FragmentStoryBinding
import com.example.paintnumber.utils.StoryRepository
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.paintnumber.R

class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: StoryRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        repository = StoryRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val artworkId = requireArguments().getString("artworkId") ?: return
        val artwork = repository.getArtworks().find { it.id == artworkId } ?: return
        val artist = repository.getArtistById(artwork.artistId)

        binding.title.text = artwork.title
        binding.artist.text = artist?.name ?: ""
        binding.story.text = artwork.story
        val previewId = resources.getIdentifier(artwork.previewResName, "drawable", requireContext().packageName)
        if (previewId != 0) binding.preview.setImageResource(previewId)

        binding.buttonPaint.setOnClickListener {
            val ctx = requireContext()
            val lineId = resources.getIdentifier(artwork.lineArtResName, "raw", ctx.packageName)
            val svgId = resources.getIdentifier(artwork.svgResName, "raw", ctx.packageName)
            val imageId = artwork.id
            findNavController().navigate(
                R.id.action_story_to_sketch_loading,
                bundleOf(
                    "imageId" to imageId,
                    "lineArtResId" to lineId,
                    "svgResId" to svgId,
                    "progressPath" to null
                )
            )
        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}

