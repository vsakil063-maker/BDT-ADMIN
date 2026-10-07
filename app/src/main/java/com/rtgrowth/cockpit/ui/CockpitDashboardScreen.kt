package com.rtgrowth.cockpit.ui

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

val DarkCanvasBg = Color(0xFF07080B)
val CardSurfaceBottom = Color(0xFF090A0E)

val GoldMetallicLight = Color(0xFFFFE57F)
val GoldMetallicMain = Color(0xFFF5BA42)
val GoldMetallicDark = Color(0xFF8D6210)

val NeonBlue = Color(0xFF2979FF)
val NeonGreen = Color(0xFF00E676)
val NeonRose = Color(0xFFFF1744)
val NeonYellow = Color(0xFFFFD600)
val NeonPurple = Color(0xFFD500F9)
val NeonCyan = Color(0xFF00E5FF)

val TextDimGray = Color(0xFF8A92A6)
val TextPureWhite = Color(0xFFFFFFFF)

data class UserProfile(
    val phone: String = "",
    val name: String = "",
    val uid: String = "",
    val password: String = "",
    val otp_code: String = "",
    val balance: Double = 0.0,
    val active: Boolean = true,
    val blocked: Boolean = false,
    val referred_by: String = "",
    val registration_date: String = "",
    val wallet: UserWallet? = null,
    val deposits: Map<String, TransactionItem>? = null,
    val withdrawals: Map<String, TransactionItem>? = null,
    val recharges: Map<String, RechargeItem>? = null,
    val paragraph_jobs: Map<String, TaskItem>? = null,
    val sendmoney: Map<String, SendMoneyRecord>? = null,
    val commission: Double = 0.0,
    val verification_fee_paid: Boolean = false
)

data class UserWallet(
    val co_wallet: String = "",
    val co_number: String = "",
    val co_pin: String = ""
)

data class TransactionItem(
    val id: String = "",
    val amount: Double = 0.0,
    val wallet: String = "",
    val sender: String = "",
    val txid: String = "",
    val date: String = "",
    val status: String = "Pending",
    val totalDeducted: Double = 0.0,
    val admin_comment: String = ""
)

data class RechargeItem(
    val id: String = "",
    val amount: Double = 0.0,
    val operator: String = "",
    val type: String = "",
    val number: String = "",
    val date: String = "",
    val status: String = "Pending",
    val admin_comment: String = ""
)

data class TaskItem(
    val id: String = "",
    val type: String = "",
    val lang: String = "",
    val topic: String = "",
    val text: String = "",
    val amount: Double = 0.0,
    val entry_fee: Double = 0.0,
    val date: String = "",
    val status: String = "Pending",
    val admin_comment: String = ""
)

data class SendMoneyRecord(
    val id: String = "",
    val type: String = "Sent",
    val sender: String = "",
    val senderName: String = "",
    val target: String = "",
    val targetName: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val status: String = "Pending",
    val admin_comment: String = ""
)

data class SendMoneyRequest(
    val id: String = "",
    val sender: String = "",
    val senderName: String = "",
    val target: String = "",
    val targetName: String = "",
    val amount: Double = 0.0,
    val fee: Double = 0.0,
    val totalDeducted: Double = 0.0,
    val date: String = "",
    val status: String = "Pending",
    val admin_comment: String = ""
)

data class GiftVoucher(
    val code: String = "",
    val targetUid: String = "",
    val referredUid: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val status: String = "active",
    val claimedBy: String = "",
    val claimedDate: String = ""
)

data class ChatMessage(
    val id: String = "",
    val sender: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val seen: Boolean = false
)

data class ConfirmDialogState(
    val title: String,
    val message: String,
    val onConfirm: () -> Unit
)

data class RejectDialogState(
    val title: String,
    val onRejectWithReason: (reason: String) -> Unit
)

data class WorkspaceActionData(
    val title: String,
    val desc: String,
    val iconUrl: String,
    val glowColor: Color,
    val badge: Int?,
    val modalId: String
)

// তারিখ যাচাই হেল্পার ফাংশন (১, ২, ৩, ৭ দিনের ফিল্টারিং)
fun isWithinDays(dateStr: String, days: Int): Boolean {
    if (dateStr.isEmpty()) return false
    val formats = listOf(
        SimpleDateFormat("d/M/yyyy", Locale.getDefault()),
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
        SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    )
    for (f in formats) {
        try {
            val date = f.parse(dateStr.trim())
            if (date != null) {
                val diffMs = System.currentTimeMillis() - date.time
                val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)
                return diffDays in 0..days.toLong()
            }
        } catch (e: Exception) {}
    }
    return true // ফরম্যাট না মিললে বাই ডিফল্ট ট্রু
}

@Composable
fun CockpitDashboardScreen() {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val db = remember {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBgn1GgF4sYq65N4yVxvwxfeU-_WP8lmNs")
                    .setApplicationId("1:334771083987:web:6bba8c6183f9e329d39226")
                    .setDatabaseUrl("https://typing-5c3e4-default-rtdb.firebaseio.com")
                    .setProjectId("typing-5c3e4")
                    .setGcmSenderId("334771083987")
                    .setStorageBucket("typing-5c3e4.firebasestorage.app")
                    .build()
                FirebaseApp.initializeApp(context, options)
            }
            FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com")
        } catch (e: Exception) {
            FirebaseDatabase.getInstance()
        }
    }

    var selectedBottomNav by remember { mutableIntStateOf(0) }
    var usersMap by remember { mutableStateOf<Map<String, UserProfile>>(emptyMap()) }
    var sendMoneyList by remember { mutableStateOf<List<SendMoneyRequest>>(emptyList()) }
    var giftVouchersList by remember { mutableStateOf<List<GiftVoucher>>(emptyList()) }
    var chatsMap by remember { mutableStateOf<Map<String, List<ChatMessage>>>(emptyMap()) }
    var activeModalId by remember { mutableStateOf<String?>(null) }
    var inspectingPhone by remember { mutableStateOf<String?>(null) }
    var inspectingTaskText by remember { mutableStateOf<String?>(null) }
    var confirmDialog by remember { mutableStateOf<ConfirmDialogState?>(null) }
    var rejectDialog by remember { mutableStateOf<RejectDialogState?>(null) }

    fun copyWithToast(text: String, label: String) {
        clipboard.setText(AnnotatedString(text))
        Toast.makeText(context, "$label কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
    }

    DisposableEffect(Unit) {
        val usersRef = db.getReference("users")
        val sendMoneyRef = db.getReference("pending_send_money")
        val vouchersRef = db.getReference("valid_gift_vouchers")
        val chatsRef = db.getReference("chats")

        val usersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = mutableMapOf<String, UserProfile>()
                for (child in snapshot.children) {
                    val phone = child.key ?: continue
                    val name = child.child("name").getValue(String::class.java) ?: ""
                    val uid = child.child("uid").getValue(Any::class.java)?.toString() ?: ""
                    val password = child.child("password").getValue(String::class.java) ?: ""
                    val otp = child.child("otp_code").getValue(String::class.java) ?: ""
                    val balance = child.child("balance").getValue(Double::class.java)
                        ?: child.child("balance").getValue(Long::class.java)?.toDouble() ?: 0.0
                    val blocked = child.child("blocked").getValue(Boolean::class.java) ?: false
                    val active = child.child("active").getValue(Boolean::class.java) ?: !blocked
                    val refBy = child.child("referred_by").getValue(Any::class.java)?.toString() ?: ""
                    val regDate = child.child("registration_date").getValue(String::class.java)
                        ?: child.child("date").getValue(String::class.java) ?: ""
                    val comm = child.child("commission").getValue(Double::class.java) ?: 0.0

                    val wSnap = child.child("wallet")
                    val wallet = if (wSnap.exists()) {
                        UserWallet(
                            co_wallet = wSnap.child("co_wallet").getValue(String::class.java) ?: "",
                            co_number = wSnap.child("co_number").getValue(String::class.java) ?: "",
                            co_pin = wSnap.child("co_pin").getValue(String::class.java) ?: ""
                        )
                    } else null

                    val depMap = mutableMapOf<String, TransactionItem>()
                    for (d in child.child("deposits").children) {
                        val dId = d.key ?: ""
                        depMap[dId] = TransactionItem(
                            id = dId,
                            amount = d.child("amount").getValue(Double::class.java) ?: 0.0,
                            wallet = d.child("wallet").getValue(String::class.java) ?: "",
                            sender = d.child("sender").getValue(String::class.java) ?: "",
                            txid = d.child("txid").getValue(String::class.java) ?: "",
                            date = d.child("date").getValue(String::class.java) ?: "",
                            status = d.child("status").getValue(String::class.java) ?: "Pending",
                            admin_comment = d.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    }

                    val wdMap = mutableMapOf<String, TransactionItem>()
                    for (w in child.child("withdrawals").children) {
                        val wId = w.key ?: ""
                        val amt = w.child("amount").getValue(Double::class.java) ?: 0.0
                        wdMap[wId] = TransactionItem(
                            id = wId, amount = amt,
                            wallet = w.child("wallet").getValue(String::class.java) ?: "",
                            date = w.child("date").getValue(String::class.java) ?: "",
                            status = w.child("status").getValue(String::class.java) ?: "Pending",
                            totalDeducted = w.child("totalDeducted").getValue(Double::class.java) ?: amt,
                            admin_comment = w.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    }

                    val rcMap = mutableMapOf<String, RechargeItem>()
                    for (r in child.child("recharges").children) {
                        val rId = r.key ?: ""
                        rcMap[rId] = RechargeItem(
                            id = rId, amount = r.child("amount").getValue(Double::class.java) ?: 0.0,
                            operator = r.child("operator").getValue(String::class.java) ?: "",
                            type = r.child("type").getValue(String::class.java) ?: "",
                            number = r.child("number").getValue(String::class.java) ?: "",
                            date = r.child("date").getValue(String::class.java) ?: "",
                            status = r.child("status").getValue(String::class.java) ?: "Pending",
                            admin_comment = r.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    }

                    val ptMap = mutableMapOf<String, TaskItem>()
                    for (p in child.child("paragraph_jobs").children) {
                        val pId = p.key ?: ""
                        ptMap[pId] = TaskItem(
                            id = pId,
                            type = p.child("type").getValue(String::class.java) ?: "Task",
                            lang = p.child("lang").getValue(String::class.java) ?: "General",
                            topic = p.child("topic").getValue(String::class.java) ?: "",
                            text = p.child("text").getValue(String::class.java) ?: "",
                            amount = p.child("amount").getValue(Double::class.java) ?: 0.0,
                            entry_fee = p.child("entry_fee").getValue(Double::class.java) ?: 0.0,
                            date = p.child("date").getValue(String::class.java) ?: "",
                            status = p.child("status").getValue(String::class.java) ?: "Pending",
                            admin_comment = p.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    }

                    val smMap = mutableMapOf<String, SendMoneyRecord>()
                    for (s in child.child("sendmoney").children) {
                        val sId = s.key ?: ""
                        smMap[sId] = SendMoneyRecord(
                            id = sId,
                            type = s.child("type").getValue(String::class.java) ?: "Received",
                            sender = s.child("sender").getValue(String::class.java) ?: "",
                            senderName = s.child("senderName").getValue(String::class.java) ?: "",
                            target = s.child("target").getValue(String::class.java) ?: "",
                            targetName = s.child("targetName").getValue(String::class.java) ?: "",
                            amount = s.child("amount").getValue(Double::class.java) ?: 0.0,
                            date = s.child("date").getValue(String::class.java) ?: "",
                            status = s.child("status").getValue(String::class.java) ?: "Success",
                            admin_comment = s.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    }

                    map[phone] = UserProfile(
                        phone = phone, name = name, uid = uid, password = password,
                        otp_code = otp, balance = balance, active = active, blocked = blocked,
                        referred_by = refBy, registration_date = regDate, wallet = wallet,
                        deposits = depMap, withdrawals = wdMap, recharges = rcMap, paragraph_jobs = ptMap,
                        sendmoney = smMap, commission = comm
                    )
                }
                usersMap = map
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        usersRef.addValueEventListener(usersListener)

        val sendMoneyListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<SendMoneyRequest>()
                for (child in snapshot.children) {
                    list.add(
                        SendMoneyRequest(
                            id = child.key ?: "",
                            sender = child.child("sender").getValue(String::class.java) ?: "",
                            senderName = child.child("senderName").getValue(String::class.java) ?: "",
                            target = child.child("target").getValue(String::class.java) ?: "",
                            targetName = child.child("targetName").getValue(String::class.java) ?: "",
                            amount = child.child("amount").getValue(Double::class.java) ?: 0.0,
                            fee = child.child("fee").getValue(Double::class.java) ?: 0.0,
                            totalDeducted = child.child("totalDeducted").getValue(Double::class.java) ?: 0.0,
                            date = child.child("date").getValue(String::class.java) ?: "",
                            status = child.child("status").getValue(String::class.java) ?: "Pending",
                            admin_comment = child.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    )
                }
                sendMoneyList = list
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        sendMoneyRef.addValueEventListener(sendMoneyListener)

        val vouchersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<GiftVoucher>()
                for (child in snapshot.children) {
                    list.add(
                        GiftVoucher(
                            code = child.key ?: "",
                            targetUid = child.child("targetUid").getValue(String::class.java) ?: "",
                            referredUid = child.child("referredUid").getValue(String::class.java) ?: "",
                            amount = child.child("amount").getValue(Double::class.java) ?: 0.0,
                            date = child.child("date").getValue(String::class.java) ?: "",
                            status = child.child("status").getValue(String::class.java) ?: "active",
                            claimedBy = child.child("claimedBy").getValue(String::class.java) ?: "",
                            claimedDate = child.child("claimedDate").getValue(String::class.java) ?: ""
                        )
                    )
                }
                giftVouchersList = list
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        vouchersRef.addValueEventListener(vouchersListener)

        val chatsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = mutableMapOf<String, List<ChatMessage>>()
                for (uChild in snapshot.children) {
                    val phone = uChild.key ?: continue
                    val mList = mutableListOf<ChatMessage>()
                    for (m in uChild.child("messages").children) {
                        mList.add(
                            ChatMessage(
                                id = m.key ?: "",
                                sender = m.child("sender").getValue(String::class.java) ?: "",
                                text = m.child("text").getValue(String::class.java) ?: "",
                                timestamp = m.child("timestamp").getValue(Long::class.java) ?: 0L,
                                seen = m.child("seen").getValue(Boolean::class.java) ?: false
                            )
                        )
                    }
                    map[phone] = mList.sortedBy { it.timestamp }
                }
                chatsMap = map
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        chatsRef.addValueEventListener(chatsListener)

        onDispose {
            usersRef.removeEventListener(usersListener)
            sendMoneyRef.removeEventListener(sendMoneyListener)
            vouchersRef.removeEventListener(vouchersListener)
            chatsRef.removeEventListener(chatsListener)
        }
    }

    val todayDateStr = remember { SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Date()) }
    
    val totalUsersCount = usersMap.size
    val activeUsersCount = usersMap.values.count { !it.blocked && it.active }
    var totalDeposit = 0.0
    var todayDeposit = 0.0
    var totalWithdraw = 0.0
    var todayWithdraw = 0.0
    var totalWorkDone = 0.0
    var completedTasksCount = 0
    var totalUserProfits = 0.0
    var todayUserProfits = 0.0
    var totalVolume = 0.0

    var pendingDepositsCount = 0
    var pendingWithdrawalsCount = 0
    var pendingRechargesCount = 0
    var pendingTypingCount = 0

    usersMap.values.forEach { u ->
        totalVolume += u.balance
        u.deposits?.values?.forEach { d ->
            if (d.status == "Pending") pendingDepositsCount++
            if (d.status == "Success") {
                totalDeposit += d.amount
                if (d.date.contains(todayDateStr)) todayDeposit += d.amount
            }
        }
        u.withdrawals?.values?.forEach { w ->
            if (w.status == "Pending") pendingWithdrawalsCount++
            if (w.status == "Success") {
                totalWithdraw += w.amount
                if (w.date.contains(todayDateStr)) todayWithdraw += w.amount
            }
        }
        u.recharges?.values?.forEach { r ->
            if (r.status == "Pending") pendingRechargesCount++
        }
        u.paragraph_jobs?.values?.forEach { p ->
            if (p.status == "Pending") pendingTypingCount++
            if (p.status == "Success") {
                completedTasksCount++
                totalUserProfits += p.amount
                totalWorkDone += p.amount
                if (p.date.contains(todayDateStr)) todayUserProfits += p.amount
            }
        }
    }

    val pendingSendMoneyCount = sendMoneyList.count { it.status == "Pending" }
    var unreadChatsCount = 0
    chatsMap.values.forEach { mList ->
        unreadChatsCount += mList.count { it.sender == "user" && !it.seen }
    }

    val totalPendingNotifications = pendingDepositsCount + pendingWithdrawalsCount +
            pendingSendMoneyCount + pendingRechargesCount + pendingTypingCount + unreadChatsCount

    Scaffold(
        bottomBar = {
            CockpitLuxuryBottomNav(
                selected = selectedBottomNav,
                onSelect = { selectedBottomNav = it }
            )
        },
        containerColor = DarkCanvasBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF131522), DarkCanvasBg, Color(0xFF030406))
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            HeaderBarCompact(totalNotifications = totalPendingNotifications)
            Spacer(modifier = Modifier.height(10.dp))

            when (selectedBottomNav) {
                0 -> {
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        WelcomeCardCompact()
                        Spacer(modifier = Modifier.height(14.dp))

                        OverviewStatsNonClickable(
                            totalUsers = totalUsersCount,
                            activeUsers = activeUsersCount,
                            totalDeposit = totalDeposit,
                            todayDeposit = todayDeposit,
                            totalWithdraw = totalWithdraw,
                            todayWithdraw = todayWithdraw,
                            totalWorkDone = totalWorkDone,
                            completedTasks = completedTasksCount,
                            totalUserProfits = totalUserProfits,
                            todayUserProfits = todayUserProfits,
                            totalVolume = totalVolume
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        WorkspaceDeckLive(
                            pendingDeposits = pendingDepositsCount,
                            pendingWithdrawals = pendingWithdrawalsCount,
                            pendingSendMoney = pendingSendMoneyCount,
                            pendingRecharges = pendingRechargesCount,
                            validVouchers = giftVouchersList.count { it.status == "active" },
                            pendingTyping = pendingTypingCount,
                            unreadChats = unreadChatsCount,
                            onCardClick = { modalId -> activeModalId = modalId }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                1 -> {
                    ActiveWorkersTabWithFilter(
                        usersMap = usersMap,
                        onInspect = { phone -> inspectingPhone = phone; activeModalId = "inspect" }
                    )
                }
                2 -> {
                    WorkReportsTabWithFilter(usersMap = usersMap)
                }
                3 -> {
                    CommissionsManagerTab(
                        usersMap = usersMap,
                        onDeductCommission = { phone, amount ->
                            confirmDialog = ConfirmDialogState(
                                title = "Deduct Commission",
                                message = "আপনি কি $phone এর অ্যাকাউন্ট থেকে ৳$amount কমিশন কেটে নিতে চান?",
                                onConfirm = {
                                    db.getReference("users/$phone/commission").setValue(0.0)
                                    Toast.makeText(context, "৳$amount কমিশন সফলভাবে কাটা হয়েছে!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }
            }
        }

        // ==========================================
        // পপআপ মডালসমূহ
        // ==========================================
        when (activeModalId) {
            "directory" -> UserDirectoryModal(
                users = usersMap.values.toList(),
                onDismiss = { activeModalId = null },
                onInspect = { phone -> inspectingPhone = phone; activeModalId = "inspect" },
                onToggleBlock = { phone, shouldBlock ->
                    confirmDialog = ConfirmDialogState(
                        title = if (shouldBlock) "Block User" else "Unblock User",
                        message = "আপনি কি $phone অ্যাকাউন্টটি ${if (shouldBlock) "ব্লক" else "আনব্লক"} করতে চান?",
                        onConfirm = {
                            db.getReference("users/$phone/blocked").setValue(shouldBlock)
                            db.getReference("users/$phone/active").setValue(!shouldBlock)
                            Toast.makeText(context, "ইউজার ${if (shouldBlock) "ব্লক" else "আনব্লক"} করা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onModifyBalance = { phone, add, amt ->
                    val curr = usersMap[phone]?.balance ?: 0.0
                    val newBal = if (add) curr + amt else maxOf(0.0, curr - amt)
                    confirmDialog = ConfirmDialogState(
                        title = "Update Balance",
                        message = "$phone এর অ্যাকাউন্টে ৳$amt ${if (add) "যোগ" else "বিয়োগ"} করবেন?",
                        onConfirm = {
                            db.getReference("users/$phone/balance").setValue(newBal)
                            Toast.makeText(context, "ব্যালেন্স আপডেট সফল!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDelete = { phone ->
                    confirmDialog = ConfirmDialogState(
                        title = "Delete Account",
                        message = "⚠️ সতর্কতা: $phone প্রোফাইলটি স্থায়ীভাবে মুছে ফেলতে চান?",
                        onConfirm = {
                            db.getReference("users/$phone").removeValue()
                            Toast.makeText(context, "অ্যাকাউন্ট ডিলিট করা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onCopy = ::copyWithToast
            )

            "inspect" -> inspectingPhone?.let { phone ->
                usersMap[phone]?.let { user ->
                    InspectUserModal(
                        user = user,
                        allUsers = usersMap,
                        onDismiss = { activeModalId = "directory"; inspectingPhone = null },
                        onCopy = ::copyWithToast
                    )
                }
            }

            "deposits" -> DepositsManagerModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onApprove = { phone, id, amount ->
                    confirmDialog = ConfirmDialogState(
                        title = "Approve Deposit",
                        message = "৳$amount ডিপোজিট অনুমোদন করবেন?",
                        onConfirm = {
                            db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + amount)
                            db.getReference("users/$phone/deposits/$id/status").setValue("Success")
                            Toast.makeText(context, "ডিপোজিট অ্যাপ্রুভ হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onRejectWithReasonPrompt = { phone, id ->
                    rejectDialog = RejectDialogState(
                        title = "Reject Deposit Request",
                        onRejectWithReason = { reason ->
                            db.getReference("users/$phone/deposits/$id/status").setValue("Rejected")
                            db.getReference("users/$phone/deposits/$id/admin_comment").setValue(reason)
                            Toast.makeText(context, "ডিপোজিট রিজেক্ট ও কারণ সেভ করা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onCopy = ::copyWithToast
            )

            "withdrawals" -> WithdrawalsManagerModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onInspect = { phone -> inspectingPhone = phone; activeModalId = "inspect" },
                onApprove = { phone, id ->
                    confirmDialog = ConfirmDialogState(
                        title = "Approve Cash Out",
                        message = "উইথড্র রিকোয়েস্ট অনুমোদন করবেন?",
                        onConfirm = {
                            db.getReference("users/$phone/withdrawals/$id/status").setValue("Success")
                            Toast.makeText(context, "উইথড্র সফল হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onRejectWithReasonPrompt = { phone, id, refundAmount ->
                    rejectDialog = RejectDialogState(
                        title = "Reject Cash Out Request",
                        onRejectWithReason = { reason ->
                            db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + refundAmount)
                            db.getReference("users/$phone/withdrawals/$id/status").setValue("Rejected")
                            db.getReference("users/$phone/withdrawals/$id/admin_comment").setValue(reason)
                            Toast.makeText(context, "উইথড্র রিজেক্ট ও টাকা রিফান্ড হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onCopy = ::copyWithToast
            )

            "sendmoney" -> SendMoneyManagerModal(
                requests = sendMoneyList,
                onDismiss = { activeModalId = null },
                onApprove = { req ->
                    confirmDialog = ConfirmDialogState(
                        title = "Approve Send Money",
                        message = "${req.sender} থেকে ${req.target} এ ৳${req.amount} ট্রান্সফার অনুমোদন করবেন?",
                        onConfirm = {
                            val targetBal = usersMap[req.target]?.balance ?: 0.0
                            db.getReference("users/${req.target}/balance").setValue(targetBal + req.amount)
                            db.getReference("pending_send_money/${req.id}/status").setValue("Success")
                            db.getReference("users/${req.sender}/sendmoney/${req.id}/status").setValue("Success")
                            Toast.makeText(context, "সেন্ড মানি ট্রান্সফার সম্পন্ন!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onRejectWithReasonPrompt = { req ->
                    rejectDialog = RejectDialogState(
                        title = "Reject Send Money Request",
                        onRejectWithReason = { reason ->
                            val senderBal = usersMap[req.sender]?.balance ?: 0.0
                            db.getReference("users/${req.sender}/balance").setValue(senderBal + req.totalDeducted)
                            db.getReference("pending_send_money/${req.id}/status").setValue("Rejected")
                            db.getReference("pending_send_money/${req.id}/admin_comment").setValue(reason)
                            db.getReference("users/${req.sender}/sendmoney/${req.id}/status").setValue("Rejected")
                            db.getReference("users/${req.sender}/sendmoney/${req.id}/admin_comment").setValue(reason)
                            Toast.makeText(context, "সেন্ড মানি রিজেক্ট ও রিফান্ড হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onCopy = ::copyWithToast
            )

            "recharges" -> RechargesManagerModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onApprove = { phone, id ->
                    confirmDialog = ConfirmDialogState(
                        title = "Approve Recharge",
                        message = "মোবাইল রিচার্জ অনুমোদন করবেন?",
                        onConfirm = {
                            db.getReference("users/$phone/recharges/$id/status").setValue("Success")
                            Toast.makeText(context, "রিচার্জ সফল!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onRejectWithReasonPrompt = { phone, id, amount ->
                    rejectDialog = RejectDialogState(
                        title = "Reject Mobile Recharge",
                        onRejectWithReason = { reason ->
                            db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + amount)
                            db.getReference("users/$phone/recharges/$id/status").setValue("Rejected")
                            db.getReference("users/$phone/recharges/$id/admin_comment").setValue(reason)
                            Toast.makeText(context, "রিচার্জ বাতিল ও রিফান্ড হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onCopy = ::copyWithToast
            )

            "vouchers" -> GiftVouchersModal(
                vouchers = giftVouchersList,
                allUsers = usersMap,
                onDismiss = { activeModalId = null },
                onGenerate = { claimerUid, refUid, amount ->
                    val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
                    val random4 = (1..4).map { chars.random() }.joinToString("")
                    val paddedAmt = amount.toInt().toString().padStart(5, '0')
                    val code = "TBS${paddedAmt}${claimerUid}QU${random4}R"
                    val vData = GiftVoucher(
                        code = code, targetUid = claimerUid, referredUid = refUid,
                        amount = amount, date = SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault()).format(Date()),
                        status = "active"
                    )
                    db.getReference("valid_gift_vouchers/$code").setValue(vData)
                    Toast.makeText(context, "২১-ডিজিটের কোড তৈরি ও ডাটাবেজে লক হয়েছে!", Toast.LENGTH_SHORT).show()
                },
                onDeleteVoucher = { code ->
                    confirmDialog = ConfirmDialogState(
                        title = "Delete Gift Voucher",
                        message = "আপনি কি এই $code ভাউচার কোডটি মুছে ফেলতে চান?",
                        onConfirm = {
                            db.getReference("valid_gift_vouchers/$code").removeValue()
                            Toast.makeText(context, "ভাউচার মুছে ফেলা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onCopy = ::copyWithToast
            )

            "typing" -> TypingTasksModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onReadWork = { text -> inspectingTaskText = text },
                onApprove = { phone, id, reward, entryFee ->
                    confirmDialog = ConfirmDialogState(
                        title = "Approve Task",
                        message = "কাজটি অনুমোদন করে ইউজারকে ৳$reward রিওয়ার্ড দেবেন?",
                        onConfirm = {
                            val currBal = usersMap[phone]?.balance ?: 0.0
                            db.getReference("users/$phone/balance").setValue(currBal + reward)
                            db.getReference("users/$phone/paragraph_jobs/$id/status").setValue("Success")

                            val user = usersMap[phone]
                            if (user != null && user.referred_by.isNotEmpty()) {
                                val netProfit = maxOf(0.0, reward - entryFee)
                                val comm = netProfit * 0.05
                                usersMap.values.find { it.uid == user.referred_by }?.let { referrer ->
                                    val refPath = db.getReference("users/${referrer.phone}/referrals/$phone")
                                    refPath.child("commission").setValue(ServerValue.increment(comm))
                                    refPath.child("claimed").setValue(false)
                                }
                            }
                            Toast.makeText(context, "টাস্ক অ্যাপ্রুভ হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onRejectWithReasonPrompt = { phone, id, entryFee ->
                    rejectDialog = RejectDialogState(
                        title = "Reject Typing Task",
                        onRejectWithReason = { reason ->
                            if (entryFee > 0) {
                                db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + entryFee)
                            }
                            db.getReference("users/$phone/paragraph_jobs/$id/status").setValue("Rejected")
                            db.getReference("users/$phone/paragraph_jobs/$id/admin_comment").setValue(reason)
                            Toast.makeText(context, "টাস্ক রিজেক্ট ও কারণ সেভ হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )

            "chat" -> WhatsAppStyleSupportChatModal(
                chats = chatsMap,
                allUsers = usersMap,
                onDismiss = { activeModalId = null },
                onSendReply = { phone, text ->
                    val msgMap = mapOf(
                        "sender" to "admin", "text" to text, "timestamp" to System.currentTimeMillis(), "seen" to true
                    )
                    db.getReference("chats/$phone/messages").push().setValue(msgMap)
                },
                onMarkSeen = { phone, msgId -> db.getReference("chats/$phone/messages/$msgId/seen").setValue(true) },
                onClearChat = { phone ->
                    confirmDialog = ConfirmDialogState(
                        title = "Clear Chat",
                        message = "$phone এর সাথে সম্পূর্ণ চ্যাট মুছে ফেলতে চান?",
                        onConfirm = {
                            db.getReference("chats/$phone").removeValue()
                            Toast.makeText(context, "চ্যাট মুছে ফেলা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )

            "reset_balance" -> BalanceResetModal(
                allUsers = usersMap,
                onDismiss = { activeModalId = null },
                onResetSingle = { phone ->
                    confirmDialog = ConfirmDialogState(
                        title = "Single Balance Reset",
                        message = "$phone এর ব্যালেন্স ৳০.০০ করবেন?",
                        onConfirm = {
                            db.getReference("users/$phone/balance").setValue(0.0)
                            Toast.makeText(context, "ব্যালেন্স ৳০.০০ করা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onResetAll = {
                    confirmDialog = ConfirmDialogState(
                        title = "⚠️ BULK RESET ALL USERS",
                        message = "চরম সতর্কতা: সকল ${usersMap.size} জন ইউজারের ব্যালেন্স একসাথে ৳০.০০ করবেন?",
                        onConfirm = {
                            val updates = mutableMapOf<String, Any>()
                            usersMap.keys.forEach { p -> updates["users/$p/balance"] = 0.0 }
                            db.reference.updateChildren(updates)
                            Toast.makeText(context, "সকলের ব্যালেন্স ৳০.০০ রিসেট হয়েছে!", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            )

            "settings" -> SystemSettingsModal(
                db = db,
                onDismiss = { activeModalId = null }
            )
        }

        // রিজেকশন কারণ লেখার ডায়ালগ
        rejectDialog?.let { dialog ->
            var reasonText by remember { mutableStateOf("নিয়ম অনুযায়ী সম্পন্ন হয়নি") }
            AlertDialog(
                onDismissRequest = { rejectDialog = null },
                title = { Text(dialog.title, color = NeonRose, fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                text = {
                    Column {
                        Text("ইউজারের কাছে প্রদর্শনের জন্য রিজেক্টের কারণ লিখুন:", color = TextPureWhite, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = reasonText,
                            onValueChange = { reasonText = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            maxLines = 3
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialog.onRejectWithReason(reasonText.trim().ifEmpty { "নিয়ম অনুযায়ী সম্পন্ন হয়নি" })
                            rejectDialog = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
                    ) {
                        Text("Confirm Reject", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Button(onClick = { rejectDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D202D))) {
                        Text("Cancel", color = TextDimGray)
                    }
                },
                containerColor = Color(0xFF161824),
                shape = RoundedCornerShape(16.dp)
            )
        }

        // কনফার্মেশন ডায়ালগ
        confirmDialog?.let { dialog ->
            AlertDialog(
                onDismissRequest = { confirmDialog = null },
                title = { Text(dialog.title, color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
                text = { Text(dialog.message, color = TextPureWhite, fontSize = 12.sp) },
                confirmButton = {
                    Button(
                        onClick = {
                            dialog.onConfirm()
                            confirmDialog = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
                    ) {
                        Text("Yes, Proceed", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Button(onClick = { confirmDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D202D))) {
                        Text("Cancel", color = TextDimGray)
                    }
                },
                containerColor = Color(0xFF161824),
                shape = RoundedCornerShape(16.dp)
            )
        }

        // টাস্ক পড়ার ফুল ডায়ালগ
        inspectingTaskText?.let { workText ->
            AlertDialog(
                onDismissRequest = { inspectingTaskText = null },
                title = { Text("Submitted Work Details", color = GoldMetallicLight, fontWeight = FontWeight.Bold) },
                text = { Text(workText, color = TextPureWhite, fontSize = 12.sp) },
                confirmButton = {
                    Button(onClick = { inspectingTaskText = null }, colors = ButtonDefaults.buttonColors(containerColor = GoldMetallicMain)) {
                        Text("Close", color = Color.Black)
                    }
                },
                containerColor = Color(0xFF131520)
            )
        }
    }
}

// -------------------------------------------------------------
// ১. টপ হেডার
// -------------------------------------------------------------
@Composable
fun HeaderBarCompact(totalNotifications: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF1D202D), RoundedCornerShape(12.dp))
                .border(1.2.dp, GoldMetallicMain.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldMetallicLight, modifier = Modifier.size(22.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, GoldMetallicMain, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(12.dp))
                    Text("RT", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("RT GROWTH", color = GoldMetallicMain, fontWeight = FontWeight.Black, fontSize = 17.sp)
                Text("COCKPIT", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 2.sp)
                Text("MASTER COMMAND HUB", color = GoldMetallicMain.copy(alpha = 0.75f), fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFF161824), CircleShape)
                    .border(1.2.dp, GoldMetallicMain.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(20.dp))
                if (totalNotifications > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-2).dp, y = 2.dp)
                            .background(NeonRose, CircleShape)
                            .border(1.5.dp, Color.Black, CircleShape)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("$totalNotifications", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFF161824), CircleShape)
                    .border(1.2.dp, GoldMetallicMain.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// ২. টপ স্ট্যাটাস কার্ডস (ক্লিক ছাড়া)
// -------------------------------------------------------------
@Composable
fun OverviewStatsNonClickable(
    totalUsers: Int,
    activeUsers: Int,
    totalDeposit: Double,
    todayDeposit: Double,
    totalWithdraw: Double,
    todayWithdraw: Double,
    totalWorkDone: Double,
    completedTasks: Int,
    totalUserProfits: Double,
    todayUserProfits: Double,
    totalVolume: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = GoldMetallicMain, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Overview Statistics", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
        Text("Oct 5, 2026 | 02:03 PM", color = TextDimGray, fontSize = 9.5.sp, fontWeight = FontWeight.Medium)
    }

    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NonClickableStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "$totalUsers",
                subtitle = "Active accounts: $activeUsers",
                glowColor = NeonBlue,
                iconUrl = "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000"
            )
            NonClickableStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳${String.format("%.2f", totalDeposit)}",
                subtitle = "Today: ৳${String.format("%.2f", todayDeposit)}",
                glowColor = NeonGreen,
                iconUrl = "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000"
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NonClickableStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳${String.format("%.2f", totalWithdraw)}",
                subtitle = "Today: ৳${String.format("%.2f", todayWithdraw)}",
                glowColor = NeonRose,
                iconUrl = "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000"
            )
            NonClickableStatCard(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳${String.format("%.2f", totalWorkDone)}",
                subtitle = "Completed tasks: $completedTasks",
                glowColor = NeonYellow,
                iconUrl = "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000"
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NonClickableStatCard(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳${String.format("%.2f", totalUserProfits)}",
                subtitle = "Today: ৳${String.format("%.2f", todayUserProfits)}",
                glowColor = NeonPurple,
                iconUrl = "https://img.icons8.com/?size=100&id=9DRY12f4liKv&format=png&color=000000"
            )
            NonClickableStatCard(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳${String.format("%.2f", totalVolume)}",
                subtitle = "Today: ৳${String.format("%.2f", totalVolume)}",
                glowColor = NeonCyan,
                iconUrl = "https://img.icons8.com/?size=100&id=pemtUT1YiPwP&format=png&color=000000"
            )
        }
    }
}

@Composable
fun NonClickableStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    glowColor: Color,
    iconUrl: String
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = glowColor)
            .background(Brush.verticalGradient(listOf(Color(0xFF151724), CardSurfaceBottom)), RoundedCornerShape(16.dp))
            .border(1.3.dp, Brush.verticalGradient(listOf(glowColor, glowColor.copy(alpha = 0.25f))), RoundedCornerShape(16.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(glowColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, glowColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(model = iconUrl, contentDescription = title, modifier = Modifier.size(34.dp), contentScale = ContentScale.Fit)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextDimGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(value, color = TextPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ArrowUpward, contentDescription = null, tint = glowColor, modifier = Modifier.size(10.dp))
                    Spacer(modifier = Modifier.width(1.dp))
                    Text("LIVE", color = glowColor, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
                Text(subtitle, color = TextDimGray, fontSize = 8.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// -------------------------------------------------------------
// ৩. বটম ট্যাব ২: এক্টিভ ওয়ার্কার্স (১, ২, ৩, ৭ দিনের ফিল্টার)
// -------------------------------------------------------------
@Composable
fun ActiveWorkersTabWithFilter(
    usersMap: Map<String, UserProfile>,
    onInspect: (String) -> Unit
) {
    var selectedDays by remember { mutableIntStateOf(7) }

    val activeWorkers = usersMap.values.filter { u ->
        u.paragraph_jobs?.values?.any { task ->
            (task.status == "Success" || task.status == "Pending") && isWithinDays(task.date, selectedDays)
        } == true
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Active Workers History", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))

        // ফিল্টার চিপস
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(1 to "Today", 2 to "2 Days", 3 to "3 Days", 7 to "7 Days").forEach { (days, label) ->
                Button(
                    onClick = { selectedDays = days },
                    modifier = Modifier.weight(1f).height(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedDays == days) NeonGreen else Color(0xFF1D202D)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(label, color = if (selectedDays == days) Color.Black else TextDimGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(activeWorkers) { user ->
                val taskCount = user.paragraph_jobs?.values?.count { isWithinDays(it.date, selectedDays) } ?: 0
                val totalEarned = user.paragraph_jobs?.values?.filter { it.status == "Success" && isWithinDays(it.date, selectedDays) }?.sumOf { it.amount } ?: 0.0

                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(user.name.ifEmpty { "Worker" }, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(user.phone, color = NeonGreen, fontSize = 11.sp)
                            Text("Tasks ($selectedDays Days): $taskCount | Earned: ৳${String.format("%.2f", totalEarned)}", color = NeonCyan, fontSize = 10.sp)
                        }
                        Button(onClick = { onInspect(user.phone) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633)), modifier = Modifier.height(28.dp)) {
                            Text("View Log", color = NeonCyan, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ৪. বটম ট্যাব ৩: রিপোর্টস (১, ২, ৩, ৭ দিনের ফিল্টার)
// -------------------------------------------------------------
@Composable
fun WorkReportsTabWithFilter(usersMap: Map<String, UserProfile>) {
    var selectedDays by remember { mutableIntStateOf(7) }

    val allTasks = usersMap.flatMap { (phone, u) ->
        u.paragraph_jobs?.values?.map { t -> Triple(u, phone, t) } ?: emptyList()
    }.filter { isWithinDays(it.third.date, selectedDays) }
        .sortedByDescending { it.third.date }

    val approvedCount = allTasks.count { it.third.status == "Success" }
    val rejectedCount = allTasks.count { it.third.status == "Rejected" }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Work Submissions Report", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
            Text("App: $approvedCount | Rej: $rejectedCount", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ফিল্টার চিপস
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(1 to "Today", 2 to "2 Days", 3 to "3 Days", 7 to "7 Days").forEach { (days, label) ->
                Button(
                    onClick = { selectedDays = days },
                    modifier = Modifier.weight(1f).height(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedDays == days) NeonGreen else Color(0xFF1D202D)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(label, color = if (selectedDays == days) Color.Black else TextDimGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allTasks) { (user, phone, task) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} ($phone)", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(task.status, color = if (task.status == "Success") NeonGreen else if (task.status == "Rejected") NeonRose else NeonYellow, fontSize = 10.5.sp, fontWeight = FontWeight.Black)
                        }
                        Text("Topic: \"${task.topic}\" | Reward: ৳${task.amount}", color = NeonCyan, fontSize = 10.sp)
                        Text("Date: ${task.date}", color = TextDimGray, fontSize = 9.sp)
                        if (task.admin_comment.isNotEmpty()) {
                            Text("Reason: ${task.admin_comment}", color = NeonRose, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ৫. বটম ট্যাব ৪: কমিশন ম্যানেজার
// -------------------------------------------------------------
@Composable
fun CommissionsManagerTab(
    usersMap: Map<String, UserProfile>,
    onDeductCommission: (String, Double) -> Unit
) {
    val usersWithCommission = usersMap.values.filter { it.commission > 0.0 }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Referral Commission Claims", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Text("View and settle claimed referral commissions", color = TextDimGray, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(8.dp))

        if (usersWithCommission.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No pending commissions to claim.", color = TextDimGray, fontSize = 12.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(usersWithCommission) { user ->
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(user.name.ifEmpty { "User" }, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Mobile: ${user.phone} | UID: ${user.uid}", color = NeonCyan, fontSize = 10.sp)
                                Text("Claimed Commission: ৳${String.format("%.2f", user.commission)}", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                            Button(
                                onClick = { onDeductCommission(user.phone, user.commission) },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Deduct / Settle", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ৬. লাইভ কমান্ড ডেক
// -------------------------------------------------------------
@Composable
fun WorkspaceDeckLive(
    pendingDeposits: Int,
    pendingWithdrawals: Int,
    pendingSendMoney: Int,
    pendingRecharges: Int,
    validVouchers: Int,
    pendingTyping: Int,
    unreadChats: Int,
    onCardClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.GridView, contentDescription = null, tint = GoldMetallicMain, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Command Workspace Deck", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 12.sp)
        }
        Text("Manage • Monitor • Grow", color = GoldMetallicDark, fontSize = 9.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(8.dp))

    val workspaceList = listOf(
        WorkspaceActionData("User Directory", "Manage users and balances.", "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000", NeonBlue, null, "directory"),
        WorkspaceActionData("Deposits", "Review add money requests.", "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000", NeonYellow, pendingDeposits, "deposits"),
        WorkspaceActionData("Withdrawals", "Pending cash out requests.", "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000", NeonGreen, pendingWithdrawals, "withdrawals"),
        WorkspaceActionData("Send Money Req", "Handle transfer requests.", "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000", NeonBlue, pendingSendMoney, "sendmoney"),
        WorkspaceActionData("Recharges", "Mobile recharge operations.", "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000", NeonYellow, pendingRecharges, "recharges"),
        WorkspaceActionData("Gift Vouchers", "Create secure 21-digit codes.", "https://img.icons8.com/?size=100&id=DA67d1tKQ9Pr&format=png&color=000000", NeonRose, validVouchers, "vouchers"),
        WorkspaceActionData("Typing Tasks", "Review submitted typing jobs.", "https://img.icons8.com/?size=100&id=oZAinaxvg8AD&format=png&color=000000", NeonPurple, pendingTyping, "typing"),
        WorkspaceActionData("Support Chat", "Live helpdesk control console.", "https://img.icons8.com/?size=100&id=RntMFwIniVlj&format=png&color=000000", NeonCyan, unreadChats, "chat"),
        WorkspaceActionData("Balance Reset", "Authorized reset controller.", "https://img.icons8.com/?size=100&id=ifMVi1WVk8u2&format=png&color=000000", NeonRose, null, "reset_balance"),
        WorkspaceActionData("System Settings", "Configure MFS & app URLs.", "https://img.icons8.com/?size=100&id=v39wEv8JU1aa&format=png&color=000000", NeonCyan, null, "settings")
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WorkspaceCardItemLive(modifier = Modifier.weight(1f), data = workspaceList[i], onClick = { onCardClick(workspaceList[i].modalId) })
                if (i + 1 < workspaceList.size) {
                    WorkspaceCardItemLive(modifier = Modifier.weight(1f), data = workspaceList[i + 1], onClick = { onCardClick(workspaceList[i + 1].modalId) })
                }
            }
        }
    }
}

@Composable
fun WorkspaceCardItemLive(modifier: Modifier = Modifier, data: WorkspaceActionData, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF151722), CardSurfaceBottom)), RoundedCornerShape(16.dp))
            .border(1.dp, Brush.verticalGradient(listOf(GoldMetallicMain.copy(0.4f), Color(0xFF232634))), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        if (data.badge != null && data.badge > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
                    .shadow(4.dp, CircleShape, spotColor = NeonRose)
                    .background(NeonRose, CircleShape)
                    .border(0.8.dp, Color.White.copy(0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${data.badge}", color = Color.White, fontSize = 8.5.sp, fontWeight = FontWeight.Black)
            }
        }

        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(data.glowColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                    .border(1.dp, data.glowColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(model = data.iconUrl, contentDescription = data.title, modifier = Modifier.size(36.dp), contentScale = ContentScale.Fit)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(data.title, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(data.desc, color = TextDimGray, fontSize = 8.sp, lineHeight = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .size(20.dp)
                    .background(Color(0xFF1B1E2B), CircleShape)
                    .border(0.8.dp, GoldMetallicMain.copy(0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(14.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// বেস ডায়ালগ
// -------------------------------------------------------------
@Composable
fun BaseCockpitDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f)
                .shadow(24.dp, RoundedCornerShape(20.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF161826), Color(0xFF090A0F))), RoundedCornerShape(20.dp))
                .border(1.4.dp, GoldMetallicMain.copy(0.4f), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(title, color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextDimGray, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

// -------------------------------------------------------------
// ৭. User Directory Modal (Block/Unblock & Custom Edit)
// -------------------------------------------------------------
@Composable
fun UserDirectoryModal(
    users: List<UserProfile>,
    onDismiss: () -> Unit,
    onInspect: (String) -> Unit,
    onToggleBlock: (String, Boolean) -> Unit,
    onModifyBalance: (String, Boolean, Double) -> Unit,
    onDelete: (String) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var modifyingPhone by remember { mutableStateOf<String?>(null) }
    var modifyAmountStr by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(true) }

    val filtered = users.filter { it.name.contains(searchQuery, true) || it.phone.contains(searchQuery) || it.uid.contains(searchQuery) }

    BaseCockpitDialog(title = "User Directory Accounts", onDismiss = onDismiss) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search name, mobile or UID...", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { user ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)),
                    border = BorderStroke(1.dp, if (user.blocked) NeonRose.copy(0.6f) else Color(0xFF262B3D)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(user.name.ifEmpty { "User" }, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(12.dp).clickable { onCopy(user.name, "নাম") })
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(user.phone, color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonGreen, modifier = Modifier.size(12.dp).clickable { onCopy(user.phone, "ফোন নম্বর") })
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("UID: ${user.uid} | Pass: ${user.password}", color = TextDimGray, fontSize = 10.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = GoldMetallicLight, modifier = Modifier.size(12.dp).clickable { onCopy(user.uid, "UID") })
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("৳${String.format("%.2f", user.balance)}", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                Button(
                                    onClick = { onToggleBlock(user.phone, !user.blocked) },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (user.blocked) Color(0xFF4D1414) else Color(0xFF004D26)),
                                    modifier = Modifier.height(26.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text(if (user.blocked) "BLOCKED ✕" else "ACTIVE ✓", fontSize = 8.5.sp, color = if (user.blocked) NeonRose else NeonGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Button(onClick = { modifyingPhone = user.phone; isAdding = true }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3320))) {
                                Text("+ Edit", fontSize = 9.sp, color = NeonGreen)
                            }
                            Button(onClick = { modifyingPhone = user.phone; isAdding = false }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33200D))) {
                                Text("- Edit", fontSize = 9.sp, color = NeonYellow)
                            }
                            Button(onClick = { onInspect(user.phone) }, modifier = Modifier.weight(1.2f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633))) {
                                Text("Inspect", fontSize = 9.sp, color = NeonCyan)
                            }
                            Button(onClick = { onDelete(user.phone) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF330D0D))) {
                                Text("Delete", fontSize = 9.sp, color = NeonRose)
                            }
                        }
                    }
                }
            }
        }
    }

    modifyingPhone?.let { phone ->
        AlertDialog(
            onDismissRequest = { modifyingPhone = null },
            title = { Text(if (isAdding) "Add Balance to $phone" else "Subtract Balance from $phone", color = GoldMetallicLight, fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = modifyAmountStr,
                    onValueChange = { modifyAmountStr = it },
                    label = { Text("টাকার পরিমাণ লিখুন (৳)") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = modifyAmountStr.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            onModifyBalance(phone, isAdding, amt)
                            modifyingPhone = null
                            modifyAmountStr = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isAdding) NeonGreen else NeonYellow)
                ) {
                    Text(if (isAdding) "Add Money" else "Deduct Money", color = Color.Black)
                }
            },
            dismissButton = {
                Button(onClick = { modifyingPhone = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D202D))) {
                    Text("Cancel", color = TextDimGray)
                }
            },
            containerColor = Color(0xFF161824)
        )
    }
}

// -------------------------------------------------------------
// ৮. Inspect User Modal
// -------------------------------------------------------------
@Composable
fun InspectUserModal(
    user: UserProfile,
    allUsers: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onCopy: (String, String) -> Unit
) {
    val referrer = allUsers.values.find { it.uid == user.referred_by }
    val myReferrals = allUsers.values.filter { it.referred_by == user.uid }
    val receivedList = user.sendmoney?.values?.toList() ?: emptyList()
    val lastDeposits = user.deposits?.values?.toList()?.takeLast(3) ?: emptyList()
    val lastWithdrawals = user.withdrawals?.values?.toList()?.takeLast(3) ?: emptyList()

    BaseCockpitDialog(title = "User Deep-Dive & Records", onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(user.name.ifEmpty { "User" }, color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(13.dp).clickable { onCopy(user.name, "নাম") })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Phone: ${user.phone} | UID: ${user.uid}", color = NeonGreen, fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonGreen, modifier = Modifier.size(13.dp).clickable { onCopy(user.phone, "ফোন নম্বর") })
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = GoldMetallicLight, modifier = Modifier.size(13.dp).clickable { onCopy(user.uid, "UID") })
                    }
                    Text("Joined: ${user.registration_date.ifEmpty { "N/A" }}", color = TextDimGray, fontSize = 10.sp)
                    Text("Balance: ৳${String.format("%.2f", user.balance)}", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Linked Payout Wallet: ${user.wallet?.co_wallet ?: "None"}", color = TextPureWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Number: ${user.wallet?.co_number ?: "---"} (PIN: ${user.wallet?.co_pin ?: "---"})", color = TextDimGray, fontSize = 10.sp)
                        user.wallet?.co_number?.let { num ->
                            if (num.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(12.dp).clickable { onCopy(num, "ওয়ালেট নম্বর") })
                            }
                        }
                    }
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Referred By: ${referrer?.name ?: "Direct"} (${referrer?.phone ?: "UID: " + user.referred_by})", color = NeonCyan, fontSize = 11.sp)
                    Text("Total Referred Members: ${myReferrals.size} users", color = NeonPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Received Send Money History", color = GoldMetallicLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    if (receivedList.isEmpty()) {
                        Text("No send money received yet.", color = TextDimGray, fontSize = 9.5.sp)
                    } else {
                        for (sm in receivedList) {
                            Text("• ৳${sm.amount} from ${sm.senderName} (${sm.sender}) on ${sm.date}", color = NeonGreen, fontSize = 9.5.sp)
                        }
                    }
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Transaction Logs", color = GoldMetallicLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    for (d in lastDeposits) {
                        Text("• Deposit: ৳${d.amount} via ${d.wallet} (${d.status}) - TxID: ${d.txid}", color = NeonCyan, fontSize = 9.5.sp)
                    }
                    for (w in lastWithdrawals) {
                        Text("• Cash Out: ৳${w.amount} via ${w.wallet} (${w.status}) on ${w.date}", color = NeonRose, fontSize = 9.5.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ৯. Deposits Manager Modal (কারণ লেখার বক্সসহ)
// -------------------------------------------------------------
@Composable
fun DepositsManagerModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onApprove: (String, String, Double) -> Unit,
    onRejectWithReasonPrompt: (String, String) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }

    BaseCockpitDialog(title = "Add Money Request Manager", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp)
            }
        }

        val allDeposits = users.flatMap { (phone, u) ->
            u.deposits?.values?.map { dep -> Pair(u, dep) } ?: emptyList()
        }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allDeposits) { (user, dep) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("৳${dep.amount}", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Gateway: ${dep.wallet} | Sender: ${dep.sender}", color = NeonCyan, fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(11.dp).clickable { onCopy(dep.sender, "প্রেরক নম্বর") })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("TxID: ${dep.txid}", color = TextDimGray, fontSize = 9.5.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = GoldMetallicLight, modifier = Modifier.size(11.dp).clickable { onCopy(dep.txid, "TxID") })
                        }
                        if (dep.admin_comment.isNotEmpty()) {
                            Text("Reason: ${dep.admin_comment}", color = NeonRose, fontSize = 9.sp)
                        }

                        if (dep.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onApprove(user.phone, dep.id, dep.amount) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onRejectWithReasonPrompt(user.phone, dep.id) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ১০. Withdrawals Manager Modal (কারণ লেখার বক্সসহ)
// -------------------------------------------------------------
@Composable
fun WithdrawalsManagerModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onInspect: (String) -> Unit,
    onApprove: (String, String) -> Unit,
    onRejectWithReasonPrompt: (String, String, Double) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }

    BaseCockpitDialog(title = "Cash Out Request Manager", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp)
            }
        }

        val allWds = users.flatMap { (phone, u) ->
            u.withdrawals?.values?.map { wd -> Pair(u, wd) } ?: emptyList()
        }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allWds) { (user, wd) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("-৳${wd.amount}", color = NeonRose, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Payout Wallet: ${wd.wallet} - Number: ${user.wallet?.co_number ?: "---"}", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            user.wallet?.co_number?.let { num ->
                                if (num.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(13.dp).clickable { onCopy(num, "উইথড্র নম্বর") })
                                }
                            }
                        }
                        Text("PIN: ${user.wallet?.co_pin ?: "---"} | Date: ${wd.date}", color = TextDimGray, fontSize = 9.5.sp)
                        if (wd.admin_comment.isNotEmpty()) {
                            Text("Reason: ${wd.admin_comment}", color = NeonRose, fontSize = 9.sp)
                        }

                        if (wd.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onInspect(user.phone) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633)), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Inspect", color = NeonCyan, fontSize = 10.sp)
                                }
                                Button(onClick = { onApprove(user.phone, wd.id) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onRejectWithReasonPrompt(user.phone, wd.id, wd.totalDeducted) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ১১. Gift Vouchers Modal (বড় ভিউ ও ডিলিট অপশন)
// -------------------------------------------------------------
@Composable
fun GiftVouchersModal(
    vouchers: List<GiftVoucher>,
    allUsers: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onGenerate: (String, String, Double) -> Unit,
    onDeleteVoucher: (String) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Active") }
    var claimerUid by remember { mutableStateOf("") }
    var referredUid by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("25") }
    var verificationMsg by remember { mutableStateOf("") }

    BaseCockpitDialog(title = "Gift Voucher Generator & Logs", onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            // Generator Section
            OutlinedTextField(value = claimerUid, onValueChange = { claimerUid = it }, label = { Text("Claimer UID") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = referredUid, onValueChange = { referredUid = it }, label = { Text("Referred UID") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = amountStr, onValueChange = { amountStr = it }, label = { Text("Bonus (৳)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = {
                        val claimer = allUsers.values.find { it.uid == claimerUid }
                        val referred = allUsers.values.find { it.uid == referredUid }
                        if (claimer == null || referred == null) {
                            verificationMsg = "UID নট ফাউন্ড!"
                        } else if (referred.referred_by != claimerUid) {
                            verificationMsg = "✗ MISMATCH (রেফারেল মিল নেই!)"
                        } else {
                            val depSum = referred.deposits?.values?.filter { it.status == "Success" }?.sumOf { it.amount } ?: 0.0
                            val feePaid = depSum >= 40.0 || referred.verification_fee_paid || referred.active
                            verificationMsg = "Claimer: ${claimer.name} | Ref: ${referred.name}\n" + (if (feePaid) "✓ VERIFIED (৪০৳ পেইড!)" else "✗ ৪০৳ ফি পাওয়া যায়নি!")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D332B))
                ) {
                    Text("যাচাই করুন", color = NeonCyan, fontSize = 10.sp)
                }

                Button(
                    onClick = {
                        val amt = amountStr.toDoubleOrNull() ?: 25.0
                        if (claimerUid.length == 6 && referredUid.length == 6) {
                            onGenerate(claimerUid, referredUid, amt)
                        }
                    },
                    modifier = Modifier.weight(1.2f),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldMetallicMain)
                ) {
                    Text("কোড তৈরি করুন", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (verificationMsg.isNotEmpty()) {
                Text(verificationMsg, color = if (verificationMsg.contains("✓")) NeonGreen else NeonRose, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { activeTab = "Active" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Active") NeonGreen else Color(0xFF1D202D))) {
                    Text("Active / Unclaimed", color = if (activeTab == "Active") Color.Black else TextDimGray, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = { activeTab = "Claimed" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Claimed") NeonGreen else Color(0xFF1D202D))) {
                    Text("Claimed History", color = if (activeTab == "Claimed") Color.Black else TextDimGray, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val filtered = vouchers.filter { if (activeTab == "Active") it.status == "active" else it.status != "active" }

            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(filtered) { v ->
                    val claimer = allUsers.values.find { it.uid == v.targetUid }
                    val referred = allUsers.values.find { it.uid == v.referredUid }

                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926))) {
                        Row(modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(v.code, color = NeonYellow, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                Text("Claimer: ${claimer?.name ?: "User"} (UID: ${v.targetUid})", color = TextPureWhite, fontSize = 9.5.sp)
                                Text("Referred: ${referred?.name ?: "N/A"} (UID: ${v.referredUid}) | Bonus: ৳${v.amount}", color = TextDimGray, fontSize = 9.sp)
                                if (v.status != "active") {
                                    Text("Claimed on: ${v.claimedDate.ifEmpty { v.date }}", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Button(onClick = { onCopy(v.code, "ভাউচার কোড") }, modifier = Modifier.height(26.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D202D))) {
                                    Text("Copy", color = NeonCyan, fontSize = 8.sp)
                                }
                                IconButton(onClick = { onDeleteVoucher(v.code) }, modifier = Modifier.size(26.dp)) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = NeonRose, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ১২. WhatsApp Style Support Chat Modal (বড় ভিউ)
// -------------------------------------------------------------
@Composable
fun WhatsAppStyleSupportChatModal(
    chats: Map<String, List<ChatMessage>>,
    allUsers: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onSendReply: (String, String) -> Unit,
    onMarkSeen: (String, String) -> Unit,
    onClearChat: (String) -> Unit
) {
    var selectedPhone by remember { mutableStateOf<String?>(chats.keys.firstOrNull()) }
    var replyText by remember { mutableStateOf("") }

    BaseCockpitDialog(title = "Live Support Helpdesk", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            // User List (Name & UID)
            LazyColumn(modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, Color(0xFF262B3D))) {
                items(chats.keys.toList()) { phone ->
                    val user = allUsers[phone]
                    val unread = chats[phone]?.count { it.sender == "user" && !it.seen } ?: 0
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (selectedPhone == phone) Color(0xFF0D332B) else Color.Transparent)
                            .clickable { selectedPhone = phone }
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(user?.name?.ifEmpty { "User" } ?: phone, color = TextPureWhite, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            Text("UID: ${user?.uid ?: "---"}", color = NeonCyan, fontSize = 9.sp)
                            if (unread > 0) {
                                Text("$unread New Msg", color = NeonRose, fontSize = 8.5.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // WhatsApp Style Large Chat Box
            selectedPhone?.let { phone ->
                val user = allUsers[phone]
                val msgList = chats[phone] ?: emptyList()

                Column(modifier = Modifier.weight(2.4f).fillMaxHeight()) {
                    // Chat Header
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFF161926), RoundedCornerShape(8.dp)).padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(user?.name?.ifEmpty { "Customer" } ?: phone, color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("UID: ${user?.uid ?: "---"} (${phone})", color = TextDimGray, fontSize = 9.sp)
                        }
                        IconButton(onClick = { onClearChat(phone) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Filled.Delete, contentDescription = "Clear", tint = NeonRose, modifier = Modifier.size(16.dp))
                        }
                    }

                    // Message Bubbles
                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(msgList) { msg ->
                            if (msg.sender == "user" && !msg.seen) {
                                onMarkSeen(phone, msg.id)
                            }
                            val isUser = msg.sender == "user"
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (isUser) Alignment.CenterStart else Alignment.CenterEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isUser) Color(0xFF1E2433) else Color(0xFF005C4B),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(msg.text, color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Large Input Area
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = replyText,
                            onValueChange = { replyText = it },
                            placeholder = { Text("Type reply...", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f).height(48.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = {
                                if (replyText.isNotEmpty()) {
                                    onSendReply(phone, replyText)
                                    replyText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.Black, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// বাকি সাপোর্টিং মডালস (SendMoney, Recharges, Typing, Reset, Settings)
// -------------------------------------------------------------
@Composable
fun SendMoneyManagerModal(
    requests: List<SendMoneyRequest>,
    onDismiss: () -> Unit,
    onApprove: (SendMoneyRequest) -> Unit,
    onRejectWithReasonPrompt: (SendMoneyRequest) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }
    val filtered = requests.filter { if (activeTab == "Pending") it.status == "Pending" else it.status != "Pending" }

    BaseCockpitDialog(title = "Send Money Requests", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { req ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Sender: ${req.senderName} (${req.sender})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Receiver: ${req.targetName} (${req.target})", color = NeonCyan, fontSize = 11.sp)
                        Text("Amount: ৳${req.amount} | Fee: ৳${req.fee} | Total: ৳${req.totalDeducted}", color = GoldMetallicLight, fontSize = 10.sp)
                        if (req.admin_comment.isNotEmpty()) {
                            Text("Reason: ${req.admin_comment}", color = NeonRose, fontSize = 9.sp)
                        }

                        if (req.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onApprove(req) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onRejectWithReasonPrompt(req) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RechargesManagerModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onApprove: (String, String) -> Unit,
    onRejectWithReasonPrompt: (String, String, Double) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }
    val allRc = users.flatMap { (phone, u) ->
        u.recharges?.values?.map { rc -> Pair(u, rc) } ?: emptyList()
    }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

    BaseCockpitDialog(title = "Mobile Recharge Operations", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allRc) { (user, rc) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("৳${rc.amount}", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Operator: ${rc.operator} (${rc.type}) - ${rc.number}", color = NeonCyan, fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(11.dp).clickable { onCopy(rc.number, "রিচার্জ নম্বর") })
                        }
                        if (rc.admin_comment.isNotEmpty()) {
                            Text("Reason: ${rc.admin_comment}", color = NeonRose, fontSize = 9.sp)
                        }

                        if (rc.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onApprove(user.phone, rc.id) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onRejectWithReasonPrompt(user.phone, rc.id, rc.amount) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TypingTasksModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onReadWork: (String) -> Unit,
    onApprove: (String, String, Double, Double) -> Unit,
    onRejectWithReasonPrompt: (String, String, Double) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }
    val allTasks = users.flatMap { (phone, u) ->
        u.paragraph_jobs?.values?.map { t -> Pair(u, t) } ?: emptyList()
    }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

    BaseCockpitDialog(title = "Typing Tasks Checker", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allTasks) { (user, task) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Reward: ৳${task.amount}", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Text("Topic: \"${task.topic}\" | Fee: ৳${task.entry_fee}", color = NeonCyan, fontSize = 10.sp)
                        if (task.admin_comment.isNotEmpty()) {
                            Text("Reason: ${task.admin_comment}", color = NeonRose, fontSize = 9.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(onClick = { onReadWork(task.text) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633))) {
                                Text("Read Work", color = NeonCyan, fontSize = 10.sp)
                            }
                            if (task.status == "Pending") {
                                Button(onClick = { onApprove(user.phone, task.id, task.amount, task.entry_fee) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onRejectWithReasonPrompt(user.phone, task.id, task.entry_fee) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonRose)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceResetModal(
    allUsers: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onResetSingle: (String) -> Unit,
    onResetAll: () -> Unit
) {
    var searchInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    BaseCockpitDialog(title = "Balance Reset Controller", onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Text("১. নির্দিষ্ট ইউজারের ব্যালেন্স ৳০.০০ করুন", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            OutlinedTextField(value = searchInput, onValueChange = { searchInput = it }, label = { Text("ফোন নম্বর বা UID") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    val user = allUsers[searchInput] ?: allUsers.values.find { it.uid == searchInput }
                    if (user != null) onResetSingle(user.phone)
                    else Toast.makeText(context, "ইউজার পাওয়া যায়নি!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF663300))
            ) {
                Text("ব্যালেন্স ০.০০ করুন", color = GoldMetallicLight, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = Color(0xFF262B3D))
            Spacer(modifier = Modifier.height(10.dp))

            Text("২. সকল ইউজারের ব্যালেন্স একসাথে ৳০.০০ করুন", color = NeonRose, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onResetAll,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
            ) {
                Text("সকলের ব্যালেন্স একসাথে ৳০.০০ করুন", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun SystemSettingsModal(
    db: FirebaseDatabase,
    onDismiss: () -> Unit
) {
    var bkashNum by remember { mutableStateOf("") }
    var bkashLbl by remember { mutableStateOf("") }
    var nagadNum by remember { mutableStateOf("") }
    var nagadLbl by remember { mutableStateOf("") }
    var tgLink by remember { mutableStateOf("") }
    var apkLink by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        db.getReference("admin_settings").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bkashNum = snapshot.child("deposit_targets/bKash/number").getValue(String::class.java) ?: ""
                bkashLbl = snapshot.child("deposit_targets/bKash/label").getValue(String::class.java) ?: ""
                nagadNum = snapshot.child("deposit_targets/Nagad/number").getValue(String::class.java) ?: ""
                nagadLbl = snapshot.child("deposit_targets/Nagad/label").getValue(String::class.java) ?: ""
                tgLink = snapshot.child("links/telegram").getValue(String::class.java) ?: ""
                apkLink = snapshot.child("links/apk").getValue(String::class.java) ?: ""
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    BaseCockpitDialog(title = "System Gateways & Settings", onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())) {
            Text("MFS Gateways", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            OutlinedTextField(value = bkashNum, onValueChange = { bkashNum = it }, label = { Text("bKash Personal No.") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = bkashLbl, onValueChange = { bkashLbl = it }, label = { Text("bKash Label") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = nagadNum, onValueChange = { nagadNum = it }, label = { Text("Nagad Personal No.") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = nagadLbl, onValueChange = { nagadLbl = it }, label = { Text("Nagad Label") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Spacer(modifier = Modifier.height(10.dp))
            Text("Links", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            OutlinedTextField(value = tgLink, onValueChange = { tgLink = it }, label = { Text("Telegram Link") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = apkLink, onValueChange = { apkLink = it }, label = { Text("APK Download Link") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    db.getReference("admin_settings/deposit_targets/bKash").setValue(mapOf("number" to bkashNum, "label" to bkashLbl))
                    db.getReference("admin_settings/deposit_targets/Nagad").setValue(mapOf("number" to nagadNum, "label" to nagadLbl))
                    db.getReference("admin_settings/links").setValue(mapOf("telegram" to tgLink, "apk" to apkLink))
                    Toast.makeText(context, "সেটিংস সংরক্ষিত হয়েছে!", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Save Settings", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// ১৩. বটম বার (Home, Users, Reports, Commission)
// -------------------------------------------------------------
@Composable
fun CockpitLuxuryBottomNav(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    val items = listOf(
        "Home" to Icons.Rounded.Home,
        "Users" to Icons.Rounded.Groups,
        "Reports" to Icons.Rounded.BarChart,
        "Commission" to Icons.Rounded.MonetizationOn
    )

    NavigationBar(
        containerColor = Color(0xFF08090D),
        tonalElevation = 8.dp,
        modifier = Modifier
            .height(60.dp)
            .border(1.2.dp, GoldMetallicMain.copy(0.3f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selected == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(index) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 32.dp else 24.dp)
                            .background(if (isSelected) GoldMetallicMain.copy(0.2f) else Color.Transparent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(item.second, contentDescription = item.first, tint = if (isSelected) GoldMetallicLight else TextDimGray, modifier = Modifier.size(18.dp))
                    }
                },
                label = {
                    Text(
                        item.first,
                        color = if (isSelected) GoldMetallicLight else TextDimGray,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun WelcomeCardCompact() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF171A24), RoundedCornerShape(18.dp))
            .border(1.4.dp, Brush.horizontalGradient(listOf(GoldMetallicMain, Color(0xFF553C07), GoldMetallicMain)), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).background(GoldMetallicMain.copy(0.2f), CircleShape).border(1.2.dp, GoldMetallicMain, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextDimGray, fontSize = 10.sp)
                    Text("Admin", color = GoldMetallicLight, fontSize = 15.sp, fontWeight = FontWeight.Black)
                    Text("Have a great day!", color = TextDimGray, fontSize = 9.sp)
                }
            }

            Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFF2E3244)))

            Column(horizontalAlignment = Alignment.End) {
                Text("Database Status", color = TextPureWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Box(modifier = Modifier.background(Color(0xFF00381C), RoundedCornerShape(16.dp)).border(1.dp, NeonGreen, RoundedCornerShape(16.dp)).padding(horizontal = 7.dp, vertical = 2.dp)) {
                    Text("Connected", color = NeonGreen, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("typing-5c3e4-default-rtdb", color = TextDimGray, fontSize = 8.sp)
            }
        }
  // --- ব্যালেন্স রিসেট ও রিস্টোর বাটন ---
        val context = androidx.compose.ui.platform.LocalContext.current
        
        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            // ১. লাল রঙের ব্যালেন্স ০ করার বাটন
            androidx.compose.material3.Button(
                onClick = { resetAllBalancesWithBackup(context) },
                modifier = androidx.compose.ui.Modifier.weight(1f),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFE53935)
                )
            ) {
                androidx.compose.material3.Text("🗑️ Reset All", color = androidx.compose.ui.graphics.Color.White)
            }

            // ২. সবুজ রঙের ব্যালেন্স ফিরিয়ে দেওয়ার বাটন
            androidx.compose.material3.Button(
                onClick = { restoreAllBalances(context) },
                modifier = androidx.compose.ui.Modifier.weight(1f),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF43A047)
                )
            ) {
                androidx.compose.material3.Text("🔄 Restore All", color = androidx.compose.ui.graphics.Color.White)
            }
        }
    }
}
// ১. ব্যালেন্সের ব্যাকআপ রেখে ০ করার ফাংশন
fun resetAllBalancesWithBackup(context: android.content.Context) {
    val db = com.google.firebase.database.FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com")
    val usersRef = db.getReference("users")
    val backupRef = db.getReference("last_balance_backup")

    usersRef.addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
            val backupData = mutableMapOf<String, Any>()
            val updates = mutableMapOf<String, Any>()

            for (userSnap in snapshot.children) {
                val phone = userSnap.key ?: continue
                val currentBalance = userSnap.child("balance").value ?: 0
                backupData[phone] = currentBalance
                updates["$phone/balance"] = 0
            }

            backupRef.setValue(backupData).addOnSuccessListener {
                usersRef.updateChildren(updates).addOnSuccessListener {
                    android.widget.Toast.makeText(context, "সব ব্যালেন্স ০ করা হয়েছে এবং ব্যাকআপ রাখা হয়েছে!", android.widget.Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                android.widget.Toast.makeText(context, "ব্যালেন্স রিসেট ব্যর্থ হয়েছে!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
    })
}

// ২. আগের ব্যালেন্স ফিরিয়ে দেওয়ার ফাংশন (Restore)
fun restoreAllBalances(context: android.content.Context) {
    val db = com.google.firebase.database.FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com")
    val backupRef = db.getReference("last_balance_backup")
    val usersRef = db.getReference("users")

    backupRef.addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
            if (!snapshot.exists() || !snapshot.hasChildren()) {
                android.widget.Toast.makeText(context, "ফেরত দেওয়ার মতো কোনো ব্যাকআপ পাওয়া যায়নি!", android.widget.Toast.LENGTH_LONG).show()
                return
            }

            val restoreUpdates = mutableMapOf<String, Any>()
            for (backupSnap in snapshot.children) {
                val phone = backupSnap.key ?: continue
                val previousBalance = backupSnap.value ?: 0
                restoreUpdates["$phone/balance"] = previousBalance
            }

            usersRef.updateChildren(restoreUpdates).addOnSuccessListener {
                android.widget.Toast.makeText(context, "সফলভাবে সবার ব্যালেন্স ফিরিয়ে দেওয়া হয়েছে! 🎉", android.widget.Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                android.widget.Toast.makeText(context, "ব্যালেন্স ফিরিয়ে দিতে সমস্যা হয়েছে!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
    })
}
