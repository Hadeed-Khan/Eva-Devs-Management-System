package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityUpdateCourse
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R

class AdapterCourse(context : Context , courseList : MutableList<Course>) : RecyclerView.Adapter<AdapterCourse.ViewHolder>() {

var context = context
    var courseList = courseList
    init {
        this.context = context
        this.courseList = courseList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_course_item, parent ,false))
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.courseId.text = "#${courseList[position].courseId}"
        holder.courseName.text = courseList[position].courseName
        holder.courseDuration.text = courseList[position].courseDuration
        holder.courseFee.text = courseList[position].courseFee

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ActivityUpdateCourse::class.java)
                .putExtra("Course",courseList[position]))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseId = itemView.findViewById<TextView>(R.id.courseIdTV)
        var courseName = itemView.findViewById<TextView>(R.id.courseNameTV)
        var courseDuration = itemView.findViewById<TextView>(R.id.courseDurationTV)
        var courseFee = itemView.findViewById<TextView>(R.id.courseFeeTV)
    }
}