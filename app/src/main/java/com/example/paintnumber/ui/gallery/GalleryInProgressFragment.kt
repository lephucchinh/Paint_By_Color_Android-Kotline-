package com.example.paintnumber.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paintnumber.databinding.FragmentGalleryListBinding
import com.example.paintnumber.ui.library.ImageAdapter
import android.util.Log

class GalleryInProgressFragment : Fragment() {
    private var _binding: FragmentGalleryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels({ requireParentFragment() })
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter { paintImage ->
            val fullId = paintImage.id // e.g., "image_3"
            val numberPart = fullId.substringAfter("image_") // "3"
            val context = requireContext()

            val outlineName = "image_${numberPart}_line_art"
            val svgName = "image_${numberPart}"

            var outlineResId = context.resources.getIdentifier(
                outlineName,
                "raw",
                context.packageName
            )
            var svgResId = context.resources.getIdentifier(
                svgName,
                "raw",
                context.packageName
            )

            // Extra fallback: try resolving svg by fullId as-is
            if (svgResId == 0) {
                svgResId = context.resources.getIdentifier(fullId, "raw", context.packageName)
            }
            // Fallback for outline from model
            if (outlineResId == 0 && paintImage.outlineResId != 0) outlineResId = paintImage.outlineResId

            Log.d(
                "GalleryInProgressFragment",
                "Resolve resources id=$fullId outlineName=$outlineName -> $outlineResId, svgName=$svgName -> $svgResId"
            )

            if (outlineResId != 0 && svgResId != 0) {
                findNavController().navigate(
                    GalleryFragmentDirections.actionGalleryToSketchLoading(
                        imageId = fullId,
                        lineArtResId = outlineResId,
                        svgResId = svgResId,
                        progressPath = paintImage.progressPath
                    )
                )
            } else {
                Toast.makeText(context, "Không thể tải hình ảnh", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = imageAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.inProgressImages.observe(viewLifecycleOwner) { images ->
            imageAdapter.submitList(images)
            binding.emptyText.visibility = if (images.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 