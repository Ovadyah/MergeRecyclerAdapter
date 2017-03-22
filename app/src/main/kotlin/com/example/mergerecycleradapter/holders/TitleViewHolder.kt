// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergerecycleradapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.mergeadapter.BindableViewHolder
import com.example.mergerecycleradapter.model.SectionTitle
import kotlinx.android.synthetic.main.widget_title.view.*

/**
 *
 */
class TitleViewHolder(itemView: View) : BindableViewHolder<SectionTitle>(itemView) {
    override fun bind(adapter: RecyclerView.Adapter<*>, obj: SectionTitle) {
        super.bind(adapter, obj)
        itemView.title.text = obj.title
    }
}