// Copyright (c) 2017 Magicleap. All right reserved.

package com.example.mergerecycleradapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.mergeadapter.MergeAdapter
import com.example.mergerecycleradapter.model.ContentCard
import com.example.mergerecycleradapter.model.SectionTitle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val rand = Random(System.currentTimeMillis())
    }

    val adapter = MergeAdapter()
    val contentAdapters: Array<SectionAdapter> = arrayOf(
            SectionAdapter(SectionTitle("Section 1"), { card ->
                Toast.makeText(this@MainActivity, card.title, Toast.LENGTH_SHORT).show()
            }),
            SectionAdapter(SectionTitle("Section 2"), { card ->
                Toast.makeText(this@MainActivity, card.title, Toast.LENGTH_SHORT).show()
            })
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 4).apply {
                spanSizeLookup = this@MainActivity.adapter.spanSizeLookup
            }
            adapter = this@MainActivity.adapter
        }
        adapter.onSpanCountChanged(4)
        add.setOnClickListener(this)
        contentAdapters.forEach {
            adapter.addAdapter(it as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        }
    }

    //
    // View.OnClickListener
    //

    override fun onClick(v: View) {
        // Add new content to the adapters
        contentAdapters[rand.nextInt(contentAdapters.size)].addContent(ContentCard())
    }
}
