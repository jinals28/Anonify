package com.example.anonifydemo.ui.onboard

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment() {

    private var _binding: FragmentOnboardBinding? = null

    private val binding get() = _binding

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var viewPager: ViewPager2
    private var isLastPage = false
    private val SPLASH_DELAY: Long = 3000
    private lateinit var getStarted: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnboardBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        indicatorsContainer = binding!!.indicatorsContainer
        viewPager = binding!!.onboardingViewPager
        getStarted = binding!!.getstart

        getStarted.setOnClickListener {

            if (findNavController().currentDestination!!.id == R.id.onboardFragment) {
                findNavController().navigate(R.id.action_onboardFragment_to_login_frgament)
            }

        }
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.onboarding_item_container, null)
        builder.setView(dialogView)
        val dialog = builder.create()

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler().postDelayed({
            dialog.dismiss()
        }, SPLASH_DELAY)

        setOnboardingItems()
        setupIndicators()

    }

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
//                    onboardingImage = R.drawable.welcome,
                    onboardingImage = R.drawable.pic2,
                    title = "Welcome to Anonify!",
                    description = " Where you can express yourself freely.\n Join a community where your identity is protected, and your voice is heard."
                ),
                OnboardingItem(
//                    onboardingImage = R.drawable.delivery,
                    onboardingImage = R.drawable.pic1,
                    title = " Your Privacy Matters",
                    description = "Share anonymously without fear of judgment.\n Your privacy matters to us, ensuring a secure and respectful environment for all users."
                ),
                OnboardingItem(
//                    onboardingImage = R.drawable.instant_delivery,
                    onboardingImage = R.drawable.pic3,
                    title = "Connect and Engage",
                    description = "Connect and engage with like-minded individuals.\n Join our vibrant community to seek advice, share experiences, and discover the power of anonymous expression."
                )
            )
        )

        // Set up ViewPager
        viewPager.adapter = onboardingItemsAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                isLastPage = position == onboardingItemsAdapter.itemCount - 1
            }
        })

        val recyclerView = viewPager.getChildAt(0) as RecyclerView
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setupIndicators() {

        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }
}