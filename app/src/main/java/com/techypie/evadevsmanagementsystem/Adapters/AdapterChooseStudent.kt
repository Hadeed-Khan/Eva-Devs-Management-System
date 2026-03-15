package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityAddEnrollment
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R

class AdapterChooseStudent(context: Context, studentList : MutableList<Student>) : RecyclerView.Adapter<AdapterChooseStudent.ViewHolder>() {

    var context : Context
    var studentList : MutableList<Student>
    var studentInterface : StudentInterface

    init {
        this.context = context
        this.studentList = studentList
        studentInterface = context as ActivityAddEnrollment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterChooseStudent.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_choose_student, parent, false))
    }

    override fun onBindViewHolder(holder: AdapterChooseStudent.ViewHolder, position: Int) {
        holder.id.text = studentList[position].stdId
        holder.name.text = studentList[position].stdName
        holder.fName.text = studentList[position].stdFatherName

        holder.itemView.setOnClickListener {
            var pos = holder.bindingAdapterPosition
            studentInterface.onSelectedStudent(studentList[pos])
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    interface StudentInterface{
        fun onSelectedStudent(student: Student)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var id = itemView.findViewById<TextView>(R.id.idTV)
        var name = itemView.findViewById<TextView>(R.id.nameTV)
        var fName = itemView.findViewById<TextView>(R.id.fNameTV)

    }
}