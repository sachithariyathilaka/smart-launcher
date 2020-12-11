package com.sachith.applauncher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sachith.applauncher.R
import com.sachith.applauncher.model.Apps
import com.sachith.applauncher.service.ClickListenerService

data class GridViewAdapter(private var list: List<Apps>, private var context: Context, val listener: ClickListenerService) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.app_item,null)
        val icon = view.findViewById<ImageView>(R.id.appIcon)
        val name = view.findViewById<TextView>(R.id.appName)
        name.text = list[position].name
        Glide.with(context).load(list[position].icon).into(icon)
        view.setOnClickListener{
            listener.openApp(list[position])
        }
        return view
    }
}

