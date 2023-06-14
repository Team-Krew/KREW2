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
import com.example.krew.R
import com.example.krew.databinding.DayInfoRowBinding
import com.example.krew.model.DayInfo
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AdapterDayInfo (val items:ArrayList<DayInfo>)
    :RecyclerView.Adapter<AdapterDayInfo.ViewHolder>()
{

    inner class ViewHolder(val binding: DayInfoRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DayInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = holder.binding.root.context.getDrawable(R.drawable.bg_radius_100dp)
        //val drawable = context
        if (drawable != null) {
            drawable.setColorFilter(BlendModeColorFilter(items[position].color, BlendMode.SRC_ATOP))
        }

        holder.binding.planName.setText(items[position].location)
        holder.binding.planTime.setText(items[position].time)
        holder.binding.plan.setBackground(drawable)

        holder.binding.searchBtn.setOnClickListener {
//            val destinationLatitude = 37.7749
//            val destinationLongitude = -122.4194
//
//            val intentUri = "google.navigation:q=$destinationLatitude,$destinationLongitude"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri))
//            intent.setPackage("com.google.android.apps.maps")
//            if (intent.resolveActivity(holder.binding.root.context.packageManager) != null) {
//                holder.binding.root.context.startActivity(intent)
//            } else {
//                // 구글 맵 앱이 설치되어 있지 않을 때 대체할 동작을 구현할 수 있습니다.
//            }
            //구글맵으로 대중교통 경로탐색밖에 안돼서 travelmode따로 설정함(공간정보관리법)
            //나중엔 의명유치원과 건국대학교 공과대학에 ${}로 변수 넣기
            val uri = "http://maps.google.com/maps?saddr=의명유치원&daddr=건국대학교 공과대학&travelmode=transit"
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