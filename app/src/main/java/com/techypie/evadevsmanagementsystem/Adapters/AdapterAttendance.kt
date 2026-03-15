package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityDeleteAttendance
import com.techypie.evadevsmanagementsystem.Models.Attendance
import com.techypie.evadevsmanagementsystem.R

class AdapterAttendance(context: Context , attendanceList: MutableList<Attendance>) : RecyclerView.Adapter<AdapterAttendance.ViewHolder>() {

    var context : Context
    var attendanceList : MutableList<Attendance>
    init {
        this.context = context
        this.attendanceList = attendanceList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_attendance_item,parent,false))
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.stdId.text = attendanceList[position].stdId
        holder.stdname.text = attendanceList[position].stdName
        holder.stdFName.text = attendanceList[position].stdFatherName
        holder.date.text = attendanceList[position].date
        var status = attendanceList[position].status
        if (status == "Present")
        {
            holder.status.text = status
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.bg_present)
        }
        else if (status == "Absent")
        {
            holder.status.text = status
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.bg_absent)
        }
        else{
            holder.status.text = status
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.bg_leave)
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,ActivityDeleteAttendance::class.java)
                .putExtra("Attendance",attendanceList[position]))
        }
    }


    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        var stdId = itemView.findViewById<TextView>(R.id.stuentIdTV2)
        var stdname = itemView.findViewById<TextView>(R.id.studentNameTV)
        var stdFName = itemView.findViewById<TextView>(R.id.studentFNameTV)
        var date = itemView.findViewById<TextView>(R.id.dateTV)
        var status = itemView.findViewById<TextView>(R.id.statusTV)
    }

}