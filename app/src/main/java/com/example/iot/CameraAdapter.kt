package com.example.iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CameraAdapter (private val cameraList: MutableList<CameraModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.header_layout, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_camera, parent, false)
                CameraViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                val title = if (position == 0) {
                    "Header Title" // Set a default header title
                } else {
                    val currentItem = cameraList.getOrNull(position - 1) // Adjust position for header
                    currentItem?.let {
                        "Title for ${it.location}" // Set the title based on the CameraModel location
                    } ?: "Default Title" // Handle if currentItem is null
                }
                headerHolder.titleTextView.text = title
            }
            VIEW_TYPE_ITEM -> {
                val cameraHolder = holder as CameraViewHolder
                val currentItem = cameraList[position - 1] // Adjust position for header
                cameraHolder.textId.text = currentItem.id.toString() // Display ID
                cameraHolder.textLocation.text = currentItem.location
                cameraHolder.textAngle.text = currentItem.angle
            }
        }
    }



    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return cameraList.size + 1 // Add 1 for the header
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.headerTextView)
    }

    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.textID) // TextView for ID
        val textLocation: TextView = itemView.findViewById(R.id.textLocation)
        val textAngle: TextView = itemView.findViewById(R.id.textAngle)
    }

    fun updateData(newList: List<CameraModel>) {
        cameraList.clear() // Clear the existing list
        cameraList.addAll(newList) // Add all elements from the new list
        notifyDataSetChanged() // Notify adapter about the dataset change
    }
}