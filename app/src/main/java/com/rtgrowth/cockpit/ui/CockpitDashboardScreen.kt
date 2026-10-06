package com.rtgrowth.cockpit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage

// ==========================================
// 🎨 Luxury Gold & Deep Dark Palette
// ==========================================
val DarkCanvasBg = Color(0xFF07080B)
val CardSurfaceTop = Color(0xFF131520)
val CardSurfaceBottom = Color(0xFF090A0E)

val GoldMetallicLight = Color(0xFFFFE57F)
val GoldMetallicMain = Color(0xFFF5BA42)
val GoldMetallicDark = Color(0xFF8D6210)

// 3D Neon Card Outlines
val NeonBlue = Color(0xFF2979FF)
val NeonGreen = Color(0xFF00E676)
val NeonRose = Color(0xFFFF1744)
val NeonYellow = Color(0xFFFFD600)
val NeonPurple = Color(0xFFD500F9)
val NeonCyan = Color(0xFF00E5FF)

val TextDimGray = Color(0xFF8A92A6)
val TextPureWhite = Color(0xFFFFFFFF)

// মডাল ডাটা ক্লাস
data class CockpitModalData(
    val title: String,
    val description: String,
    val iconUrl: String,
    val themeColor: Color,
    val statValue: String? = null
)

@Composable
fun CockpitDashboardScreen() {
    var activeModal by remember { mutableStateOf<CockpitModalData?>(null) }

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
            
            // ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড (ক্লিক করলে মডাল ওপেন হবে)
            OverviewStatsSection(onCardClick = { activeModal = it })
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // কমান্ড ডেক (ক্লিক করলে মডাল ওপেন হবে)
            WorkspaceDeckSection(onCardClick = { activeModal = it })
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        // পপআপ মডাল ডায়ালগ
        activeModal?.let { modalData ->
            CockpitInteractiveModal(
                data = modalData,
                onDismiss = { activeModal = null }
            )
        }
    }
}

// -------------------------------------------------------------
// ১. টপ হেডার (গোল্ডেন ক্রাউন ও ব্র্যান্ডিং)
// -------------------------------------------------------------
@Composable
fun HeaderBarCompact() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF1D202D), Color(0xFF10121A))), RoundedCornerShape(12.dp))
                .border(1.2.dp, GoldMetallicMain.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldMetallicLight, modifier = Modifier.size(22.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(12.dp, CircleShape, ambientColor = GoldMetallicMain, spotColor = GoldMetallicMain)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, Brush.sweepGradient(listOf(GoldMetallicLight, GoldMetallicDark, GoldMetallicLight)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.size(15.dp, 9.dp)) {
                        val path = Path().apply {
                            moveTo(0f, size.height)
                            lineTo(0f, size.height * 0.2f)
                            lineTo(size.width * 0.25f, size.height * 0.55f)
                            lineTo(size.width * 0.5f, 0f)
                            lineTo(size.width * 0.75f, size.height * 0.55f)
                            lineTo(size.width, size.height * 0.2f)
                            lineTo(size.width, size.height)
                            close()
                        }
                        drawPath(path, color = GoldMetallicLight)
                    }
                    Text("RT", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 15.sp, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("RT GROWTH", color = GoldMetallicMain, fontWeight = FontWeight.Black, fontSize = 17.sp, letterSpacing = 1.sp)
                Text("COCKPIT", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 2.5.sp)
                Text("MASTER COMMAND HUB", color = GoldMetallicMain.copy(alpha = 0.75f), fontSize = 7.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
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
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                        .background(NeonRose, CircleShape)
                        .border(1.5.dp, Color.Black, CircleShape)
                )
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
// ২. ওয়েলকাম ও ডাটাবেজ ব্যানার
// -------------------------------------------------------------
@Composable
fun WelcomeCardCompact() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), ambientColor = GoldMetallicMain.copy(0.2f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF171A24), CardSurfaceBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.4.dp,
                Brush.horizontalGradient(listOf(GoldMetallicMain, Color(0xFF553C07), GoldMetallicMain)),
                RoundedCornerShape(18.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(Brush.radialGradient(listOf(GoldMetallicMain.copy(0.35f), Color.Transparent)), CircleShape)
                        .border(1.2.dp, GoldMetallicMain, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(34.dp)) {
                        drawCircle(Brush.verticalGradient(listOf(GoldMetallicLight, GoldMetallicDark)))
                    }
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextDimGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    Text("Admin", color = GoldMetallicLight, fontSize = 15.sp, fontWeight = FontWeight.Black)
                    Text("Have a great day!", color = TextDimGray, fontSize = 9.sp)
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color(0xFF2E3244))
            )

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(24.dp, 24.dp)) {
                        val cyanGrad = Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF004D73)))
                        drawOval(cyanGrad, topLeft = Offset(0f, 0f), size = Size(size.width * 0.85f, size.height * 0.4f))
                        drawRect(cyanGrad, topLeft = Offset(0f, size.height * 0.2f), size = Size(size.width * 0.85f, size.height * 0.5f))
                        drawOval(cyanGrad, topLeft = Offset(0f, size.height * 0.5f), size = Size(size.width * 0.85f, size.height * 0.4f))
                        drawCircle(Color(0xFF00E676), radius = size.width * 0.22f, center = Offset(size.width * 0.8f, size.height * 0.7f))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Database Status", color = TextPureWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF00381C))
                                .border(1.dp, NeonGreen, RoundedCornerShape(16.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text("Connected", color = NeonGreen, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("typing-5c3e4-default-rtdb", color = TextDimGray, fontSize = 8.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
    }
}

// -------------------------------------------------------------
// ৩. ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড (বড় স্পষ্ট আইকন ও ছোট ব্যাকগ্রাউন্ড)
// -------------------------------------------------------------
@Composable
fun OverviewStatsSection(onCardClick: (CockpitModalData) -> Unit) {
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
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                change = "+12%",
                subtitle = "Active accounts",
                glowColor = NeonBlue,
                iconUrl = "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000",
                onClick = {
                    onCardClick(
                        CockpitModalData(
                            title = "Total Users Overview",
                            description = "Current active registered users database log and network status.",
                            iconUrl = "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000",
                            themeColor = NeonBlue,
                            statValue = "919 Active Accounts"
                        )
                    )
                }
            )
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                change = "+8%",
                subtitle = "Today: ৳0.00",
                glowColor = NeonGreen,
                iconUrl = "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000",
                onClick = {
                    onCardClick(
                        CockpitModalData(
                            title = "Total Deposit Summary",
                            description = "All incoming gateway transactions, MFS verification and completed deposits.",
                            iconUrl = "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000",
                            themeColor = NeonGreen,
                            statValue = "৳64,597.00 (Today: ৳0.00)"
                        )
                    )
                }
            )
        }
        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                change = "+6%",
                subtitle = "Today: ৳0.00",
                glowColor = NeonRose,
                iconUrl = "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000",
                onClick = {
                    onCardClick(
                        CockpitModalData(
                            title = "Total Withdrawals Record",
                            description = "Approved payouts, pending payout requests and user settlement history.",
                            iconUrl = "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000",
                            themeColor = NeonRose,
                            statValue = "৳57,414.00 (Today: ৳0.00)"
                        )
                    )
                }
            )
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                change = "+0%",
                subtitle = "Completed tasks",
                glowColor = NeonYellow,
                iconUrl = "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000",
                onClick = {
                    onCardClick(
                        CockpitModalData(
                            title = "Work Value Done",
                            description = "Today's typing tasks completion value and work efficiency metrics.",
                            iconUrl = "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000",
                            themeColor = NeonYellow,
                            statValue = "৳0.00 Completed"
                        )
                    )
                }
            )
        }
        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                change = "+15%",
                subtitle = "Total profits",
                glowColor = NeonPurple,
                iconUrl = "https://img.icons8.com/?size=100&id=9DRY12f4liKv&format=png&color=000000",
                onClick = {
                    onCardClick(
                        CockpitModalData(
                            title = "User Profits Analytics",
                            description = "Total lifetime profits earned by active users across tasks and referrals.",
                            iconUrl = "https://img.icons8.com/?size=100&id=9DRY12f4liKv&format=png&color=000000",
                            themeColor = NeonPurple,
                            statValue = "৳310,073.00 Total Profit"
                        )
                    )
                }
            )
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                change = "+9%",
                subtitle = "Total assets",
                glowColor = NeonCyan,
                iconUrl = "https://img.icons8.com/?size=100&id=pemtUT1YiPwP&format=png&color=000000",
                onClick = {
                    onCardClick(
                        CockpitModalData(
                            title = "Asset Volume (Accept Value)",
                            description = "Combined wallet balances and liquid database assets.",
                            iconUrl = "https://img.icons8.com/?size=100&id=pemtUT1YiPwP&format=png&color=000000",
                            themeColor = NeonCyan,
                            statValue = "৳53,496.90 In Vault"
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun StatCardItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    subtitle: String,
    glowColor: Color,
    iconUrl: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = glowColor)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF151724), CardSurfaceBottom)),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(glowColor, glowColor.copy(alpha = 0.25f))),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ছোট ব্যাকগ্রাউন্ড ফ্রেম ও বড় আইকন
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(glowColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, glowColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = iconUrl,
                        contentDescription = title,
                        modifier = Modifier.size(34.dp), // বড় এবং স্পষ্ট
                        contentScale = ContentScale.Fit
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextDimGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        value,
                        color = TextPureWhite,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = null,
                        tint = glowColor,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(change, color = glowColor, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
                Text(subtitle, color = TextDimGray, fontSize = 8.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// -------------------------------------------------------------
// ৪. কমান্ড ডেক (১০টি স্লিম কার্ড - Together We Grow সরানো হয়েছে)
// -------------------------------------------------------------
@Composable
fun WorkspaceDeckSection(onCardClick: (CockpitModalData) -> Unit) {
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
        WorkspaceActionData(
            "User Directory",
            "Manage users, profiles and account activity.",
            "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000",
            NeonBlue,
            null
        ),
        WorkspaceActionData(
            "Deposits",
            "Review and manage deposit requests.",
            "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000",
            NeonYellow,
            null
        ),
        WorkspaceActionData(
            "Withdrawals",
            "Pending withdrawal requests.",
            "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000",
            NeonGreen,
            2
        ),
        WorkspaceActionData(
            "Send Money Req",
            "Handle transfer requests.",
            "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000",
            NeonBlue,
            0
        ),
        WorkspaceActionData(
            "Recharges",
            "Mobile & wallet recharge requests.",
            "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000",
            NeonYellow,
            0
        ),
        WorkspaceActionData(
            "Gift Vouchers",
            "Create and manage voucher codes.",
            "https://img.icons8.com/?size=100&id=DA67d1tKQ9Pr&format=png&color=000000",
            NeonRose,
            31
        ),
        WorkspaceActionData(
            "Typing Tasks",
            "Create tasks and review submissions.",
            "https://img.icons8.com/?size=100&id=oZAinaxvg8AD&format=png&color=000000",
            NeonPurple,
            0
        ),
        WorkspaceActionData(
            "Support Chat",
            "View conversations and reply to users.",
            "https://img.icons8.com/?size=100&id=RntMFwIniVlj&format=png&color=000000",
            NeonCyan,
            3
        ),
        WorkspaceActionData(
            "Balance Reset",
            "Authorized balance correction tools.",
            "https://img.icons8.com/?size=100&id=ifMVi1WVk8u2&format=png&color=000000",
            NeonRose,
            null
        ),
        WorkspaceActionData(
            "System Settings",
            "Configure security, notifications and database.",
            "https://img.icons8.com/?size=100&id=v39wEv8JU1aa&format=png&color=000000",
            NeonCyan,
            null
        )
    )

    // ২-কলাম গ্রিডে ১০টি কার্ড সমানভাবে সাজানো হয়েছে
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WorkspaceCardItem(
                    modifier = Modifier.weight(1f),
                    data = workspaceList[i],
                    onClick = {
                        onCardClick(
                            CockpitModalData(
                                title = workspaceList[i].title,
                                description = workspaceList[i].desc,
                                iconUrl = workspaceList[i].iconUrl,
                                themeColor = workspaceList[i].glowColor
                            )
                        )
                    }
                )
                if (i + 1 < workspaceList.size) {
                    WorkspaceCardItem(
                        modifier = Modifier.weight(1f),
                        data = workspaceList[i + 1],
                        onClick = {
                            onCardClick(
                                CockpitModalData(
                                    title = workspaceList[i + 1].title,
                                    description = workspaceList[i + 1].desc,
                                    iconUrl = workspaceList[i + 1].iconUrl,
                                    themeColor = workspaceList[i + 1].glowColor
                                )
                            )
                        }
                    )
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
    val badge: Int?
)

@Composable
fun WorkspaceCardItem(
    modifier: Modifier = Modifier,
    data: WorkspaceActionData,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF151722), CardSurfaceBottom)),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Brush.verticalGradient(listOf(GoldMetallicMain.copy(0.4f), Color(0xFF232634))),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        if (data.badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
                    .shadow(4.dp, CircleShape, spotColor = NeonRose)
                    .background(if (data.badge > 0) NeonRose else Color(0xFF651FFF), CircleShape)
                    .border(0.8.dp, Color.White.copy(0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${data.badge}", color = Color.White, fontSize = 8.5.sp, fontWeight = FontWeight.Black)
            }
        }

        Column {
            // ছোট ব্যাকগ্রাউন্ড ফ্রেম ও বড় আইকন
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(data.glowColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                    .border(1.dp, data.glowColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = data.iconUrl,
                    contentDescription = data.title,
                    modifier = Modifier.size(36.dp), // বড় ও গাঢ় আইকন
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(data.title, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                data.desc,
                color = TextDimGray,
                fontSize = 8.sp,
                lineHeight = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

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
// ৫. ইন্টারঅ্যাক্টিভ ৩ডি অ্যাকশন মডাল (Dialog)
// -------------------------------------------------------------
@Composable
fun CockpitInteractiveModal(
    data: CockpitModalData,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .shadow(24.dp, RoundedCornerShape(22.dp), ambientColor = data.themeColor, spotColor = data.themeColor)
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF161826), Color(0xFF090A0F))),
                    RoundedCornerShape(22.dp)
                )
                .border(
                    1.4.dp,
                    Brush.linearGradient(listOf(data.themeColor, GoldMetallicMain.copy(0.4f), data.themeColor.copy(0.2f))),
                    RoundedCornerShape(22.dp)
                )
                .padding(18.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // টপ বার
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "COMMAND WORKSPACE CONSOLE",
                        color = GoldMetallicLight,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextDimGray, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // বড় গ্লোয়িং আইকন
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(12.dp, CircleShape, spotColor = data.themeColor)
                        .background(Brush.radialGradient(listOf(data.themeColor.copy(0.35f), Color(0xFF10121A))), CircleShape)
                        .border(1.5.dp, data.themeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = data.iconUrl,
                        contentDescription = data.title,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = data.title,
                    color = TextPureWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                if (data.statValue != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = data.statValue,
                        color = GoldMetallicLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = data.description,
                    color = TextDimGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // অ্যাকশন বাটন
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D202D)),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFF323647))
                    ) {
                        Text("Dismiss", color = TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = data.themeColor),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Open Desk", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ৬. লাক্সারি বটম নেভিগেশন বার
// -------------------------------------------------------------
@Composable
fun CockpitLuxuryBottomNav() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        "Home" to Icons.Rounded.Home,
        "Users" to Icons.Rounded.Groups,
        "Reports" to Icons.Rounded.BarChart,
        "Settings" to Icons.Rounded.Settings
    )

    NavigationBar(
        containerColor = Color(0xFF08090D),
        tonalElevation = 8.dp,
        modifier = Modifier
            .height(60.dp)
            .border(
                1.2.dp,
                Brush.horizontalGradient(listOf(Color.Transparent, GoldMetallicMain.copy(0.4f), Color.Transparent)),
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { selectedItem = index },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 32.dp else 24.dp)
                            .background(
                                if (isSelected) GoldMetallicMain.copy(0.2f) else Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.second,
                            contentDescription = item.first,
                            tint = if (isSelected) GoldMetallicLight else TextDimGray,
                            modifier = Modifier.size(18.dp)
                        )
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
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
