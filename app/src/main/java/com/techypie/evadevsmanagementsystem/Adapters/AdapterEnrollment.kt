package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityUpdateEnrollment
import com.techypie.evadevsmanagementsystem.Models.Enrollment
import com.techypie.evadevsmanagementsystem.R

class AdapterEnrollment(context: Context , enrollmentList : MutableList<Enrollment>): RecyclerView.Adapter<AdapterEnrollment.ViewHolder>() {

    var context : Context
    var enrollmentList : MutableList<Enrollment>

    init {
        this.context = context
        this.enrollmentList = enrollmentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_enrollment_item,parent,false))
    }

    override fun getItemCount(): Int {
       return enrollmentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var pos = holder.bindingAdapterPosition
        holder.enrollmentId.text = enrollmentList[pos].enrollmentId
        holder.enrollmentDate.text = enrollmentList[pos].enrollmentDate
        holder.enrollmentDate2.text = enrollmentList[pos].enrollmentDate
        holder.courseId.text = enrollmentList[pos].courseId
        holder.studentId.text = enrollmentList[pos].studentId
        holder.studentName.text = enrollmentList[pos].studentName
        holder.studentFName.text = enrollmentList[pos].studentFName
        holder.courseNameTV.text = enrollmentList[pos].courseName
        holder.courseDuration.text = enrollmentList[pos].courseDuration
        holder.courseDuration2.text = enrollmentList[pos].courseDuration

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,ActivityUpdateEnrollment::class.java).putExtra("Enrollment",enrollmentList[pos]))
        }


    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var enrollmentId = itemView.findViewById<TextView>(R.id.enrollIdTV)
        var enrollmentDate = itemView.findViewById<TextView>(R.id.enrollDateTV)
        var enrollmentDate2 = itemView.findViewById<TextView>(R.id.enrollDateTV2)
        var courseId = itemView.findViewById<TextView>(R.id.courseIdTV)
        var studentId = itemView.findViewById<TextView>(R.id.studentIdTV)
        var studentName = itemView.findViewById<TextView>(R.id.studentNameTV)
        var studentFName = itemView.findViewById<TextView>(R.id.studentFNameTV)
        var courseNameTV = itemView.findViewById<TextView>(R.id.courseNameTV)
        var courseDuration = itemView.findViewById<TextView>(R.id.courseDurationTV)
        var courseDuration2 = itemView.findViewById<TextView>(R.id.courseDurationTV2)
    }
}