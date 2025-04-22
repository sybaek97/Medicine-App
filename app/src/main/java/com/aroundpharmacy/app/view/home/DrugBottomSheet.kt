package com.aroundpharmacy.app.view.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.ImageView
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.model.MdcinGrnIdntfcInfoResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.ViewUtils.dpToPx

class DrugBottomSheet(
    private val response: MdcinGrnIdntfcInfoResponse
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_drug, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val heightPx = convertDpToPx(500)
                it.layoutParams.height = heightPx
                BottomSheetBehavior.from(it).apply {
                    peekHeight = heightPx
                    isHideable = false
                    isDraggable = false
                }
            }
        }
        return dialog
    }
    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            dismiss()

        }
        val item = response.body.items?.firstOrNull()
        if (item == null) {
            view.findViewById<TextView>(R.id.txt_drug_empty).visibility = View.VISIBLE
            view.findViewById<ScrollView>(R.id.scroll_info).visibility = View.GONE
            return
        }

        // 공통 뷰 참조
        fun id(id: Int) = view.findViewById<TextView>(id)
        id(R.id.item_name).text           = item.itemName
        id(R.id.entp_name).text           = item.entpName
        id(R.id.chart).text               = item.chart
        id(R.id.drug_shape).text          = item.drugShape
        id(R.id.print_front).text         = item.printFront.orEmpty()
        id(R.id.print_back).text          = item.printBack.orEmpty()
        id(R.id.color_class1).text        = item.colorClass1
        id(R.id.color_class2).text        = item.colorClass2.orEmpty()
        id(R.id.line_front).text          = item.lineFront.orEmpty()
        id(R.id.line_back).text           = item.lineBack.orEmpty()
        id(R.id.leng_long).text           = item.lengthLong
        id(R.id.leng_short).text          = item.lengthShort
        id(R.id.thick).text               = item.thickness
        id(R.id.img_regist_ts).text       = item.imgRegistTs
        id(R.id.class_no).text            = item.classNo
        id(R.id.class_name).text          = item.className
        id(R.id.etc_otc_name).text        = item.etcOtcName
        id(R.id.item_permit_date).text    = item.itemPermitDate
        id(R.id.form_code_name).text      = item.formCodeName
        id(R.id.mark_code_front_anal).text= item.markCodeFrontAnal.orEmpty()
        id(R.id.mark_code_back_anal).text = item.markCodeBackAnal.orEmpty()
        id(R.id.mark_code_front_img).text = item.markCodeFrontImg.orEmpty()
        id(R.id.mark_code_back_img).text  = item.markCodeBackImg.orEmpty()
        id(R.id.item_eng_name).text       = item.itemEngName.orEmpty()
        id(R.id.change_date).text         = item.changeDate
        id(R.id.mark_code_front).text     = item.markCodeFront.orEmpty()
        id(R.id.mark_code_back).text      = item.markCodeBack.orEmpty()
        id(R.id.edi_code).text            = item.ediCode.orEmpty()
        id(R.id.bizrno).text              = item.bizrno

        // 이미지 로딩
        val imgView = view.findViewById<ImageView>(R.id.img_drug)
        Glide.with(this)
            .load(item.itemImage)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgView)
    }
}
