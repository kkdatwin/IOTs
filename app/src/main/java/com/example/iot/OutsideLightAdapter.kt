package com.example.iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OutsideLightAdapter (private val lightList: MutableList<OutsideLightsModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.header_layout, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inside_light, parent, false)
                OutsideLightsViewHolder(view)
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
                    val currentItem = lightList.getOrNull(position - 1) // Adjust position for header
                    currentItem?.let {
                        "Title for ${it.room}" // Set the title based on the CameraModel location
                    } ?: "Default Title" // Handle if currentItem is null
                }
                headerHolder.titleTextView.text = title
            }
            VIEW_TYPE_ITEM -> {
                val lightHolder = holder as OutsideLightsViewHolder
                val currentItem = lightList[position - 1] // Adjust position for header
                lightHolder.textId.text = currentItem.id.toString() // Display ID
                lightHolder.textRoom.text = currentItem.room
                lightHolder.texttype.text = currentItem.type
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
        return lightList.size + 1 // Add 1 for the header
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.headerTextView)
    }

    class OutsideLightsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.textID2) // TextView for ID
        val textRoom: TextView = itemView.findViewById(R.id.textRoom)
        val texttype: TextView = itemView.findViewById(R.id.textwtyype)
    }

    fun updateData(newList: List<OutsideLightsModel>) {
        lightList.clear() // Clear the existing list
        lightList.addAll(newList) // Add all elements from the new list
        notifyDataSetChanged() // Notify adapter about the dataset change
    }
}