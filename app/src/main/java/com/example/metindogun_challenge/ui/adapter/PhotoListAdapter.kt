package com.example.metindogun_challenge.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.metindogun_challenge.databinding.PhotoListItemBinding
import com.example.metindogun_challenge.data.model.Photo
import com.example.metindogun_challenge.utils.getImagePath

class PhotoListAdapter: ListAdapter<Photo, PhotoListAdapter.ViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(var binding: PhotoListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(photo: Photo){

            Glide.with(binding.imageView)
                .load(photo.getImagePath())
                .centerCrop()
                .into(binding.imageView)
        }
    }
}

private class PhotoDiffCallback: DiffUtil.ItemCallback<Photo>(){

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}