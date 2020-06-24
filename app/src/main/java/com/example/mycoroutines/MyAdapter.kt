package com.example.mycoroutines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * time：2020/6/15 0015
 * author：hexun
 * describe：
 */
class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private var context: Context
    private var mMsg = mutableListOf<String>()
    private var inflater: LayoutInflater

    constructor(context: Context) : super() {
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item, null, false))
    }

    override fun getItemCount(): Int {
        return mMsg.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvContent.text = mMsg[position]
    }

    fun addData(msg: String) {
        mMsg.add(msg)
        notifyItemInserted(mMsg.lastIndex)
    }

    fun removeData() {
        if (mMsg.size > 3) {
            mMsg.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvContent: TextView = itemView.findViewById(R.id.tvContent)
    }
}