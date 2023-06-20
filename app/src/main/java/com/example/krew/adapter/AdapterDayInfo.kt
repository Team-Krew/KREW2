package com.example.krew.adapter

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.databinding.DayInfoRowBinding
import com.example.krew.model.DayInfo
import com.example.krew.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AdapterDayInfo (val items:ArrayList<DayInfo>)
    :RecyclerView.Adapter<AdapterDayInfo.ViewHolder>()
{

    lateinit var cur_user: User
    inner class ViewHolder(val binding: DayInfoRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DayInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        cur_user = ApplicationClass.cur_user
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = holder.binding.root.context.getDrawable(R.drawable.bg_radius_100dp)
        //val drawable = context
        if (drawable != null) {
            drawable.setColorFilter(BlendModeColorFilter(items[position].color, BlendMode.SRC_ATOP))
        }

        holder.binding.planName.setText(items[position].title)
        holder.binding.planTime.setText(items[position].time)
        holder.binding.plan.setBackground(drawable)
        holder.binding.planLoc.setText(items[position].location)

        holder.binding.searchBtn.setOnClickListener {
            val uri = "http://maps.google.com/maps?saddr=${cur_user.address}&daddr=${items[position].location}&travelmode=transit"
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            )
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}