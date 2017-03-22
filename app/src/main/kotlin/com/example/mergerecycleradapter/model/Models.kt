// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergerecycleradapter.model

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 */

class SectionTitle(val title: String)

class ContentCard {
    companion object {
        private const val TAG = "ContentCard"
        private val RAND = Random(System.currentTimeMillis())
        private var nextId = 0
    }
    val id: Int
    val title: String
    val color: Int
    val timestamp: String

    init {
        id = nextId++
        title = "Card #$id"
        color = RAND.nextInt()
        timestamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        Log.d(TAG, toString())
    }

    override fun toString(): String =
            "$TAG(id=$id,title='$title',color=$color,timestamp='$timestamp'"
}