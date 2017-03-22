// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergeadapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *
 */
open class BindableViewHolder<T: Any>(itemView: View): RecyclerView.ViewHolder(itemView) {
    protected lateinit var adapter: RecyclerView.Adapter<*>
    protected lateinit var obj: T

    open fun bind(adapter: RecyclerView.Adapter<*>, obj: T) {
        this.adapter = adapter
        this.obj = obj
    }
}