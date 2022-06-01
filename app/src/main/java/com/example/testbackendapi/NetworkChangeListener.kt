package com.example.testbackendapi

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.widget.AppCompatButton;


class NetworkChangeListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!context?.let { Common.isConnectedToInternet(it) }!!) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.no_internet_connection, null)
            builder.setView(view)
            val btnRetry: AppCompatButton = view.findViewById(R.id.btn_retry)
            val dialog: AlertDialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)
            dialog.getWindow()?.setGravity(Gravity.CENTER)
            btnRetry.setOnClickListener { view1: View? ->
                dialog.dismiss()
                onReceive(context, intent)
            }
        }
    }
}