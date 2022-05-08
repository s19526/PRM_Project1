package com.filipjagura.PRM.adapter

import TaskAdapter
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class touchHelper(private val adapter: TaskAdapter, private val value:TextView) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.delete(viewHolder.layoutPosition)
        val future = adapter.load()
        while (future.isAlive){}
        value.text=adapter.calculateThisWeek().toString()

    }
}