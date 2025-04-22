package com.aroundpharmacy.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.model.DrugPrdtPrmsnInfoResponseDto
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class DrugAdapter : RecyclerView.Adapter<DrugAdapter.DrugViewHolder>() {

    private val items = mutableListOf<DrugPrdtPrmsnInfoResponseDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drug, parent, false)
        return DrugViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    /** 새 아이템을 뒤에 추가하고 갱신 */
    fun addItems(newItems: List<DrugPrdtPrmsnInfoResponseDto>) {
        val start = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }

    /** 검색 등으로 전체 초기화할 때 */
    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class DrugViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtDrugName     : TextView = itemView.findViewById(R.id.txt_drugName)
        private val txtStoreName     : TextView = itemView.findViewById(R.id.txt_storeName)
        private val txtCategoryName   : TextView = itemView.findViewById(R.id.txt_categoryName)
        private val txtProfessionalGeneral  : TextView = itemView.findViewById(R.id.txt_professionalGeneral)
        private val imgDrug  : ImageView = itemView.findViewById(R.id.img_drug)

        fun bind(item: DrugPrdtPrmsnInfoResponseDto) {
            txtDrugName.text = "${item.itemName}(${item.itemEngName}) "
            txtStoreName.text = item.entpName
            txtCategoryName.text = item.productType
            txtProfessionalGeneral.text = item.spcltyPblc


            val imageUrl =item.itemIngrName

            Glide.with(itemView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // 캐시 전략
                .into(imgDrug)
        }
    }
}