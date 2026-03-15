package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityAddEnrollment
import com.techypie.evadevsmanagementsystem.Activities.ActivityAddFee
import com.techypie.evadevsmanagementsystem.Models.Course
import com.techypie.evadevsmanagementsystem.R

class AdapterChooseCourseForFee(context: Context, courseList : MutableList<Course>) : RecyclerView.Adapter<AdapterChooseCourseForFee.ViewHolder>() {

    var context : Context
    var courseList : MutableList<Course>
    var courseInterface : CourseInterface

    init {
        this.context = context
        this.courseList = courseList
        courseInterface = context as ActivityAddFee
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_choose_course,parent,false))
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = courseList[position].courseId
        holder.name.text = courseList[position].courseName
        holder.duration.text = courseList[position].courseDuration

        holder.itemView.setOnClickListener {
            var pos = holder.bindingAdapterPosition
            courseInterface.courseInterface(courseList[pos])
        }
    }

    interface CourseInterface{
        fun courseInterface(course: Course)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var id = itemView.findViewById<TextView>(R.id.courseIdTV)
        var name = itemView.findViewById<TextView>(R.id.courseNameTV)
        var duration = itemView.findViewById<TextView>(R.id.courseDurationTV)
    }
}