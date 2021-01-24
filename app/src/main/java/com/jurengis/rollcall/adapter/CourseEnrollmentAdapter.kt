package com.jurengis.rollcall.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jurengis.rollcall.R
import com.jurengis.rollcall.model.Enrollment
import kotlinx.android.synthetic.main.item_enrollment.view.*
import java.lang.ref.WeakReference

class CourseEnrollmentAdapter :
    RecyclerView.Adapter<CourseEnrollmentAdapter.CourseEnrollmentViewHolder>() {

    inner class CourseEnrollmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Enrollment>() {
        override fun areItemsTheSame(
            oldItem: Enrollment,
            newItem: Enrollment
        ): Boolean {
            return oldItem.week == newItem.week
        }

        override fun areContentsTheSame(
            oldItem: Enrollment,
            newItem: Enrollment
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Enrollment) -> Unit)? = null

    fun setOnItemClickListener(listener: (Enrollment) -> Unit) {
        onItemClickListener = listener
    }

    lateinit var applicationContext: WeakReference<Context>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseEnrollmentAdapter.CourseEnrollmentViewHolder {
        applicationContext = WeakReference(parent.context)
        return CourseEnrollmentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_enrollment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CourseEnrollmentAdapter.CourseEnrollmentViewHolder,
        position: Int
    ) {
        val courseEnrollment = differ.currentList[position]
        val status = if (courseEnrollment.status) R.string.enrollment_status_present else R.string.enrollment_status_absent
        holder.itemView.apply {
            tvWeek.text = courseEnrollment.week
            tvEnrollmentStatus.text = applicationContext.get()?.getString(status)
            setOnClickListener {
                onItemClickListener?.let { it(courseEnrollment) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}