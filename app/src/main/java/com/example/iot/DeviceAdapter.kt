package com.example.iot
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iot.DeviceModel
import com.example.iot.R


class DeviceAdapter (private val deviceList: MutableList<DeviceModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.header_layout, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_devices, parent, false)
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
                    val currentItem = deviceList.getOrNull(position - 1) // Adjust position for header
                    currentItem?.let {
                        "Title for ${it.makeModel}" // Set the title based on the CameraModel location
                    } ?: "Default Title" // Handle if currentItem is null
                }
                headerHolder.titleTextView.text = title
            }
            VIEW_TYPE_ITEM -> {
                val deviceHolder = holder as CameraViewHolder
                val currentItem = deviceList[position - 1] // Adjust position for header
                deviceHolder.textId.text = currentItem.id.toString() // Display ID
                deviceHolder.textMakeModel.text = currentItem.makeModel
                deviceHolder.textOwmer.text = currentItem.Owner
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
        return deviceList.size + 1 // Add 1 for the header
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.headerTextView)
    }

    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.textID1) // TextView for ID
        val textMakeModel: TextView = itemView.findViewById(R.id.textMakeModel)
        val textOwmer: TextView = itemView.findViewById(R.id.textOwmer)
    }

    fun updateData(newList: List<DeviceModel>) {
        deviceList.clear() // Clear the existing list
        deviceList.addAll(newList) // Add all elements from the new list
        notifyDataSetChanged() // Notify adapter about the dataset change
    }
}