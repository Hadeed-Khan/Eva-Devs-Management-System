package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityViewAttendance
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R

class AdapterChooseCourseAttendance(context: Context, courseList : MutableList<Course>) : RecyclerView.Adapter<AdapterChooseCourseAttendance.ViewHolder>() {

    var context : Context
    var courseList : MutableList<Course>
    init {
        this.context = context
        this.courseList = courseList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_choose_course,parent,false))
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pos = holder.bindingAdapterPosition
        holder.id.text = courseList[pos].courseId
        holder.name.text = courseList[pos].courseName
        holder.duration.text = courseList[pos].courseDuration

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,ActivityViewAttendance::class.java)
                .putExtra("Course",courseList[pos]))
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var id = itemView.findViewById<TextView>(R.id.courseIdTV)
        var name = itemView.findViewById<TextView>(R.id.courseNameTV)
        var duration = itemView.findViewById<TextView>(R.id.courseDurationTV)
    }
}