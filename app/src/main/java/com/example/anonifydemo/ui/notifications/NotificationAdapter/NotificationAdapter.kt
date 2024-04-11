package com.example.anonifydemo.ui.notifications.NotificationAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.databinding.FragmentNotificationsBinding
import com.example.anonifydemo.databinding.RowNotificationBinding
import com.example.anonifydemo.ui.dataClasses.Notification
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.tasks.await

class NotificationAdapter (private val mContext: Context, private val mNotifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: RowNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val txtusrnm = binding.txtusrnm
        private val txtnotifi = binding.txtnotifi
        private val txttime = binding.txttime
        private val img_usr = binding.imgUsr

        fun bind(notification: Notification) {
            txtusrnm.text = notification.userid // Replace with actual username
            txtnotifi.text = notification.text
         //   img_usr.setImageDrawable(ContextCompat.getDrawable(context, post.avatarUrl))
            txttime.text = ".2d" // Replace with actual time

            // Set click listener if needed
            binding.root.setOnClickListener {
                // Handle item click if needed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowNotificationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mNotifications[position])
    }

    override fun getItemCount(): Int {
        return mNotifications.size
    }
}