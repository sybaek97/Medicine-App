package com.aroundpharmacy.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.ItemPharmacyBinding
import com.aroundpharmacy.app.model.PharmacyDto

class PharmacyAdapter(private val items: List<PharmacyDto>, private val onClick: (PharmacyDto)-> Unit): RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder>(){
    inner class PharmacyViewHolder(val binding: ItemPharmacyBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(items[adapterPosition])     // 리스트 아이템 클릭
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PharmacyViewHolder(ItemPharmacyBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
        holder.binding.txtPharmacyName.text  = items[position].name
        holder.binding.txtPharmacyAddress.text  = items[position].address
    }

    override fun getItemCount() = items.size
}