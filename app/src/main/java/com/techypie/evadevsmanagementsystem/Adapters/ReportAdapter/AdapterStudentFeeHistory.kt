package com.techypie.evadevsmanagementsystem.Adapters.ReportAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.R

class AdapterStudentFeeHistory(context: Context , feeList : MutableList<Fee>) : RecyclerView.Adapter<AdapterStudentFeeHistory.ViewHolder>() {

    var context : Context
    var feeList = mutableListOf<Fee>()

    init {
        this.context = context
        this.feeList = feeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_fee_item,parent,false))
    }

    override fun getItemCount(): Int {
        return feeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pos = holder.bindingAdapterPosition
        holder.stdName.text = feeList[position].stdName
        holder.courseName.text = feeList[position].courseName

        if (feeList[position].feeType == "Admission Fee")
        {
            holder.feeType.text = feeList[position].feeType
        }
        else {
            var parts = feeList[position].feeType.split("//").toMutableList()
            var month = parts[0].toString().toInt()
            var year = parts[1].toString()
            var type = parts[2].toString()
            var monthName = ""
            when(month){
                1-> monthName = "Jan"
                2-> monthName = "Feb"
                3-> monthName = "Mar"
                4-> monthName = "Apr"
                5-> monthName = "May"
                6-> monthName = "Jun"
                7-> monthName = "Jul"
                8-> monthName = "Aug"
                9-> monthName = "Sep"
                10-> monthName = "Oct"
                11-> monthName = "Nov"
                12-> monthName = "Dec"
            }
            holder.feeType.text = "$monthName/$year $type"
        }

        holder.totalFee.text = feeList[pos].totalFee
        holder.paidFee.text = feeList[pos].paidFee
        var remainingFee = feeList[pos].totalFee.toInt() - feeList[pos].paidFee.toInt()
        holder.remainingFee.text = "$remainingFee"
        holder.paymentDate.text = feeList[pos].paymentData
        var percentage = ((feeList[pos].paidFee.toInt() * 100) / feeList[pos].totalFee.toInt())
        holder.progressPercentage.text = "${percentage.toString()}%"
        when(percentage) {
            in 1..30 -> holder.progressBar.progressDrawable =
                context.resources.getDrawable(R.drawable.progress_bg_red, null)

            in 31..60 -> holder.progressBar.progressDrawable =
                context.resources.getDrawable(R.drawable.progress_bg_yellow, null)
            else ->{
                holder.progressBar.progressDrawable =
                    context.resources.getDrawable(R.drawable.progress_bg_green, null)
            }
        }
        holder.progressBar.setProgress(percentage)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)  {
        var stdName = itemView.findViewById<TextView>(R.id.stdNameTV)
        var courseName = itemView.findViewById<TextView>(R.id.courseNameTV)
        var feeType = itemView.findViewById<TextView>(R.id.feeTypeTV)
        var totalFee = itemView.findViewById<TextView>(R.id.totalAmountTV)
        var paidFee = itemView.findViewById<TextView>(R.id.paidAmountTV)
        var remainingFee = itemView.findViewById<TextView>(R.id.remainingAmountTV)
        var paymentDate = itemView.findViewById<TextView>(R.id.paymentDateTV)
        var progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
        var progressPercentage = itemView.findViewById<TextView>(R.id.progressPercentangeTV)
    }
}