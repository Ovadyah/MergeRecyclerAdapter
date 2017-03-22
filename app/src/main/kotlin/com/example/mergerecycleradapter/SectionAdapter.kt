// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergerecycleradapter

import android.support.v7.widget.RecyclerView.Adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mergeadapter.BindableViewHolder
import com.example.mergeadapter.SpanSizeProvider
import com.example.mergerecycleradapter.holders.ContentCardViewHolder
import com.example.mergerecycleradapter.holders.TitleViewHolder
import com.example.mergerecycleradapter.model.ContentCard
import com.example.mergerecycleradapter.model.SectionTitle

/**
 *
 */
class SectionAdapter(val title: SectionTitle,
        val onClick: (ContentCard) -> Unit
): Adapter<BindableViewHolder<*>>(), SpanSizeProvider {

    companion object {
        private const val TAG = "SectionAdapter"
    }

    private val contentList = ArrayList<ContentCard>()

    private var spanCount = 1

    fun addContent(content: ContentCard) {
        contentList.add(content)
        notifyItemInserted(contentList.size)
    }

    fun removeContent(content: ContentCard) {
        Log.d(TAG, "Remove $content")
        contentList.forEachIndexed { index, contentCard ->
            Log.d(TAG, "    comparing: ${content.id} with ${contentCard.id}")
            if (content == contentCard) {
                contentList.removeAt(index)
                notifyItemRemoved(index+1)
                return
            }
        }
        Log.d(TAG, "Remove failed")
    }

    //
    // RecyclerAdapter.Adapter
    //

    override fun onBindViewHolder(holder: BindableViewHolder<*>, position: Int) {
        if (position == 0) {
            (holder as TitleViewHolder).bind(this as Adapter<*>, title)
        } else {
            (holder as ContentCardViewHolder).bind(this as Adapter<*>,
                    contentList[position-1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<*>? {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.widget_title -> TitleViewHolder(view)
            R.layout.widget_content -> ContentCardViewHolder(view)
            else -> null
        }
    }

    override fun getItemCount(): Int = 1 + contentList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.widget_title else R.layout.widget_content
    }

    //
    // SpanSizeProvider
    //

    override fun onSpanCountChanged(numSpans: Int) {
        spanCount = numSpans
        notifyDataSetChanged()
    }

    override fun getSpanSize(position: Int): Int = when (position) {
        0 -> spanCount
        1,2 -> if (contentList.size == 1) spanCount else Math.max(1, spanCount / 2)
        else -> 1
    }

    override fun invalidateSpanIndexCache() {}
}