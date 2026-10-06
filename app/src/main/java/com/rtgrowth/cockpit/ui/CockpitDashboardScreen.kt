package com.rtgrowth.cockpit.ui

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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun CockpitDashboardScreen() {
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
            OverviewStatsSection()
            Spacer(modifier = Modifier.height(16.dp))
            WorkspaceDeckSection()
            Spacer(modifier = Modifier.height(14.dp))
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
// ৩. ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড (৬টি আইকন লিংক সহ)
// -------------------------------------------------------------
@Composable
fun OverviewStatsSection() {
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
        // Row 1: Total Users & Total Deposit
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                change = "+12%",
                subtitle = "Active accounts",
                glowColor = NeonBlue,
                iconUrl = "https://img.icons8.com/?size=100&id=3Z9nycT6VFaI&format=png&color=000000"
            )
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                change = "+8%",
                subtitle = "Today: ৳0.00",
                glowColor = NeonGreen,
                iconUrl = "https://img.icons8.com/?size=100&id=JQX2fDPyQq4E&format=png&color=000000"
            )
        }
        // Row 2: Total Withdraw & Work Value Done
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                change = "+6%",
                subtitle = "Today: ৳0.00",
                glowColor = NeonRose,
                iconUrl = "https://img.icons8.com/?size=100&id=nBI1rs9Fp9Lm&format=png&color=000000"
            )
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                change = "+0%",
                subtitle = "Completed tasks",
                glowColor = NeonYellow,
                iconUrl = "https://img.icons8.com/?size=100&id=5rjf4RBWzzU4&format=png&color=000000"
            )
        }
        // Row 3: User Profits & Asset Volume (Accept Value)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                change = "+15%",
                subtitle = "Total profits",
                glowColor = NeonPurple,
                iconUrl = "https://img.icons8.com/?size=100&id=9DRY12f4liKv&format=png&color=000000"
            )
            StatCardItem(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                change = "+9%",
                subtitle = "Total assets",
                glowColor = NeonCyan,
                iconUrl = "https://img.icons8.com/?size=100&id=pemtUT1YiPwP&format=png&color=000000"
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
    iconUrl: String
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
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = title,
                    modifier = Modifier.size(38.dp),
                    contentScale = ContentScale.Fit
                )

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
// ৪. কমান্ড ডেক (আপনার দেওয়া সবকটি আইকন লিংক সহ)
// -------------------------------------------------------------
@Composable
fun WorkspaceDeckSection() {
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

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WorkspaceCardItem(modifier = Modifier.weight(1f), data = workspaceList[i])
                if (i + 1 < workspaceList.size) {
                    WorkspaceCardItem(modifier = Modifier.weight(1f), data = workspaceList[i + 1])
                }
            }
        }
        // Together We Grow Golden Chart Card
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            TogetherWeGrowCompactCard(modifier = Modifier.weight(1f))
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
fun WorkspaceCardItem(modifier: Modifier = Modifier, data: WorkspaceActionData) {
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
            .clickable { }
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
            AsyncImage(
                model = data.iconUrl,
                contentDescription = data.title,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit
            )

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

@Composable
fun TogetherWeGrowCompactCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = GoldMetallicMain)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF241B0A), CardSurfaceBottom)),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.2.dp,
                Brush.verticalGradient(listOf(GoldMetallicMain, Color(0xFF4A3405))),
                RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Together\nWe Grow",
                color = GoldMetallicLight,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Canvas(modifier = Modifier.size(38.dp, 24.dp)) {
                val barWidth = 5.dp.toPx()
                val barGap = 3.dp.toPx()
                val goldBrush = Brush.verticalGradient(listOf(GoldMetallicLight, GoldMetallicDark))
                
                drawRoundRect(brush = goldBrush, topLeft = Offset(0f, size.height * 0.6f), size = Size(barWidth, size.height * 0.4f), cornerRadius = CornerRadius(3f))
                drawRoundRect(brush = goldBrush, topLeft = Offset(barWidth + barGap, size.height * 0.35f), size = Size(barWidth, size.height * 0.65f), cornerRadius = CornerRadius(3f))
                drawRoundRect(brush = goldBrush, topLeft = Offset((barWidth + barGap) * 2, 0f), size = Size(barWidth, size.height), cornerRadius = CornerRadius(3f))

                val arrowPath = Path().apply {
                    moveTo(0f, size.height * 0.55f)
                    cubicTo(size.width * 0.4f, size.height * 0.4f, size.width * 0.7f, size.height * 0.1f, size.width, 0f)
                }
                drawPath(arrowPath, color = GoldMetallicLight, style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round))
            }
        }
    }
}

// -------------------------------------------------------------
// ৫. লাক্সারি বটম নেভিগেশন বার
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
