package com.rtgrowth.cockpit.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.*
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import com.rtgrowth.cockpit.MainActivity
import com.rtgrowth.cockpit.R

class CockpitBackgroundService : Service() {

    private val SERVICE_CHANNEL_ID = "cockpit_service_silent_v2"
    private val ALERT_CHANNEL_ID = "cockpit_high_alerts_v2"
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
    private val mainHandler = Handler(Looper.getMainLooper())
    private var keepAliveRunnable: Runnable? = null

    override fun onCreate() {
        super.onCreate()

        // ১. সিপিইউ ব্যাকগ্রাউন্ডে জাগিয়ে রাখা
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Cockpit::RealtimeWakeLock").apply {
            acquire(24 * 60 * 60 * 1000L)
        }

        createNotificationChannels()
        startForeground(NOTIFICATION_ID, createForegroundNotification())
        startFirebaseLiveMonitoring()
        startKeepAliveHeartbeat()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com").goOnline()
        } catch (e: Exception) {}
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, CockpitBackgroundService::class.java).also {
            it.setPackage(packageName)
        }
        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext, 1, restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmService = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        )
        super.onTaskRemoved(rootIntent)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // ১. ব্যাকগ্রাউন্ড সার্ভিসের সাইলেন্ট চ্যানেল
            val serviceChannel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Cockpit Background Status",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows app background live status"
                setShowBadge(false)
            }

            // ২. নতুন ডিপোজিট/উইথড্র অ্যালার্ট চ্যানেল (সাউন্ড ও পপ-আপসহ)
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val alertChannel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "Cockpit Live Real-time Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High priority instant alerts for deposits, cash out and chats"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
                setSound(soundUri, audioAttributes)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setShowBadge(true)
            }

            manager.createNotificationChannel(serviceChannel)
            manager.createNotificationChannel(alertChannel)
        }
    }

    private fun createForegroundNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
            .setContentTitle("RT Growth Cockpit Live")
            .setContentText("সার্ভিস ব্যাকগ্রাউন্ডে সক্রিয় রয়েছে...")
            .setSmallIcon(R.drawable.ic_notification_bell)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun triggerSystemNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification_bell)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 400, 200, 400))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun startKeepAliveHeartbeat() {
        keepAliveRunnable = object : Runnable {
            override fun run() {
                try {
                    FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com").goOnline()
                } catch (e: Exception) {}
                mainHandler.postDelayed(this, 15000) // প্রতি ১৫ সেকেন্ড পর পর কানেকশন জাগিয়ে রাখবে
            }
        }
        mainHandler.post(keepAliveRunnable!!)
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
                                triggerSystemNotification("🔔 New Deposit Request!", "$name ($phone) sent ৳$amt deposit request.")
                            }
                            if (isPending) knownDeposits.add(key)
                        }

                        // উইথড্র
                        for (w in uChild.child("withdrawals").children) {
                            val key = "${phone}_wd_${w.key}"
                            val isPending = w.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownWithdrawals.contains(key)) {
                                val amt = w.child("amount").getValue(Any::class.java)?.toString() ?: "0"
                                triggerSystemNotification("💰 New Cash Out Request!", "$name ($phone) requested cash out of ৳$amt.")
                            }
                            if (isPending) knownWithdrawals.add(key)
                        }

                        // রিচার্জ
                        for (r in uChild.child("recharges").children) {
                            val key = "${phone}_rc_${r.key}"
                            val isPending = r.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownRecharges.contains(key)) {
                                triggerSystemNotification("📱 New Recharge Request!", "$name ($phone) requested mobile recharge.")
                            }
                            if (isPending) knownRecharges.add(key)
                        }

                        // টাইপিং কাজ
                        for (p in uChild.child("paragraph_jobs").children) {
                            val key = "${phone}_pt_${p.key}"
                            val isPending = p.child("status").getValue(String::class.java) == "Pending"
                            if (isPending && isUsersInitialLoaded && !knownTasks.contains(key)) {
                                triggerSystemNotification("📝 New Typing Task!", "$name ($phone) submitted a task for approval.")
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
                            triggerSystemNotification("💸 New Send Money Request!", "$sender requested transfer of ৳$amt.")
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
                                triggerSystemNotification("💬 New Message from $phone", text)
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
        keepAliveRunnable?.let { mainHandler.removeCallbacks(it) }
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
