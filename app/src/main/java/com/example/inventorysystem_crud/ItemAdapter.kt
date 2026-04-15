package com.example.inventorysystem_crud

import android.content.ClipData
import android.graphics.Color
import android.media.RouteListingPreference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private var items: List<Inventory>,
    private var onItemClick: (Inventory) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var selectedOption = RecyclerView.NO_POSITION

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rootLayout  = view
        val idtextView = view.findViewById<TextView>(R.id.itemIdTextView)
        val nametextView = view.findViewById<TextView>(R.id.itemNameTextView)
        val quantitytextView = view.findViewById<TextView>(R.id.itemQuantityTextView)
        val locationtextView = view.findViewById<TextView>(R.id.itemLocationTextView)
        val statustextView = view.findViewById<TextView>(R.id.itemStatusTextView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.idtextView.text = currentItem.itemId.toString()
        holder.nametextView.text = currentItem.itemName
        holder.quantitytextView.text = currentItem.quantity.toString()
        holder.locationtextView.text = currentItem.location
        holder.statustextView.text = currentItem.status

        holder.itemView.setBackgroundColor(
            if (selectedOption == position) Color.LTGRAY else Color.TRANSPARENT)

        holder.itemView.setOnClickListener {
            selectedOption = holder.adapterPosition
            notifyDataSetChanged()
            onItemClick(currentItem)
        }
    }


    override fun getItemCount() = items.size

    fun updateData(newItems: List<Inventory>) {
        this.items = newItems
        this.selectedOption = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }
}