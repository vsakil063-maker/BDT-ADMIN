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
import androidx.compose.ui.graphics.Path
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
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

// ==========================================
// 🎨 থিম কালার প্যালেট
// ==========================================
val DarkCanvasBg = Color(0xFF07080B)
val CardSurfaceTop = Color(0xFF131520)
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

// ==========================================
// 🌐 ফায়ারবেস ডেটা মডেল
// ==========================================
data class UserProfile(
    val phone: String = "",
    val name: String = "",
    val uid: String = "",
    val password: String = "",
    val otp_code: String = "",
    val balance: Double = 0.0,
    val active: Boolean = false,
    val referred_by: String = "",
    val registration_date: String = "",
    val wallet: UserWallet? = null,
    val deposits: Map<String, TransactionItem>? = null,
    val withdrawals: Map<String, TransactionItem>? = null,
    val recharges: Map<String, RechargeItem>? = null,
    val paragraph_jobs: Map<String, TaskItem>? = null,
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
    val totalDeducted: Double = 0.0
)

data class RechargeItem(
    val id: String = "",
    val amount: Double = 0.0,
    val operator: String = "",
    val type: String = "",
    val number: String = "",
    val date: String = "",
    val status: String = "Pending"
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
    val status: String = "Pending"
)

data class GiftVoucher(
    val code: String = "",
    val targetUid: String = "",
    val referredUid: String = "",
    val amount: Double = 0.0,
    val date: String = "",
    val status: String = "active"
)

data class ChatMessage(
    val id: String = "",
    val sender: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val seen: Boolean = false
)

// ==========================================
// 📱 মেইন ড্যাশবোর্ড স্ক্রিন
// ==========================================
@Composable
fun CockpitDashboardScreen() {
    val context = LocalContext.current
    
    // Firebase Realtime Database Instance
    val db = remember {
        FirebaseDatabase.getInstance("https://typing-5c3e4-default-rtdb.firebaseio.com")
    }

    // State Variables
    var usersMap by remember { mutableStateOf<Map<String, UserProfile>>(emptyMap()) }
    var sendMoneyList by remember { mutableStateOf<List<SendMoneyRequest>>(emptyList()) }
    var giftVouchersList by remember { mutableStateOf<List<GiftVoucher>>(emptyList()) }
    var chatsMap by remember { mutableStateOf<Map<String, List<ChatMessage>>>(emptyMap()) }
    var activeModalId by remember { mutableStateOf<String?>(null) }
    var inspectingPhone by remember { mutableStateOf<String?>(null) }
    var inspectingTaskText by remember { mutableStateOf<String?>(null) }

    // Realtime Listeners
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
                    val balance = child.child("balance").getValue(Double::class.java) ?: child.child("balance").getValue(Long::class.java)?.toDouble() ?: 0.0
                    val active = child.child("active").getValue(Boolean::class.java) ?: (child.child("active").getValue(String::class.java) == "true")
                    val refBy = child.child("referred_by").getValue(Any::class.java)?.toString() ?: ""
                    val regDate = child.child("registration_date").getValue(String::class.java) ?: child.child("date").getValue(String::class.java) ?: ""

                    // Wallet
                    val wSnap = child.child("wallet")
                    val wallet = if (wSnap.exists()) {
                        UserWallet(
                            co_wallet = wSnap.child("co_wallet").getValue(String::class.java) ?: "",
                            co_number = wSnap.child("co_number").getValue(String::class.java) ?: "",
                            co_pin = wSnap.child("co_pin").getValue(String::class.java) ?: ""
                        )
                    } else null

                    // Deposits
                    val depMap = mutableMapOf<String, TransactionItem>()
                    for (d in child.child("deposits").children) {
                        val dId = d.key ?: ""
                        val amt = d.child("amount").getValue(Double::class.java) ?: d.child("amount").getValue(Long::class.java)?.toDouble() ?: 0.0
                        depMap[dId] = TransactionItem(
                            id = dId,
                            amount = amt,
                            wallet = d.child("wallet").getValue(String::class.java) ?: "",
                            sender = d.child("sender").getValue(String::class.java) ?: "",
                            txid = d.child("txid").getValue(String::class.java) ?: "",
                            date = d.child("date").getValue(String::class.java) ?: "",
                            status = d.child("status").getValue(String::class.java) ?: "Pending"
                        )
                    }

                    // Withdrawals
                    val wdMap = mutableMapOf<String, TransactionItem>()
                    for (w in child.child("withdrawals").children) {
                        val wId = w.key ?: ""
                        val amt = w.child("amount").getValue(Double::class.java) ?: w.child("amount").getValue(Long::class.java)?.toDouble() ?: 0.0
                        val totDed = w.child("totalDeducted").getValue(Double::class.java) ?: amt
                        wdMap[wId] = TransactionItem(
                            id = wId,
                            amount = amt,
                            wallet = w.child("wallet").getValue(String::class.java) ?: "",
                            date = w.child("date").getValue(String::class.java) ?: "",
                            status = w.child("status").getValue(String::class.java) ?: "Pending",
                            totalDeducted = totDed
                        )
                    }

                    // Recharges
                    val rcMap = mutableMapOf<String, RechargeItem>()
                    for (r in child.child("recharges").children) {
                        val rId = r.key ?: ""
                        val amt = r.child("amount").getValue(Double::class.java) ?: r.child("amount").getValue(Long::class.java)?.toDouble() ?: 0.0
                        rcMap[rId] = RechargeItem(
                            id = rId,
                            amount = amt,
                            operator = r.child("operator").getValue(String::class.java) ?: "",
                            type = r.child("type").getValue(String::class.java) ?: "",
                            number = r.child("number").getValue(String::class.java) ?: "",
                            date = r.child("date").getValue(String::class.java) ?: "",
                            status = r.child("status").getValue(String::class.java) ?: "Pending"
                        )
                    }

                    // Tasks
                    val ptMap = mutableMapOf<String, TaskItem>()
                    for (p in child.child("paragraph_jobs").children) {
                        val pId = p.key ?: ""
                        val amt = p.child("amount").getValue(Double::class.java) ?: p.child("amount").getValue(Long::class.java)?.toDouble() ?: 0.0
                        val fee = p.child("entry_fee").getValue(Double::class.java) ?: 0.0
                        ptMap[pId] = TaskItem(
                            id = pId,
                            type = p.child("type").getValue(String::class.java) ?: "Task",
                            lang = p.child("lang").getValue(String::class.java) ?: "General",
                            topic = p.child("topic").getValue(String::class.java) ?: "",
                            text = p.child("text").getValue(String::class.java) ?: "",
                            amount = amt,
                            entry_fee = fee,
                            date = p.child("date").getValue(String::class.java) ?: "",
                            status = p.child("status").getValue(String::class.java) ?: "Pending",
                            admin_comment = p.child("admin_comment").getValue(String::class.java) ?: ""
                        )
                    }

                    map[phone] = UserProfile(
                        phone = phone, name = name, uid = uid, password = password,
                        otp_code = otp, balance = balance, active = active,
                        referred_by = refBy, registration_date = regDate, wallet = wallet,
                        deposits = depMap, withdrawals = wdMap, recharges = rcMap, paragraph_jobs = ptMap
                    )
                }
                usersMap = map
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        usersRef.addValueEventListener(usersListener)

        // Send Money Listener
        val sendMoneyListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<SendMoneyRequest>()
                for (child in snapshot.children) {
                    val id = child.key ?: ""
                    list.add(
                        SendMoneyRequest(
                            id = id,
                            sender = child.child("sender").getValue(String::class.java) ?: "",
                            senderName = child.child("senderName").getValue(String::class.java) ?: "",
                            target = child.child("target").getValue(String::class.java) ?: "",
                            targetName = child.child("targetName").getValue(String::class.java) ?: "",
                            amount = child.child("amount").getValue(Double::class.java) ?: 0.0,
                            fee = child.child("fee").getValue(Double::class.java) ?: 0.0,
                            totalDeducted = child.child("totalDeducted").getValue(Double::class.java) ?: 0.0,
                            date = child.child("date").getValue(String::class.java) ?: "",
                            status = child.child("status").getValue(String::class.java) ?: "Pending"
                        )
                    )
                }
                sendMoneyList = list
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        sendMoneyRef.addValueEventListener(sendMoneyListener)

        // Vouchers Listener
        val vouchersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<GiftVoucher>()
                for (child in snapshot.children) {
                    val code = child.key ?: ""
                    list.add(
                        GiftVoucher(
                            code = code,
                            targetUid = child.child("targetUid").getValue(String::class.java) ?: child.child("uid").getValue(String::class.java) ?: "",
                            referredUid = child.child("referredUid").getValue(String::class.java) ?: "",
                            amount = child.child("amount").getValue(Double::class.java) ?: 0.0,
                            date = child.child("date").getValue(String::class.java) ?: "",
                            status = child.child("status").getValue(String::class.java) ?: "active"
                        )
                    )
                }
                giftVouchersList = list
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        vouchersRef.addValueEventListener(vouchersListener)

        // Chat Listener
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

    // লাইভ ক্যালকুলেশন
    val todayDateStr = remember { SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Date()) }
    
    val totalUsers = usersMap.size
    var totalDeposit = 0.0
    var todayDeposit = 0.0
    var totalWithdraw = 0.0
    var todayWithdraw = 0.0
    var totalWorkDone = 0.0
    var totalUserProfits = 0.0
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
                totalUserProfits += p.amount
                if (p.date.contains(todayDateStr)) totalWorkDone += p.amount
            }
        }
    }

    val pendingSendMoneyCount = sendMoneyList.count { it.status == "Pending" }
    var unreadChatsCount = 0
    chatsMap.values.forEach { mList ->
        unreadChatsCount += mList.count { it.sender == "user" && !it.seen }
    }

    Scaffold(
        bottomBar = { CockpitLuxuryBottomNav() },
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            HeaderBarCompact()
            Spacer(modifier = Modifier.height(10.dp))
            WelcomeCardCompact()
            Spacer(modifier = Modifier.height(14.dp))

            // ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড
            OverviewStatsGridLive(
                totalUsers = totalUsers,
                totalDeposit = totalDeposit,
                todayDeposit = todayDeposit,
                totalWithdraw = totalWithdraw,
                todayWithdraw = todayWithdraw,
                totalWorkDone = totalWorkDone,
                totalUserProfits = totalUserProfits,
                totalVolume = totalVolume,
                onCardClick = { modalId -> activeModalId = modalId }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // কমান্ড ডেক
            WorkspaceDeckLive(
                pendingDeposits = pendingDepositsCount,
                pendingWithdrawals = pendingWithdrawalsCount,
                pendingSendMoney = pendingSendMoneyCount,
                pendingRecharges = pendingRechargesCount,
                validVouchers = giftVouchersList.size,
                pendingTyping = pendingTypingCount,
                unreadChats = unreadChatsCount,
                onCardClick = { modalId -> activeModalId = modalId }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // ১০টি ইন্টারেক্টিভ মডাল সিস্টেম
        // ==========================================
        when (activeModalId) {
            "directory" -> UserDirectoryModal(
                users = usersMap.values.toList(),
                onDismiss = { activeModalId = null },
                onInspect = { phone -> inspectingPhone = phone; activeModalId = "inspect" },
                onToggleActive = { phone, active -> db.getReference("users/$phone/active").setValue(active) },
                onModifyBalance = { phone, add, amt ->
                    val curr = usersMap[phone]?.balance ?: 0.0
                    val newBal = if (add) curr + amt else maxOf(0.0, curr - amt)
                    db.getReference("users/$phone/balance").setValue(newBal)
                },
                onResetBalance = { phone -> db.getReference("users/$phone/balance").setValue(0.0) },
                onDelete = { phone -> db.getReference("users/$phone").removeValue() }
            )

            "inspect" -> inspectingPhone?.let { phone ->
                usersMap[phone]?.let { user ->
                    InspectUserModal(
                        user = user,
                        allUsers = usersMap,
                        onDismiss = { activeModalId = "directory"; inspectingPhone = null }
                    )
                }
            }

            "deposits" -> DepositsManagerModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onApprove = { phone, id, amount ->
                    db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + amount)
                    db.getReference("users/$phone/deposits/$id/status").setValue("Success")
                },
                onReject = { phone, id ->
                    db.getReference("users/$phone/deposits/$id/status").setValue("Rejected")
                }
            )

            "withdrawals" -> WithdrawalsManagerModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onInspect = { phone -> inspectingPhone = phone; activeModalId = "inspect" },
                onApprove = { phone, id ->
                    db.getReference("users/$phone/withdrawals/$id/status").setValue("Success")
                },
                onReject = { phone, id, refundAmount ->
                    db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + refundAmount)
                    db.getReference("users/$phone/withdrawals/$id/status").setValue("Rejected")
                }
            )

            "sendmoney" -> SendMoneyManagerModal(
                requests = sendMoneyList,
                users = usersMap,
                onDismiss = { activeModalId = null },
                onApprove = { req ->
                    val targetBal = usersMap[req.target]?.balance ?: 0.0
                    db.getReference("users/${req.target}/balance").setValue(targetBal + req.amount)
                    db.getReference("pending_send_money/${req.id}/status").setValue("Success")
                    db.getReference("users/${req.sender}/sendmoney/${req.id}/status").setValue("Success")
                },
                onReject = { req ->
                    val senderBal = usersMap[req.sender]?.balance ?: 0.0
                    db.getReference("users/${req.sender}/balance").setValue(senderBal + req.totalDeducted)
                    db.getReference("pending_send_money/${req.id}/status").setValue("Rejected")
                    db.getReference("users/${req.sender}/sendmoney/${req.id}/status").setValue("Rejected")
                }
            )

            "recharges" -> RechargesManagerModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onApprove = { phone, id -> db.getReference("users/$phone/recharges/$id/status").setValue("Success") },
                onReject = { phone, id, amount ->
                    db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + amount)
                    db.getReference("users/$phone/recharges/$id/status").setValue("Rejected")
                }
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
                }
            )

            "typing" -> TypingTasksModal(
                users = usersMap,
                onDismiss = { activeModalId = null },
                onReadWork = { text -> inspectingTaskText = text },
                onApprove = { phone, id, reward, entryFee ->
                    val currBal = usersMap[phone]?.balance ?: 0.0
                    db.getReference("users/$phone/balance").setValue(currBal + reward)
                    db.getReference("users/$phone/paragraph_jobs/$id/status").setValue("Success")

                    // 5% Referrer Commission on Net Profit
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
                },
                onReject = { phone, id, entryFee, reason ->
                    if (entryFee > 0) {
                        db.getReference("users/$phone/balance").setValue((usersMap[phone]?.balance ?: 0.0) + entryFee)
                    }
                    db.getReference("users/$phone/paragraph_jobs/$id/status").setValue("Rejected")
                    db.getReference("users/$phone/paragraph_jobs/$id/admin_comment").setValue(reason)
                }
            )

            "chat" -> SupportChatModal(
                chats = chatsMap,
                onDismiss = { activeModalId = null },
                onSendReply = { phone, text ->
                    val msgMap = mapOf(
                        "sender" to "admin", "text" to text, "timestamp" to System.currentTimeMillis(), "seen" to true
                    )
                    db.getReference("chats/$phone/messages").push().setValue(msgMap)
                },
                onMarkSeen = { phone, msgId -> db.getReference("chats/$phone/messages/$msgId/seen").setValue(true) },
                onClearChat = { phone -> db.getReference("chats/$phone").removeValue() }
            )

            "reset_balance" -> BalanceResetModal(
                allUsers = usersMap,
                onDismiss = { activeModalId = null },
                onResetSingle = { phone -> db.getReference("users/$phone/balance").setValue(0.0) },
                onResetAll = {
                    val updates = mutableMapOf<String, Any>()
                    usersMap.keys.forEach { p -> updates["users/$p/balance"] = 0.0 }
                    db.reference.updateChildren(updates)
                }
            )

            "settings" -> SystemSettingsModal(
                db = db,
                onDismiss = { activeModalId = null }
            )
        }

        // টাস্ক পড়ার সাব-পপআপ
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
// লাইভ ওভারভিউ গ্রিড
// -------------------------------------------------------------
@Composable
fun OverviewStatsGridLive(
    totalUsers: Int,
    totalDeposit: Double,
    todayDeposit: Double,
    totalWithdraw: Double,
    todayWithdraw: Double,
    totalWorkDone: Double,
    totalUserProfits: Double,
    totalVolume: Double,
    onCardClick: (String) -> Unit
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
        Text("LIVE SYNC", color = NeonGreen, fontSize = 9.5.sp, fontWeight = FontWeight.Black)
    }

    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardLive(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "$totalUsers",
                subtitle = "Active accounts",
                glowColor = NeonBlue,
                iconUrl = "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000",
                onClick = { onCardClick("directory") }
            )
            StatCardLive(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳${String.format("%.2f", totalDeposit)}",
                subtitle = "Today: ৳${String.format("%.2f", todayDeposit)}",
                glowColor = NeonGreen,
                iconUrl = "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000",
                onClick = { onCardClick("deposits") }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardLive(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳${String.format("%.2f", totalWithdraw)}",
                subtitle = "Today: ৳${String.format("%.2f", todayWithdraw)}",
                glowColor = NeonRose,
                iconUrl = "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000",
                onClick = { onCardClick("withdrawals") }
            )
            StatCardLive(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳${String.format("%.2f", totalWorkDone)}",
                subtitle = "Completed tasks",
                glowColor = NeonYellow,
                iconUrl = "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000",
                onClick = { onCardClick("typing") }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardLive(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳${String.format("%.2f", totalUserProfits)}",
                subtitle = "Total profits",
                glowColor = NeonPurple,
                iconUrl = "https://img.icons8.com/?size=100&id=9DRY12f4liKv&format=png&color=000000",
                onClick = { onCardClick("directory") }
            )
            StatCardLive(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳${String.format("%.2f", totalVolume)}",
                subtitle = "Total assets",
                glowColor = NeonCyan,
                iconUrl = "https://img.icons8.com/?size=100&id=pemtUT1YiPwP&format=png&color=000000",
                onClick = { onCardClick("reset_balance") }
            )
        }
    }
}

@Composable
fun StatCardLive(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    glowColor: Color,
    iconUrl: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = glowColor)
            .background(Brush.verticalGradient(listOf(Color(0xFF151724), CardSurfaceBottom)), RoundedCornerShape(16.dp))
            .border(1.3.dp, Brush.verticalGradient(listOf(glowColor, glowColor.copy(alpha = 0.25f))), RoundedCornerShape(16.dp))
            .clickable { onClick() }
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
// লাইভ কমান্ড ডেক
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

data class WorkspaceActionData(
    val title: String,
    val desc: String,
    val iconUrl: String,
    val glowColor: Color,
    val badge: Int?,
    val modalId: String
)

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

// =============================================================
// ১০টি কমপ্লিট ডায়ালগ / পপআপ মডালস
// =============================================================

// ১. User Directory Modal
@Composable
fun UserDirectoryModal(
    users: List<UserProfile>,
    onDismiss: () -> Unit,
    onInspect: (String) -> Unit,
    onToggleActive: (String, Boolean) -> Unit,
    onModifyBalance: (String, Boolean, Double) -> Unit,
    onResetBalance: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = users.filter { it.name.contains(searchQuery, true) || it.phone.contains(searchQuery) || it.uid.contains(searchQuery) }

    BaseCockpitDialog(title = "User Directory Accounts", onDismiss = onDismiss) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search name, mobile or UID...", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().height(380.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { user ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)),
                    border = BorderStroke(1.dp, Color(0xFF262B3D)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(user.name.ifEmpty { "User" }, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(user.phone, color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Text("UID: ${user.uid} | Pass: ${user.password}", color = TextDimGray, fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("৳${String.format("%.2f", user.balance)}", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                Button(
                                    onClick = { onToggleActive(user.phone, !user.active) },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (user.active) Color(0xFF004D26) else Color(0xFF4D1414)),
                                    modifier = Modifier.height(26.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text(if (user.active) "ACTIVE ✓" else "INACTIVE", fontSize = 8.5.sp, color = if (user.active) NeonGreen else NeonRose)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Button(onClick = { onModifyBalance(user.phone, true, 50.0) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3320))) {
                                Text("+50৳", fontSize = 9.sp, color = NeonGreen)
                            }
                            Button(onClick = { onModifyBalance(user.phone, false, 50.0) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33200D))) {
                                Text("-50৳", fontSize = 9.sp, color = NeonYellow)
                            }
                            Button(onClick = { onResetBalance(user.phone) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF330D0D))) {
                                Text("0৳", fontSize = 9.sp, color = NeonRose)
                            }
                            Button(onClick = { onInspect(user.phone) }, modifier = Modifier.weight(1.4f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633))) {
                                Text("Inspect", fontSize = 9.sp, color = NeonCyan)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ২. User Deep-Dive Report Modal (Inspect)
@Composable
fun InspectUserModal(
    user: UserProfile,
    allUsers: Map<String, UserProfile>,
    onDismiss: () -> Unit
) {
    val referrer = allUsers.values.find { it.uid == user.referred_by }
    val myReferrals = allUsers.values.filter { it.referred_by == user.uid }

    BaseCockpitDialog(title = "User Deep-Dive & Analytics", onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().height(420.dp).verticalScroll(rememberScrollState())) {
            // Profile Card
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(user.name.ifEmpty { "User" }, color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Mobile: ${user.phone} | UID: ${user.uid}", color = NeonGreen, fontSize = 11.sp)
                    Text("Joined: ${user.registration_date.ifEmpty { "N/A" }}", color = TextDimGray, fontSize = 10.sp)
                    Text("Balance: ৳${String.format("%.2f", user.balance)}", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            // Referrer & Referral Count
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Referred By: ${referrer?.name ?: "Direct / None"} (${referrer?.phone ?: "UID: " + user.referred_by.ifEmpty { "N/A" }})", color = NeonCyan, fontSize = 11.sp)
                    Text("Total My Referrals: ${myReferrals.size} members", color = NeonPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Wallet Info
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Linked Payout Wallet: ${user.wallet?.co_wallet ?: "None"}", color = TextPureWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("Number: ${user.wallet?.co_number ?: "---"} (PIN: ${user.wallet?.co_pin ?: "---"})", color = TextDimGray, fontSize = 10.sp)
                }
            }
        }
    }
}

// ৩. Deposits Manager Modal
@Composable
fun DepositsManagerModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onApprove: (String, String, Double) -> Unit,
    onReject: (String, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }

    BaseCockpitDialog(title = "Add Money Request Manager", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(
                onClick = { activeTab = "Pending" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))
            ) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
                onClick = { activeTab = "History" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))
            ) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        val allDeposits = users.flatMap { (phone, u) ->
            u.deposits?.values?.map { dep -> Pair(u, dep) } ?: emptyList()
        }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(360.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allDeposits) { (user, dep) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("৳${dep.amount}", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                        Text("Gateway: ${dep.wallet} | Sender: ${dep.sender}", color = NeonCyan, fontSize = 10.sp)
                        Text("TxID: ${dep.txid} | Date: ${dep.date}", color = TextDimGray, fontSize = 9.5.sp)

                        if (dep.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onApprove(user.phone, dep.id, dep.amount) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onReject(user.phone, dep.id) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Text("Status: ${dep.status}", color = if (dep.status == "Success") NeonGreen else NeonRose, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

// ৪. Withdrawals Manager Modal
@Composable
fun WithdrawalsManagerModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onInspect: (String) -> Unit,
    onApprove: (String, String) -> Unit,
    onReject: (String, String, Double) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }

    BaseCockpitDialog(title = "Cash Out Request Manager", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(
                onClick = { activeTab = "Pending" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))
            ) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
                onClick = { activeTab = "History" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))
            ) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        val allWds = users.flatMap { (phone, u) ->
            u.withdrawals?.values?.map { wd -> Pair(u, wd) } ?: emptyList()
        }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(360.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allWds) { (user, wd) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("-৳${wd.amount}", color = NeonRose, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                        Text("Wallet: ${wd.wallet} (${user.wallet?.co_number ?: "---"}) PIN: ${user.wallet?.co_pin ?: "---"}", color = NeonCyan, fontSize = 10.sp)
                        Text("Date: ${wd.date}", color = TextDimGray, fontSize = 9.5.sp)

                        if (wd.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onInspect(user.phone) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633)), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Inspect", color = NeonCyan, fontSize = 10.sp)
                                }
                                Button(onClick = { onApprove(user.phone, wd.id) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onReject(user.phone, wd.id, wd.totalDeducted) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Reject", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Text("Status: ${wd.status}", color = if (wd.status == "Success") NeonGreen else NeonRose, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

// ৫. Send Money Manager Modal
@Composable
fun SendMoneyManagerModal(
    requests: List<SendMoneyRequest>,
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onApprove: (SendMoneyRequest) -> Unit,
    onReject: (SendMoneyRequest) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }
    val filtered = requests.filter { if (activeTab == "Pending") it.status == "Pending" else it.status != "Pending" }

    BaseCockpitDialog(title = "Send Money Requests", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(360.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { req ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Sender: ${req.senderName} (${req.sender})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Receiver: ${req.targetName} (${req.target})", color = NeonCyan, fontSize = 11.sp)
                        Text("Amount: ৳${req.amount} | Fee(5%): ৳${req.fee} | Total: ৳${req.totalDeducted}", color = GoldMetallicLight, fontSize = 10.5.sp)

                        if (req.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onApprove(req) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onReject(req) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
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

// ৬. Recharges Manager Modal
@Composable
fun RechargesManagerModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onApprove: (String, String) -> Unit,
    onReject: (String, String, Double) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }

    BaseCockpitDialog(title = "Mobile Recharge Operations", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        val allRc = users.flatMap { (phone, u) ->
            u.recharges?.values?.map { rc -> Pair(u, rc) } ?: emptyList()
        }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(360.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allRc) { (user, rc) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("৳${rc.amount}", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text("Operator: ${rc.operator} (${rc.type}) | Number: ${rc.number}", color = NeonCyan, fontSize = 10.5.sp)

                        if (rc.status == "Pending") {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onApprove(user.phone, rc.id) }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.weight(1f).height(28.dp)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onReject(user.phone, rc.id, rc.amount) }, colors = ButtonDefaults.buttonColors(containerColor = NeonRose), modifier = Modifier.weight(1f).height(28.dp)) {
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

// ৭. Gift Vouchers Manager Modal
@Composable
fun GiftVouchersModal(
    vouchers: List<GiftVoucher>,
    allUsers: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onGenerate: (String, String, Double) -> Unit
) {
    var claimerUid by remember { mutableStateOf("") }
    var referredUid by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("25") }
    var verificationMsg by remember { mutableStateOf("") }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    BaseCockpitDialog(title = "Gift Voucher Generator & Security", onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().height(420.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(
                value = claimerUid,
                onValueChange = { claimerUid = it },
                label = { Text("Claimer UID (৬ ডিজিট)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = referredUid,
                onValueChange = { referredUid = it },
                label = { Text("Referred UID (৬ ডিজিট)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = amountStr,
                onValueChange = { amountStr = it },
                label = { Text("Bonus Amount (৳)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

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
                            verificationMsg = if (feePaid) "✓ VERIFIED (রেফারেল ও ৪০৳ সঠিক!)" else "✗ ৪০৳ ডিপোজিট বা ফি পাওয়া যায়নি!"
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D332B))
                ) {
                    Text("১. যাচাই করুন", color = NeonCyan, fontSize = 10.sp)
                }

                Button(
                    onClick = {
                        val amt = amountStr.toDoubleOrNull() ?: 25.0
                        if (claimerUid.length == 6 && referredUid.length == 6) {
                            onGenerate(claimerUid, referredUid, amt)
                            Toast.makeText(context, "কোড তৈরি হয়েছে!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "সঠিক ৬ ডিজিটের UID দিন!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1.3f),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldMetallicMain)
                ) {
                    Text("২. কোড তৈরি করুন", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (verificationMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(verificationMsg, color = if (verificationMsg.contains("✓")) NeonGreen else NeonRose, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Active Vouchers Log", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)

            vouchers.forEach { v ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(v.code, color = NeonYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Claimer: ${v.targetUid} | Bonus: ৳${v.amount}", color = TextDimGray, fontSize = 9.sp)
                        }
                        Button(
                            onClick = {
                                clipboard.setText(AnnotatedString(v.code))
                                Toast.makeText(context, "কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.height(26.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D202D))
                        ) {
                            Text("Copy", color = NeonCyan, fontSize = 8.sp)
                        }
                    }
                }
            }
        }
    }
}

// ৮. Typing Tasks Checker Modal
@Composable
fun TypingTasksModal(
    users: Map<String, UserProfile>,
    onDismiss: () -> Unit,
    onReadWork: (String) -> Unit,
    onApprove: (String, String, Double, Double) -> Unit,
    onReject: (String, String, Double, String) -> Unit
) {
    var activeTab by remember { mutableStateOf("Pending") }

    BaseCockpitDialog(title = "Typing Tasks Checker", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(onClick = { activeTab = "Pending" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "Pending") NeonGreen else Color(0xFF1D202D))) {
                Text("Pending", color = if (activeTab == "Pending") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(onClick = { activeTab = "History" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (activeTab == "History") NeonGreen else Color(0xFF1D202D))) {
                Text("History", color = if (activeTab == "History") Color.Black else TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        val allTasks = users.flatMap { (phone, u) ->
            u.paragraph_jobs?.values?.map { t -> Pair(u, t) } ?: emptyList()
        }.filter { if (activeTab == "Pending") it.second.status == "Pending" else it.second.status != "Pending" }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(360.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(allTasks) { (user, task) ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161926)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${user.name} (${user.phone})", color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Reward: ৳${task.amount}", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Text("Topic: \"${task.topic}\" | Fee: ৳${task.entry_fee}", color = NeonCyan, fontSize = 10.sp)

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(onClick = { onReadWork(task.text) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2633))) {
                                Text("Read Work", color = NeonCyan, fontSize = 10.sp)
                            }
                            if (task.status == "Pending") {
                                Button(onClick = { onApprove(user.phone, task.id, task.amount, task.entry_fee) }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
                                    Text("Approve", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(onClick = { onReject(user.phone, task.id, task.entry_fee, "নিয়ম অনুযায়ী লেখা সম্পন্ন হয়নি") }, modifier = Modifier.weight(1f).height(28.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonRose)) {
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

// ৯. Support Chat Modal
@Composable
fun SupportChatModal(
    chats: Map<String, List<ChatMessage>>,
    onDismiss: () -> Unit,
    onSendReply: (String, String) -> Unit,
    onMarkSeen: (String, String) -> Unit,
    onClearChat: (String) -> Unit
) {
    var selectedUserPhone by remember { mutableStateOf<String?>(chats.keys.firstOrNull()) }
    var replyText by remember { mutableStateOf("") }

    BaseCockpitDialog(title = "Live Support Helpdesk", onDismiss = onDismiss) {
        Row(modifier = Modifier.fillMaxWidth().height(380.dp)) {
            // User List Sidebar
            LazyColumn(modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, Color(0xFF262B3D))) {
                items(chats.keys.toList()) { phone ->
                    val unread = chats[phone]?.count { it.sender == "user" && !it.seen } ?: 0
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (selectedUserPhone == phone) Color(0xFF0D332B) else Color.Transparent)
                            .clickable { selectedUserPhone = phone }
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(phone, color = TextPureWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            if (unread > 0) {
                                Text("$unread Unread", color = NeonRose, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Chat Messages Thread
            selectedUserPhone?.let { phone ->
                val msgList = chats[phone] ?: emptyList()
                Column(modifier = Modifier.weight(2f).fillMaxHeight()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(phone, color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Button(onClick = { onClearChat(phone) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF330D0D)), modifier = Modifier.height(24.dp)) {
                            Text("Clear", color = NeonRose, fontSize = 8.sp)
                        }
                    }

                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(msgList) { msg ->
                            if (msg.sender == "user" && !msg.seen) {
                                onMarkSeen(phone, msg.id)
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (msg.sender == "user") Alignment.CenterStart else Alignment.CenterEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(if (msg.sender == "user") Color(0xFF161926) else NeonGreen, RoundedCornerShape(8.dp))
                                        .padding(6.dp)
                                ) {
                                    Text(msg.text, color = if (msg.sender == "user") Color.White else Color.Black, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = replyText,
                            onValueChange = { replyText = it },
                            placeholder = { Text("Reply...", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f).height(42.dp),
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
                            modifier = Modifier.height(42.dp)
                        ) {
                            Text("Send", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ১০. Balance Reset Controller Modal
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
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = searchInput,
                onValueChange = { searchInput = it },
                label = { Text("ফোন নম্বর অথবা ৬ ডিজিট UID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    val user = allUsers[searchInput] ?: allUsers.values.find { it.uid == searchInput }
                    if (user != null) {
                        onResetSingle(user.phone)
                        Toast.makeText(context, "${user.phone} এর ব্যালেন্স ৳০.০০ করা হয়েছে!", Toast.LENGTH_SHORT).show()
                        searchInput = ""
                    } else {
                        Toast.makeText(context, "ইউজার পাওয়া যায়নি!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF663300))
            ) {
                Text("নির্দিষ্ট ইউজারের ব্যালেন্স ০.০০ করুন", color = GoldMetallicLight, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFF262B3D))
            Spacer(modifier = Modifier.height(12.dp))

            Text("২. সকল ইউজারের ব্যালেন্স এক ক্লিকে ৳০.০০ করুন", color = NeonRose, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("মোট ইউজার: ${allUsers.size} জন", color = TextDimGray, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onResetAll()
                    Toast.makeText(context, "সকল ইউজারের ব্যালেন্স ৳০.০০ করা হয়েছে!", Toast.LENGTH_LONG).show()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
            ) {
                Text("সকলের ব্যালেন্স একসাথে ৳০.০০ করুন", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}

// ১১. System Settings & Gateways Modal
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
        Column(modifier = Modifier.fillMaxWidth().height(420.dp).verticalScroll(rememberScrollState())) {
            Text("MFS Gateways", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            OutlinedTextField(value = bkashNum, onValueChange = { bkashNum = it }, label = { Text("bKash Personal No.") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = bkashLbl, onValueChange = { bkashLbl = it }, label = { Text("bKash Label") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = nagadNum, onValueChange = { nagadNum = it }, label = { Text("Nagad Personal No.") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = nagadLbl, onValueChange = { nagadLbl = it }, label = { Text("Nagad Label") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Spacer(modifier = Modifier.height(10.dp))
            Text("Official Links", color = GoldMetallicLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            OutlinedTextField(value = tgLink, onValueChange = { tgLink = it }, label = { Text("Telegram Link") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = apkLink, onValueChange = { apkLink = it }, label = { Text("APK Download Link") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

            Spacer(modifier = Modifier.height(14.dp))
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

// বেস ডায়ালগ কনটেইনার
@Composable
fun BaseCockpitDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
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
// বটম বার ও হেডার হেল্পার
// -------------------------------------------------------------
@Composable
fun CockpitLuxuryBottomNav() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home" to Icons.Rounded.Home, "Users" to Icons.Rounded.Groups, "Reports" to Icons.Rounded.BarChart, "Settings" to Icons.Rounded.Settings)

    NavigationBar(
        containerColor = Color(0xFF08090D),
        tonalElevation = 8.dp,
        modifier = Modifier.height(60.dp).border(1.2.dp, GoldMetallicMain.copy(0.3f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { selectedItem = index },
                icon = {
                    Box(modifier = Modifier.size(if (isSelected) 32.dp else 24.dp).background(if (isSelected) GoldMetallicMain.copy(0.2f) else Color.Transparent, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(item.second, contentDescription = item.first, tint = if (isSelected) GoldMetallicLight else TextDimGray, modifier = Modifier.size(18.dp))
                    }
                },
                label = { Text(item.first, color = if (isSelected) GoldMetallicLight else TextDimGray, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun HeaderBarCompact() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(Color(0xFF1D202D), RoundedCornerShape(12.dp)).border(1.2.dp, GoldMetallicMain.copy(alpha = 0.5f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldMetallicLight, modifier = Modifier.size(22.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(46.dp).background(Color.Black, CircleShape).border(2.dp, GoldMetallicMain, CircleShape), contentAlignment = Alignment.Center) {
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
            Box(modifier = Modifier.size(38.dp).background(Color(0xFF161824), CircleShape).border(1.2.dp, GoldMetallicMain.copy(0.4f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(20.dp))
            }
            Box(modifier = Modifier.size(38.dp).background(Color(0xFF161824), CircleShape).border(1.2.dp, GoldMetallicMain.copy(0.4f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(20.dp))
            }
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
    }
}
