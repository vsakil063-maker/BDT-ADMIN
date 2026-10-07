package com.rtgrowth.cockpit.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import com.rtgrowth.cockpit.MainActivity

class CockpitBackgroundService : Service() {

    private val CHANNEL_ID = "cockpit_live_channel"
    private val NOTIFICATION_ID = 1001
    private var isFirstLoad = true
    private val notifiedKeys = mutableSetOf<String>()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createForegroundNotification())
        startFirebaseLiveMonitoring()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cockpit Live Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Live alerts for deposits, withdrawals, tasks, and chats"
                enableVibration(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createForegroundNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("RT Growth Cockpit Active")
            .setContentText("Monitoring live transactions and messages...")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun triggerSystemNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun startFirebaseLiveMonitoring() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBgn1GgF4sYq65N4yVxvwxfeU-_WP8lmNs")
                    .setApplicationId("1:334771083987:web:6bba8c6183f9e329d39226")
                    .setDatabaseUrl("https://typing-5c3e4-default-rtdb.firebaseio.com")
                    .setProjectId("typing-5c3e4")
                    .build()
                FirebaseApp.initializeApp(this, options)
            }

            val db = FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com")

            // ১. ডিপোজিট, উইথড্র, রিচার্জ, টাইপিং কাজ মনিটরিং
            db.getReference("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFirstLoad) return

                    for (uChild in snapshot.children) {
                        val phone = uChild.key ?: ""
                        val name = uChild.child("name").getValue(String::class.java) ?: phone

                        // ডিপোজিট
                        for (d in uChild.child("deposits").children) {
                            val key = "${phone}_dep_${d.key}"
                            if (d.child("status").getValue(String::class.java) == "Pending" && !notifiedKeys.contains(key)) {
                                val amt = d.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerSystemNotification("New Deposit Request!", "$name ($phone) sent deposit request of ৳$amt")
                                notifiedKeys.add(key)
                            }
                        }

                        // উইথড্র
                        for (w in uChild.child("withdrawals").children) {
                            val key = "${phone}_wd_${w.key}"
                            if (w.child("status").getValue(String::class.java) == "Pending" && !notifiedKeys.contains(key)) {
                                val amt = w.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerSystemNotification("New Cash Out Request!", "$name ($phone) requested cash out of ৳$amt")
                                notifiedKeys.add(key)
                            }
                        }

                        // রিচার্জ
                        for (r in uChild.child("recharges").children) {
                            val key = "${phone}_rc_${r.key}"
                            if (r.child("status").getValue(String::class.java) == "Pending" && !notifiedKeys.contains(key)) {
                                triggerSystemNotification("New Mobile Recharge!", "$name ($phone) requested mobile recharge.")
                                notifiedKeys.add(key)
                            }
                        }

                        // টাইপিং কাজ
                        for (p in uChild.child("paragraph_jobs").children) {
                            val key = "${phone}_pt_${p.key}"
                            if (p.child("status").getValue(String::class.java) == "Pending" && !notifiedKeys.contains(key)) {
                                triggerSystemNotification("New Typing Task Submitted!", "$name ($phone) submitted a task for review.")
                                notifiedKeys.add(key)
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // ২. সেন্ড মানি মনিটরিং
            db.getReference("pending_send_money").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFirstLoad) return
                    for (c in snapshot.children) {
                        val key = "sm_${c.key}"
                        if (c.child("status").getValue(String::class.java) == "Pending" && !notifiedKeys.contains(key)) {
                            val sender = c.child("sender").getValue(String::class.java) ?: ""
                            val amt = c.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                            triggerSystemNotification("New Send Money Request!", "Sender: $sender requested transfer of ৳$amt")
                            notifiedKeys.add(key)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // ৩. সাপোর্ট চ্যাট মেসেজ মনিটরিং
            db.getReference("chats").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFirstLoad) {
                        isFirstLoad = false
                        return
                    }
                    for (u in snapshot.children) {
                        val phone = u.key ?: ""
                        for (m in u.child("messages").children) {
                            val key = "chat_${m.key}"
                            val sender = m.child("sender").getValue(String::class.java) ?: ""
                            val seen = m.child("seen").getValue(Boolean::class.java) ?: false
                            val text = m.child("text").getValue(String::class.java) ?: ""
                            if (sender == "user" && !seen && !notifiedKeys.contains(key)) {
                                triggerSystemNotification("New Support Message from $phone", text)
                                notifiedKeys.add(key)
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        } catch (e: Exception) {}
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
