package com.rtgrowth.cockpit.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import com.rtgrowth.cockpit.MainActivity
import com.rtgrowth.cockpit.R

class CockpitBackgroundService : Service() {

    private val CHANNEL_ID = "cockpit_live_channel"
    private val NOTIFICATION_ID = 1001

    private var isUsersInitialLoaded = false
    private var isSendMoneyInitialLoaded = false
    private var isChatsInitialLoaded = false

    private val knownDeposits = mutableSetOf<String>()
    private val knownWithdrawals = mutableSetOf<String>()
    private val knownRecharges = mutableSetOf<String>()
    private val knownTasks = mutableSetOf<String>()
    private val knownSendMoney = mutableSetOf<String>()
    private val knownChats = mutableSetOf<String>()

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        
        // ১. ওয়েক-লক চালু (স্ক্রিন বন্ধ থাকলেও প্রসেসর চালু রাখবে)
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Cockpit::LiveSyncWakeLock").apply {
            acquire(24 * 60 * 60 * 1000L) // ২৪ ঘণ্টা ওয়েক-লক ধরে রাখবে
        }

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createForegroundNotification())
        startFirebaseLiveMonitoring()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // কোনো কারণে বন্ধ হলে সিস্টেম নিজে থেকেই রিস্টার্ট করবে
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
                setShowBadge(true)
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
            .setContentTitle("RT Growth Cockpit Live Active")
            .setContentText("Background real-time listener active...")
            .setSmallIcon(R.drawable.ic_notification_bell) // আপডেট: বেল আইকন
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
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
            .setSmallIcon(R.drawable.ic_notification_bell) // আপডেট: নতুন এলার্টেও বেল আইকন
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
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
            
            // ফায়ারবেসকে ব্যাকগ্রাউন্ডে সক্রিয় রাখার কমান্ড
            db.goOnline()
            db.getReference("users").keepSynced(true)
            db.getReference("pending_send_money").keepSynced(true)
            db.getReference("chats").keepSynced(true)

            // ১. ডিপোজিট, উইথড্র, রিচার্জ, টাইপিং মনিটরিং
            db.getReference("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (uChild in snapshot.children) {
                        val phone = uChild.key ?: ""
                        val name = uChild.child("name").getValue(String::class.java) ?: phone

                        // ডিপোজিট
                        for (d in uChild.child("deposits").children) {
                            val key = "${phone}_dep_${d.key}"
                            val isPending = d.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownDeposits.contains(key)) {
                                val amt = d.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerSystemNotification("New Deposit Request!", "$name ($phone) sent ৳$amt deposit request.")
                            }
                            if (isPending) knownDeposits.add(key)
                        }

                        // উইথড্র
                        for (w in uChild.child("withdrawals").children) {
                            val key = "${phone}_wd_${w.key}"
                            val isPending = w.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownWithdrawals.contains(key)) {
                                val amt = w.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerSystemNotification("New Cash Out Request!", "$name ($phone) requested cash out of ৳$amt.")
                            }
                            if (isPending) knownWithdrawals.add(key)
                        }

                        // রিচার্জ
                        for (r in uChild.child("recharges").children) {
                            val key = "${phone}_rc_${r.key}"
                            val isPending = r.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownRecharges.contains(key)) {
                                triggerSystemNotification("New Recharge Request!", "$name ($phone) requested mobile recharge.")
                            }
                            if (isPending) knownRecharges.add(key)
                        }

                        // টাইপিং কাজ
                        for (p in uChild.child("paragraph_jobs").children) {
                            val key = "${phone}_pt_${p.key}"
                            val isPending = p.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownTasks.contains(key)) {
                                triggerSystemNotification("New Typing Task!", "$name ($phone) submitted a task for approval.")
                            }
                            if (isPending) knownTasks.add(key)
                        }
                    }
                    isUsersInitialLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // ২. সেন্ড মানি মনিটরিং
            db.getReference("pending_send_money").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (c in snapshot.children) {
                        val key = "sm_${c.key}"
                        val isPending = c.child("status").getValue(String::class.java) == "Pending"
                        if (isPending && isSendMoneyInitialLoaded && !knownSendMoney.contains(key)) {
                            val sender = c.child("sender").getValue(String::class.java) ?: ""
                            val amt = c.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                            triggerSystemNotification("New Send Money Request!", "$sender requested transfer of ৳$amt.")
                        }
                        if (isPending) knownSendMoney.add(key)
                    }
                    isSendMoneyInitialLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // ৩. সাপোর্ট চ্যাট মনিটরিং
            db.getReference("chats").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (u in snapshot.children) {
                        val phone = u.key ?: ""
                        for (m in u.child("messages").children) {
                            val key = "chat_${m.key}"
                            val sender = m.child("sender").getValue(String::class.java) ?: ""
                            val seen = m.child("seen").getValue(Boolean::class.java) ?: false
                            val text = m.child("text").getValue(String::class.java) ?: ""
                            if (sender == "user" && !seen && isChatsInitialLoaded && !knownChats.contains(key)) {
                                triggerSystemNotification("New Message from $phone", text)
                            }
                            if (sender == "user" && !seen) knownChats.add(key)
                        }
                    }
                    isChatsInitialLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        } catch (e: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
