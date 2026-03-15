package com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityReport
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R

class AdapterCourseCourseReport(context : Context, courseList : MutableList<Course>): RecyclerView.Adapter<AdapterCourseCourseReport.ViewHolder>() {

    var context : Context
    var courseList = mutableListOf<Course>()
    var courseInterfaceCReport : CourseInterfaceCReport

    init {
        this.context = context
        this.courseList = courseList
        courseInterfaceCReport = context as ActivityReport
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_choose_course,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = courseList[position].courseId
        holder.name.text = courseList[position].courseName
        holder.duration.text = courseList[position].courseDuration

        holder.itemView.setOnClickListener {
            courseInterfaceCReport.onSelectedCourseCReport(courseList[position])
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    interface CourseInterfaceCReport{
        fun onSelectedCourseCReport(course: Course)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var id = itemView.findViewById<TextView>(R.id.courseIdTV)
        var name = itemView.findViewById<TextView>(R.id.courseNameTV)
        var duration = itemView.findViewById<TextView>(R.id.courseDurationTV)
    }

}