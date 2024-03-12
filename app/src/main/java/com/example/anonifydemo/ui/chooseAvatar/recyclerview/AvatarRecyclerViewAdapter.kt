package com.example.anonifydemo.ui.chooseAvatar.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ItemAvatarBinding
import com.example.anonifydemo.ui.chooseAvatar.ChooseAvatarFragmentDirections
import com.google.firebase.storage.FirebaseStorage


class AvatarRecyclerViewAdapter(private val context: Context, private val imageIds: List<Pair<Int, String>>) : RecyclerView.Adapter<AvatarRecyclerViewAdapter.ViewHolder>() {

    private val storage = FirebaseStorage.getInstance()

    inner class ViewHolder(val binding: ItemAvatarBinding) : RecyclerView.ViewHolder(binding.root) {

        val imgAvatar : ImageView = binding.imgAvatar
        val txtAvatar : TextView = binding.avatarName
        fun onBind(context: Context, avatar: Pair<Int, String>){

//            val storageRef = storage.getReferenceFromUrl(url)
//            val oneMegabyte = (1024 * 1024).toLong()
//
//            storageRef.getBytes(oneMegabyte)
//                .addOnSuccessListener { bytes ->
//                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                    imgAvatar.setImageBitmap(bitmap)
//                }
//                .addOnFailureListener { exception ->
//                    // Handle any errors
//                }
            val drawable = ContextCompat.getDrawable(context, avatar.first)
            imgAvatar.setImageDrawable(drawable)
//            Glide.with(context)
//                .asBitmap()
//                .load(url)
//                .into(imgAvatar)

            txtAvatar.text = avatar.second


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return imageIds.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageUrl = imageIds[position]
        holder.onBind(holder.itemView.context, imageUrl)

        holder.imgAvatar.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Welcome ${imageUrl.second}", Toast.LENGTH_LONG).show()
            if (holder.itemView.findNavController().currentDestination!!.id == R.id.chooseAvatarFragment){
                val action = ChooseAvatarFragmentDirections.actionChooseAvatarFragmentToChooseTopic()
                holder.itemView.findNavController().navigate(action)
            }

        }
    }
}