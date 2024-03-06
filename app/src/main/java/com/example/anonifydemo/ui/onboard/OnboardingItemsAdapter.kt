package com.example.anonifydemo.ui.onboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R

class OnboardingItemsAdapter(private val onboardingItems: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return onboardingItems.size

    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.bind(onboardingItems[position])

    }
    inner class OnboardingItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val imageOnboarding= view.findViewById<ImageView>(R.id.imageOnboarding)
        private val texttitle = view.findViewById<TextView>(R.id.tv_title)
        private val textDescription = view.findViewById<TextView>(R.id.tv_desc)

        fun bind(onboardingItem: OnboardingItem)
        {
            imageOnboarding.setImageResource(onboardingItem.onboardingImage)
            texttitle.text=onboardingItem.title
            textDescription.text=onboardingItem.description
        }
    }

}