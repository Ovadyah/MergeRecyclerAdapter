// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergerecycleradapter.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.mergeadapter.BindableViewHolder
import com.example.mergerecycleradapter.SectionAdapter
import com.example.mergerecycleradapter.model.ContentCard
import kotlinx.android.synthetic.main.widget_content.view.*

/**
 *
 */
class ContentCardViewHolder(itemView: View) : BindableViewHolder<ContentCard>(itemView),
        View.OnClickListener {

    lateinit var boundContentCard: ContentCard

    override fun bind(adapter: RecyclerView.Adapter<*>, obj: ContentCard) {
        super.bind(adapter, obj)
        boundContentCard = obj
        itemView.title.text = obj.title
        itemView.timestamp.text = obj.timestamp
        itemView.color.setBackgroundColor(obj.color)
        itemView.dismiss.setOnClickListener(this)
    }

    //
    // View.OnClickListener
    //

    override fun onClick(v: View?) {
        adapter.let {
            if (it is SectionAdapter) {
                it.removeContent(obj)
            }
        }
    }
}