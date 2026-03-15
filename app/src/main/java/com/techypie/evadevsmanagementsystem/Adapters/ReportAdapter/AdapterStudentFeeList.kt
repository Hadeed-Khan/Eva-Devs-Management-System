package com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityReport
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R

class AdapterStudentFeeList(context: Context, studentList : MutableList<Student>) : RecyclerView.Adapter<AdapterStudentFeeList.ViewHolder>() {

    var context : Context
    var studentList = mutableListOf<Student>()
    var studentInterface : StudentInterface
    init {
        this.context = context
        this.studentList = studentList
        studentInterface = context as ActivityReport
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_choose_student,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = studentList[position].stdId
        holder.name.text = studentList[position].stdName
        holder.fName.text = studentList[position].stdFatherName

        holder.itemView.setOnClickListener {
            studentInterface.onSelectedStudent(studentList[position])
        }
    }

    override fun getItemCount(): Int {
       return studentList.size
    }

    interface StudentInterface{
        fun onSelectedStudent(student: Student)
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var id = itemView.findViewById<TextView>(R.id.idTV)
        var name = itemView.findViewById<TextView>(R.id.nameTV)
        var fName = itemView.findViewById<TextView>(R.id.fNameTV)
    }



}