package com.aroundpharmacy.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.ItemDrugBinding
import com.aroundpharmacy.app.model.DrugPrdtPrmsnInfoResponseDto
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class DrugAdapter(private val onClick: (String)-> Unit) : RecyclerView.Adapter<DrugAdapter.DrugViewHolder>() {

    private val items = mutableListOf<DrugPrdtPrmsnInfoResponseDto>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
            DrugViewHolder(ItemDrugBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
            holder.binding.apply {
                txtDrugName.text = items[position].itemName
                txtStoreName.text = items[position].entpName
                txtCategoryName.text = items[position].productType
                txtProfessionalGeneral.text = items[position].spcltyPblc


                val imageUrl =items[position].itemIngrName

                Glide.with(imgDrug)
                    .load(imageUrl)
                    .error(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgDrug)
            }
        }

        override fun getItemCount(): Int = items.size

        fun addItems(newItems: List<DrugPrdtPrmsnInfoResponseDto>) {
            val start = items.size
            items.addAll(newItems)
            notifyItemRangeInserted(start, newItems.size)
        }



        fun clearItems() {
            items.clear()
            notifyDataSetChanged()
        }

        inner class DrugViewHolder(val binding: ItemDrugBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    onClick(items[adapterPosition].itemSeq)     // 리스트 아이템 클릭
                }
            }
        }
    }