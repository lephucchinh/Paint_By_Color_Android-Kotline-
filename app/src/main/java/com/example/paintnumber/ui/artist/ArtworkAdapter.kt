package com.example.paintnumber.ui.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paintnumber.data.models.Artwork
import com.example.paintnumber.databinding.ItemArtworkBinding
import com.example.paintnumber.utils.StoryProvider

class ArtworkAdapter(
    private val onClick: (Artwork) -> Unit
) : RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder>() {

    private val items = mutableListOf<Artwork>()

    fun submitList(newItems: List<Artwork>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val binding = ItemArtworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ArtworkViewHolder(private val binding: ItemArtworkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Artwork) {
            val ctx = binding.root.context
            val previewId = ctx.resources.getIdentifier(item.previewResName, "drawable", ctx.packageName)
            if (previewId != 0) binding.preview.setImageResource(previewId)
            val artistName = StoryProvider.getArtistName(ctx, item.artistId)
            binding.authorName.text = artistName
            val avatarId = StoryProvider.getArtistAvatarResId(ctx, item.artistId)
            if (avatarId != 0) binding.authorAvatar.setImageResource(avatarId)
            binding.badge.visibility = if (item.isNew || item.isSpecial) android.view.View.VISIBLE else android.view.View.GONE
            binding.badge.text = when {
                item.isNew -> "New"
                item.isSpecial -> "Special"
                else -> ""
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}

