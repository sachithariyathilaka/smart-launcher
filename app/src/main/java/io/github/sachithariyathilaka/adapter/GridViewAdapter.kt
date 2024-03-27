package io.github.sachithariyathilaka.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import io.github.sachithariyathilaka.R
import io.github.sachithariyathilaka.model.Apps
import io.github.sachithariyathilaka.service.ClickListenerService

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

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.app_item,null)
        val icon = view.findViewById<ImageView>(R.id.appIcon)
        val name = view.findViewById<TextView>(R.id.appName)
        name.text = list[position].name
        Glide.with(context).load(list[position].icon).into(icon)
        if(list[position].disable){
            list[position].icon.colorFilter = BlendModeColorFilter(Color.GRAY, BlendMode.COLOR)
        }
        view.setOnClickListener{
            listener.onAppClick(list[position])
        }
        return view
    }
}

