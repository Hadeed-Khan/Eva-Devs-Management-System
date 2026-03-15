package com.techypie.evadevsmanagementsystem.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityMarkAttendance
import com.techypie.evadevsmanagementsystem.Models.Attendance
import com.techypie.evadevsmanagementsystem.Models.Student
import com.techypie.evadevsmanagementsystem.R

class AdapterMarkAttendance(context: Context, enrollStudentList: MutableList<Student>,courseId : String): RecyclerView.Adapter<AdapterMarkAttendance.ViewHolder>() {

    var context : Context
    var enrollStudentList : MutableList<Student>
    var attendanceList : MutableList<Attendance>


    var courseId = courseId
    init {
        this.context = context
        this.enrollStudentList = enrollStudentList
        this.courseId = courseId

        attendanceList = ArrayList()
        for(student in enrollStudentList){
            var singleAttendance = Attendance("","", "Absent",student.stdId,student.stdName, student.stdFatherName,courseId,"Active")
            attendanceList.add(singleAttendance)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_mark_attendance,parent,false))
    }

    override fun getItemCount(): Int {
        return enrollStudentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pos = holder.bindingAdapterPosition
       holder.id.text = "${enrollStudentList[pos].stdId} :"
       holder.name.text = enrollStudentList[pos].stdName
       holder.fName.text = enrollStudentList[pos].stdFatherName

        var status = ""
        holder.radioGroup.setOnCheckedChangeListener(object : OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {

                if (holder.presentButton.isChecked){
                    status = holder.presentButton.text.toString()
                }
                else if(holder.absentButton.isChecked){
                    status = holder.absentButton.text.toString()
                }
                else{
                    status = holder.leaveButton.text.toString()
                }

                attendanceList[pos].status = status

            }
        })
    }

    fun getAllAttendance() : MutableList<Attendance>
    {
        return attendanceList
    }



    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var id = itemView.findViewById<TextView>(R.id.idTV)
        var name = itemView.findViewById<TextView>(R.id.studentNameTV)
        var fName = itemView.findViewById<TextView>(R.id.stdFNameTV)
        var radioGroup = itemView.findViewById<RadioGroup>(R.id.radioGroup)
        var presentButton = itemView.findViewById<RadioButton>(R.id.presentButton)
        var absentButton = itemView.findViewById<RadioButton>(R.id.absentButton)
        var leaveButton = itemView.findViewById<RadioButton>(R.id.leaveButton)
    }
}