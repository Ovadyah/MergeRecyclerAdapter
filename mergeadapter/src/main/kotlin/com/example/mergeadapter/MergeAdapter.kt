// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergeadapter

import android.support.annotation.IntRange
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.*
import android.view.ViewGroup


interface SpanSizeProvider {
    fun onSpanCountChanged(@IntRange(from = 1, to = Long.MAX_VALUE) numSpans: Int)
    fun getSpanSize(position: Int): Int
    fun invalidateSpanIndexCache()
}
/**
 *
 */
class MergeAdapter: Adapter<ViewHolder>(), SpanSizeProvider {

    val adapters = ArrayList<Adapter<ViewHolder>>()

    var defaultSpanSize = 1
        set(value) {
            field = value
        }

    val spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return this@MergeAdapter.getSpanSize(position)
        }
    }

    private var spanCount = 1

    fun addAdapter(adapter: Adapter<ViewHolder>) {
        adapters.add(adapter)
        adapter.registerAdapterDataObserver(MergeAdapterDataObserver(adapters.size-1))
        if (adapter is SpanSizeProvider) {
            adapter.onSpanCountChanged(spanCount)
        }
    }

    //
    // RecyclerView.Adapter
    //

    override fun getItemCount(): Int {
        var count = 0
        adapters.forEach { count += it.itemCount }
        return count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getChildAdapter(position, { adapter, position ->
            adapter.onBindViewHolder(holder, position)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        adapters.forEach {
            it.onCreateViewHolder(parent, viewType)?.let {
                return it
            }
        }
        throw IllegalArgumentException("Invalid viewType: $viewType")
    }

    override fun getItemViewType(position: Int): Int {
        return getChildAdapter(position, Adapter<*>::getItemViewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        adapters.forEach { it.onAttachedToRecyclerView(recyclerView) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        adapters.forEach { it.onDetachedFromRecyclerView(recyclerView) }
    }

    override fun getItemId(position: Int): Long {
        return getChildAdapter(position, Adapter<*>::getItemId)
    }

    //
    // SpanSizeProvider
    //

    override fun onSpanCountChanged(@IntRange(from = 1, to = Long.MAX_VALUE) numSpans: Int) {
        if (numSpans < 1) {
            throw IllegalArgumentException("Invalid number of spans: $numSpans")
        }
        invalidateSpanIndexCache()
        spanCount = numSpans
        adapters.forEach {
            if (it is SpanSizeProvider) {
                it.onSpanCountChanged(numSpans)
            }
        }
        notifyDataSetChanged()
    }

    override fun invalidateSpanIndexCache() {
        spanSizeLookup.invalidateSpanIndexCache()
        adapters.forEach {
            if (it is SpanSizeProvider) {
                it.invalidateSpanIndexCache()
            }
        }
    }

    override fun getSpanSize(position: Int): Int =
            Math.min(spanCount, getChildAdapter(position, { adapter, position ->
                if (adapter is SpanSizeProvider) {
                    adapter.getSpanSize(position)
                } else {
                    defaultSpanSize
                }
            }))

    //
    // Private helper functions
    //

    private fun <R> getChildAdapter(position: Int, block: (Adapter<ViewHolder>, Int) -> R): R {
        var offset = 0
        adapters.forEach {
            val adapterItemCount = it.itemCount
            if (position in offset until offset+adapterItemCount) {
                return block(it, position - offset)
            } else {
                offset += adapterItemCount
            }
        }
        throw IllegalArgumentException("Invalid position: $position")
    }

    private fun getAdapterOffset(index: Int): Int {
        var offset = 0
        adapters.forEachIndexed { i, adapter ->
            if (i == index) {
                return offset
            }
            offset += adapter.itemCount
        }
        throw IllegalArgumentException("Invalid adapter index: $index")
    }

    inner class MergeAdapterDataObserver(val adapterIndex: Int): AdapterDataObserver() {

        private fun getOffset() = getAdapterOffset(adapterIndex)

        override fun onChanged() {
            notifyItemRangeChanged(getOffset(), adapters[adapterIndex].itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(getOffset() + positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapterOffset = getOffset()
            val range = adapterOffset until (adapterOffset + itemCount)
            for (offset in range) {
                notifyItemMoved(fromPosition + offset, toPosition + offset)
            }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(getOffset() + positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(getOffset() + positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            notifyItemRangeChanged(getOffset() + positionStart, itemCount, payload)
        }
    }
}