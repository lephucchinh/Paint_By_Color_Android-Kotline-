package com.example.paintnumber.ui.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.paintnumber.data.CompletedPaintingsManager
import com.example.paintnumber.data.models.PaintImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val completedPaintingsManager = CompletedPaintingsManager(application)
    private val TAG = "GalleryViewModel"

    private val _completedImages = MutableLiveData<List<PaintImage>>()
    val completedImages: LiveData<List<PaintImage>> = _completedImages

    private val _inProgressImages = MutableLiveData<List<PaintImage>>()
    val inProgressImages: LiveData<List<PaintImage>> = _inProgressImages

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val allImages = getAllImages()
            val (completed, notCompleted) = allImages.partition { it.isCompleted }

            // Build in-progress from remaining
            val (inProgress, _) = notCompleted.partition {
                completedPaintingsManager.isInProgress(it.id)
            }

            // Remove any in-progress that are already completed (extra safety)
            val completedIds = completed.map { it.id }.toSet()
            val filteredInProgress = inProgress.filter { it.id !in completedIds }
                .map { image ->
                    image.copy(
                        progressPath = completedPaintingsManager.getProgressPath(image.id),
                        isInProgress = true
                    )
                }

            _completedImages.postValue(completed)
            _inProgressImages.postValue(filteredInProgress)
        }
    }

    private fun getAllImages(): List<PaintImage> {
        val images = mutableListOf<PaintImage>()
        
        // Lấy danh sách ảnh đã hoàn thành và đang trong tiến trình
        val completedPaintings = completedPaintingsManager.getCompletedPaintings()
        val inProgressPaintings = completedPaintingsManager.getInProgressPaintings()
        
        // Xử lý các ảnh đã hoàn thành
        completedPaintings.forEach { imagePath ->
            try {
                val fileName = imagePath.substringAfterLast("/").substringBeforeLast(".")
                val imageNumber = fileName.substringAfter("image_")
                
                val context = getApplication<Application>()
                val previewResId = context.resources.getIdentifier(
                    "image_${imageNumber}_preview",
                    "drawable",
                    context.packageName
                )
                val outlineResId = context.resources.getIdentifier(
                    "image_${imageNumber}_line_art",
                    "raw",
                    context.packageName
                )
                
                images.add(
                    PaintImage(
                        id = "image_${imageNumber}",
                        previewResId = previewResId,
                        outlineResId = outlineResId,
                        category = "Completed",
                        isCompleted = true,
                        completedImagePath = imagePath,
                        isInProgress = false
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading completed image: ${e.message}")
            }
        }
        
        // Xử lý các ảnh đang trong tiến trình (bỏ qua file không tồn tại)
        inProgressPaintings.forEach { imagePath ->
            try {
                val file = File(imagePath)
                if (!file.exists()) return@forEach

                val fileName = imagePath.substringAfterLast("/").substringBeforeLast(".")
                val imageNumber = fileName.substringBefore("_progress").substringAfter("image_")
                
                val context = getApplication<Application>()
                val previewResId = context.resources.getIdentifier(
                    "image_${imageNumber}_preview",
                    "drawable",
                    context.packageName
                )
                val outlineResId = context.resources.getIdentifier(
                    "image_${imageNumber}_line_art",
                    "raw",
                    context.packageName
                )
                
                images.add(
                    PaintImage(
                        id = "image_${imageNumber}",
                        previewResId = previewResId,
                        outlineResId = outlineResId,
                        category = "In Progress",
                        isCompleted = false,
                        progressPath = imagePath,
                        isInProgress = true
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading in-progress image: ${e.message}")
            }
        }
        
        return images
    }

    fun refreshImages() {
        loadImages()
    }
} 