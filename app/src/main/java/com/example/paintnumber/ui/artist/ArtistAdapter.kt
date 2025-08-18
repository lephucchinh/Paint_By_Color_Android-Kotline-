package com.example.paintnumber.ui.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paintnumber.data.models.Artist
import com.example.paintnumber.databinding.ItemArtistBinding
import com.example.paintnumber.R

class ArtistAdapter(
    private val onClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    private val items = mutableListOf<Artist>()

    fun submitList(newItems: List<Artist>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ArtistViewHolder(private val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Artist) {
            binding.name.text = item.name
            val pkg = binding.root.context.packageName
            var resId = binding.root.resources.getIdentifier(item.avatarResName, "drawable", pkg)
            if (resId == 0) {
                // fallback sang ảnh thật trong drawable-xxxhdpi nếu có
                resId = binding.root.resources.getIdentifier("artist_1", "drawable", pkg)
            }
            if (resId != 0) binding.avatar.setImageResource(resId) else binding.avatar.setImageResource(R.drawable.ic_artist)
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}

