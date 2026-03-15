package com.techypie.evadevsmanagementsystem.Adapters.InactiveAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityUpdateStudent
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R
import com.techypie.evadevsmanagementsystem.databinding.ActivityUpdateStudentBinding

class AdapterInactiveStudent(context: Context, studentList : MutableList<Student>) : RecyclerView.Adapter<AdapterInactiveStudent.ViewHolder>() {

    var context = context
    var studentList = studentList
    init {
        this.context = context
        this.studentList = studentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_student_item,parent,false))
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.studentId.text = studentList[position].stdId
        holder.studentName.text = studentList[position].stdName
        holder.studentFName.text = studentList[position].stdFatherName
        holder.studentGender.text = studentList[position].stdGender
        holder.studentNumber.text = studentList[position].stdPhoneNumber

    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        var studentId = itemView.findViewById<TextView>(R.id.studentIdTV)
        var studentName = itemView.findViewById<TextView>(R.id.studentNameTV)
        var studentFName = itemView.findViewById<TextView>(R.id.studentFatherNameTV)
        var studentGender = itemView.findViewById<TextView>(R.id.studentGenderTV)
        var studentNumber = itemView.findViewById<TextView>(R.id.studentPhoneNumberTV)

    }



}