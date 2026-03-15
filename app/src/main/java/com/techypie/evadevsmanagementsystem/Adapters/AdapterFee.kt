package com.techypie.evadevsmanagementsystem.Adapters

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.techypie.evadevsmanagementsystem.Activities.ActivityDeleteFee
import com.techypie.evadevsmanagementsystem.Models.Fee
import com.techypie.evadevsmanagementsystem.R

class AdapterFee(context: Context , feeList : MutableList<Fee>) : RecyclerView.Adapter<AdapterFee.ViewHolder>() {

    var context : Context
    var feeList : MutableList<Fee>

    init {
        this.context = context
        this.feeList = feeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_fee_item,parent,false), context)
    }

    override fun getItemCount(): Int {
       return feeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var pos = holder.bindingAdapterPosition
        holder.stdName.text = feeList[pos].stdName
        holder.courseName.text = feeList[pos].courseName

        if (feeList[position].feeType == "Admission Fee")
        {
            holder.feeType.text = feeList[position].feeType
        }
        else {
            var parts = feeList[position].feeType.split("//").toMutableList()
            var month = parts[0].toInt()
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
        holder.progressBar.setProgress(percentage)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,ActivityDeleteFee::class.java)
                .putExtra("Fee",feeList[pos]))
        }

    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        var pos = holder.bindingAdapterPosition


        holder.animator?.cancel()

        var currentFee = feeList[pos]

        var feePaid = currentFee.paidFee.toInt()
        var feeTotal = currentFee.totalFee.toInt()

        var percentage = ((feePaid * 100) / feeTotal)

        var animator = ValueAnimator.ofInt(0,percentage)
        animator.duration= 2000
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator) {
                holder.progressBar.progress = animation.animatedValue.toString().toInt()
                holder.progressPercentage.text = animation.animatedValue.toString() + "%"

                when(animation.animatedValue.toString().toInt())
                {
                    in 0..30 -> holder.progressBar.progressDrawable = context.resources.getDrawable(R.drawable.progress_bg_red, null)
                    in 31..70 -> holder.progressBar.progressDrawable = context.resources.getDrawable(R.drawable.progress_bg_yellow, null)
                    else-> holder.progressBar.progressDrawable = context.resources.getDrawable(R.drawable.progress_bg_green, null)
                }
            }

        })
        animator.start()
        holder.animator = animator

    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.animator?.cancel()
    }

    class ViewHolder(itemView : View, context: Context): RecyclerView.ViewHolder(itemView) {
        var stdName = itemView.findViewById<TextView>(R.id.stdNameTV)
        var courseName = itemView.findViewById<TextView>(R.id.courseNameTV)
        var feeType = itemView.findViewById<TextView>(R.id.feeTypeTV)
        var totalFee = itemView.findViewById<TextView>(R.id.totalAmountTV)
        var paidFee = itemView.findViewById<TextView>(R.id.paidAmountTV)
        var remainingFee = itemView.findViewById<TextView>(R.id.remainingAmountTV)
        var paymentDate = itemView.findViewById<TextView>(R.id.paymentDateTV)
        var progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
        var progressPercentage = itemView.findViewById<TextView>(R.id.progressPercentangeTV)

        var animator : ValueAnimator? = null

    }

}