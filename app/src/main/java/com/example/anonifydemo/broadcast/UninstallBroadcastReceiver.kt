package com.example.anonifydemo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class UninstallBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_PACKAGE_REMOVED) {
            // Get the package name of the uninstalled app
            val packageName = intent.dataString?.substringAfter(":")
            if (packageName == context?.packageName) {
                // Delete the shared preferences data
                clearSharedPreferences(context)
            }
        }
    }

    private fun clearSharedPreferences(context: Context?) {
        val sharedPreferences: SharedPreferences? = context?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
