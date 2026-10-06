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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 🎨 Luxury 3D Dark & Neon Color Palette
// ==========================================
val BgCanvasDark = Color(0xFF06070A)
val CardSurfaceGradientStart = Color(0xFF151824)
val CardSurfaceGradientEnd = Color(0xFF090A0F)

val GoldMetallicLight = Color(0xFFFFE082)
val GoldMetallicMain = Color(0xFFF5BA42)
val GoldMetallicDark = Color(0xFF8D6210)

// 3D Neon Outlines
val NeonBlue3D = Color(0xFF2979FF)
val NeonGreen3D = Color(0xFF00E676)
val NeonRose3D = Color(0xFFFF1744)
val NeonYellow3D = Color(0xFFFFD600)
val NeonPurple3D = Color(0xFFD500F9)
val NeonCyan3D = Color(0xFF00E5FF)

val TextDimGray = Color(0xFF8A92A6)
val TextPureWhite = Color(0xFFFFFFFF)

@Composable
fun CockpitDashboardScreen() {
    Scaffold(
        bottomBar = { CockpitLuxuryBottomNav() },
        containerColor = BgCanvasDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF131624), BgCanvasDark, Color(0xFF030406))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            HeaderBar3D()
            Spacer(modifier = Modifier.height(14.dp))
            WelcomeCard3D()
            Spacer(modifier = Modifier.height(18.dp))
            OverviewStatsSection3D()
            Spacer(modifier = Modifier.height(20.dp))
            WorkspaceDeckSection3D()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// -------------------------------------------------------------
// ১. টপ হেডার (৩ডি গোল্ডেন ক্রাউন ও ব্র্যান্ডিং)
// -------------------------------------------------------------
@Composable
fun HeaderBar3D() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // মেনু আইকন
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF1E2130), Color(0xFF10121A))), RoundedCornerShape(14.dp))
                .border(1.2.dp, GoldMetallicMain.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldMetallicLight, modifier = Modifier.size(24.dp))
        }

        // লোগো
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(14.dp, CircleShape, ambientColor = GoldMetallicMain, spotColor = GoldMetallicMain)
                    .background(Color.Black, CircleShape)
                    .border(2.2.dp, Brush.sweepGradient(listOf(GoldMetallicLight, GoldMetallicDark, GoldMetallicLight)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.size(16.dp, 10.dp)) {
                        draw3DCrown()
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("RT", color = GoldMetallicLight, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("RT GROWTH", color = GoldMetallicMain, fontWeight = FontWeight.Black, fontSize = 19.sp, letterSpacing = 1.sp)
                Text("COCKPIT", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 13.sp, letterSpacing = 3.sp)
                Text("MASTER COMMAND HUB", color = GoldMetallicMain.copy(alpha = 0.75f), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
            }
        }

        // নোটিফিকেশন ও প্রোফাইল
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF161824), CircleShape)
                    .border(1.2.dp, GoldMetallicMain.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(22.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-3).dp, y = 3.dp)
                        .background(NeonRose3D, CircleShape)
                        .border(1.5.dp, Color.Black, CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF161824), CircleShape)
                    .border(1.2.dp, GoldMetallicMain.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(22.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// ২. ওয়েলকাম ও ডাটাবেজ ব্যানার (৩ডি সিলিন্ডার ও গোল্ডেন মেডেল)
// -------------------------------------------------------------
@Composable
fun WelcomeCard3D() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp), ambientColor = GoldMetallicMain.copy(0.2f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF181B26), CardSurfaceGradientEnd)),
                RoundedCornerShape(22.dp)
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(GoldMetallicMain, Color(0xFF553C07), GoldMetallicMain)),
                RoundedCornerShape(22.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Admin with 3D Crown Medallion
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(Brush.radialGradient(listOf(GoldMetallicMain.copy(0.35f), Color.Transparent)), CircleShape)
                        .border(1.5.dp, GoldMetallicMain, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(38.dp)) {
                        drawCircle(
                            brush = Brush.verticalGradient(listOf(GoldMetallicLight, GoldMetallicDark))
                        )
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.White.copy(0.6f), Color.Transparent),
                                center = Offset(size.width * 0.35f, size.height * 0.3f),
                                radius = size.width * 0.4f
                            )
                        )
                    }
                    Canvas(modifier = Modifier.size(22.dp, 16.dp)) {
                        draw3DCrown(Color(0xFF332000))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextDimGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("Admin", color = GoldMetallicLight, fontSize = 17.sp, fontWeight = FontWeight.Black)
                    Text("Have a great day!", color = TextDimGray, fontSize = 10.sp)
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(46.dp)
                    .background(Color(0xFF323647))
            )

            // Database Info with 3D Stacked Drums
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(28.dp, 28.dp)) {
                        draw3DDatabase()
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Database Status", color = TextPureWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF00381C))
                                .border(1.dp, NeonGreen3D, RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Connected", color = NeonGreen3D, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("typing-5c3e4-default-rtdb", color = TextDimGray, fontSize = 9.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
    }
}

// -------------------------------------------------------------
// ৩. ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড (৬টি নিখুঁত ৩ডি কার্ড)
// -------------------------------------------------------------
@Composable
fun OverviewStatsSection3D() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = GoldMetallicMain, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Overview Statistics", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
        Text("Oct 5, 2026 | 02:03 PM", color = TextDimGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Row 1: Users & Deposit
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Exact3DStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                change = "+12%",
                subtitle = "Active accounts",
                glowColor = NeonBlue3D,
                iconType = "3d_users"
            )
            Exact3DStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                change = "+8%",
                subtitle = "Today: ৳0.00",
                glowColor = NeonGreen3D,
                iconType = "3d_deposit"
            )
        }
        // Row 2: Withdraw & Work Done
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Exact3DStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                change = "+6%",
                subtitle = "Today: ৳0.00",
                glowColor = NeonRose3D,
                iconType = "3d_withdraw"
            )
            Exact3DStatCard(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                change = "+0%",
                subtitle = "Completed tasks",
                glowColor = NeonYellow3D,
                iconType = "3d_work"
            )
        }
        // Row 3: Profits & Assets
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Exact3DStatCard(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                change = "+15%",
                subtitle = "Total profits",
                glowColor = NeonPurple3D,
                iconType = "3d_profits"
            )
            Exact3DStatCard(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                change = "+9%",
                subtitle = "Total assets",
                glowColor = NeonCyan3D,
                iconType = "3d_assets"
            )
        }
    }
}

@Composable
fun Exact3DStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    subtitle: String,
    glowColor: Color,
    iconType: String
) {
    Box(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = glowColor.copy(0.4f), spotColor = glowColor)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161826), CardSurfaceGradientEnd)),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.4.dp,
                Brush.verticalGradient(listOf(glowColor, glowColor.copy(alpha = 0.2f))),
                RoundedCornerShape(20.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Visual Renderer
                Render3DIconCanvas(type = iconType, modifier = Modifier.size(46.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextDimGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        value,
                        color = TextPureWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(change, color = glowColor, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
                Text(subtitle, color = TextDimGray, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// -------------------------------------------------------------
// ৪. কমান্ড ডেক (১০টি ৩ডি মেনু কার্ড)
// -------------------------------------------------------------
@Composable
fun WorkspaceDeckSection3D() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.GridView, contentDescription = null, tint = GoldMetallicMain, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Command Workspace Deck", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
        Text("Manage • Monitor • Grow", color = GoldMetallicDark, fontSize = 10.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    val workspaceList = listOf(
        WorkspaceActionData("User Directory", "Manage users, profiles and account activity.", "3d_users_deck", NeonBlue3D, null),
        WorkspaceActionData("Deposits", "Review and manage deposit requests.", "3d_wallet_blue", NeonYellow3D, null),
        WorkspaceActionData("Withdrawals", "Pending withdrawal requests.", "3d_cash_green", NeonGreen3D, 2),
        WorkspaceActionData("Send Money Req", "Handle transfer requests.", "3d_plane", NeonBlue3D, 0),
        WorkspaceActionData("Recharges", "Mobile & wallet recharge requests.", "3d_phone", NeonYellow3D, 0),
        WorkspaceActionData("Gift Vouchers", "Create and manage voucher codes.", "3d_gift", NeonRose3D, 31),
        WorkspaceActionData("Typing Tasks", "Create tasks and review submissions.", "3d_keyboard", NeonPurple3D, 0),
        WorkspaceActionData("Support Chat", "View conversations and reply to users.", "3d_headset", NeonCyan3D, 3),
        WorkspaceActionData("Balance Reset", "Authorized balance correction tools.", "3d_refresh", NeonRose3D, null),
        WorkspaceActionData("System Settings", "Configure security, notifications and database.", "3d_gear", NeonCyan3D, null)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Workspace3DCard(modifier = Modifier.weight(1f), data = workspaceList[i])
                if (i + 1 < workspaceList.size) {
                    Workspace3DCard(modifier = Modifier.weight(1f), data = workspaceList[i + 1])
                }
            }
        }
        // Together We Grow Golden Chart Card
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            TogetherWeGrow3DCard(modifier = Modifier.weight(1f))
        }
    }
}

data class WorkspaceActionData(
    val title: String,
    val desc: String,
    val iconType: String,
    val glowColor: Color,
    val badge: Int?
)

@Composable
fun Workspace3DCard(modifier: Modifier = Modifier, data: WorkspaceActionData) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161824), CardSurfaceGradientEnd)),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.2.dp,
                Brush.verticalGradient(listOf(GoldMetallicMain.copy(0.45f), Color(0xFF262A38))),
                RoundedCornerShape(20.dp)
            )
            .clickable { }
            .padding(12.dp)
    ) {
        // Glowing Badge
        if (data.badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .shadow(6.dp, CircleShape, spotColor = NeonRose3D)
                    .background(if (data.badge > 0) NeonRose3D else Color(0xFF651FFF), CircleShape)
                    .border(1.dp, Color.White.copy(0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${data.badge}", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Black)
            }
        }

        Column {
            Render3DIconCanvas(type = data.iconType, modifier = Modifier.size(44.dp))

            Spacer(modifier = Modifier.height(10.dp))
            Text(data.title, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                data.desc,
                color = TextDimGray,
                fontSize = 9.sp,
                lineHeight = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .size(24.dp)
                    .background(Color(0xFF1B1E2B), CircleShape)
                    .border(1.dp, GoldMetallicMain.copy(0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = GoldMetallicLight, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun TogetherWeGrow3DCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = GoldMetallicMain)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF281F0B), CardSurfaceGradientEnd)),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(GoldMetallicMain, Color(0xFF4A3405))),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp),
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
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Canvas(modifier = Modifier.size(44.dp, 30.dp)) {
                // Golden Rising Chart Bars & Arrow
                val barWidth = 6.dp.toPx()
                val barGap = 4.dp.toPx()
                val goldBrush = Brush.verticalGradient(listOf(GoldMetallicLight, GoldMetallicDark))
                
                drawRoundRect(brush = goldBrush, topLeft = Offset(0f, size.height * 0.6f), size = Size(barWidth, size.height * 0.4f), cornerRadius = CornerRadius(4f))
                drawRoundRect(brush = goldBrush, topLeft = Offset(barWidth + barGap, size.height * 0.35f), size = Size(barWidth, size.height * 0.65f), cornerRadius = CornerRadius(4f))
                drawRoundRect(brush = goldBrush, topLeft = Offset((barWidth + barGap) * 2, 0f), size = Size(barWidth, size.height), cornerRadius = CornerRadius(4f))

                // Upward Arrow
                val arrowPath = Path().apply {
                    moveTo(0f, size.height * 0.5f)
                    cubicTo(size.width * 0.4f, size.height * 0.4f, size.width * 0.7f, size.height * 0.1f, size.width, 0f)
                }
                drawPath(arrowPath, color = GoldMetallicLight, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
            }
        }
    }
}

// -------------------------------------------------------------
// ৫. কাস্টম ৩ডি ক্যানভাস অবজেক্ট রেন্ডারার
// -------------------------------------------------------------
@Composable
fun Render3DIconCanvas(type: String, modifier: Modifier) {
    Canvas(modifier = modifier) {
        when (type) {
            "3d_users", "3d_users_deck" -> draw3DClayAvatars()
            "3d_deposit" -> draw3DEmeraldWallet()
            "3d_wallet_blue" -> draw3DBlueWallet()
            "3d_withdraw" -> draw3DStackedCash(isPink = true)
            "3d_cash_green" -> draw3DStackedCash(isPink = false)
            "3d_work" -> draw3DGlowingLightning()
            "3d_profits" -> draw3DPurpleTrendingChart()
            "3d_assets" -> draw3DCyanSafeVault()
            "3d_plane" -> draw3DPaperPlane()
            "3d_phone" -> draw3DSmartphone()
            "3d_gift" -> draw3DGiftBox()
            "3d_keyboard" -> draw3DKeyboard()
            "3d_headset" -> draw3DHeadphones()
            "3d_refresh" -> draw3DCircularRefresh()
            "3d_gear" -> draw3DMechanicalGear()
        }
    }
}

// ৩ডি গোল্ডেন ক্রাউন
fun DrawScope.draw3DCrown(color: Color = GoldMetallicLight) {
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
    drawPath(path, color = color)
}

// ৩ডি ডাটাবেজ সিলিন্ডার
fun DrawScope.draw3DDatabase() {
    val cyanGrad = Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF005B94)))
    drawOval(cyanGrad, topLeft = Offset(0f, 0f), size = Size(size.width, size.height * 0.45f))
    drawRect(cyanGrad, topLeft = Offset(0f, size.height * 0.22f), size = Size(size.width, size.height * 0.5f))
    drawOval(cyanGrad, topLeft = Offset(0f, size.height * 0.55f), size = Size(size.width, size.height * 0.45f))
    
    // Checkmark Orb
    drawCircle(Color(0xFF00E676), radius = size.width * 0.22f, center = Offset(size.width * 0.85f, size.height * 0.75f))
    drawCircle(Color.White.copy(0.6f), radius = size.width * 0.08f, center = Offset(size.width * 0.80f, size.height * 0.70f))
}

// ৩ডি চকচকে নীল পুতুল
fun DrawScope.draw3DClayAvatars() {
    val blueGrad = Brush.radialGradient(listOf(Color(0xFF80D8FF), Color(0xFF0066FF), Color(0xFF002288)))
    drawCircle(blueGrad, radius = size.width * 0.24f, center = Offset(size.width * 0.35f, size.height * 0.35f))
    drawCircle(blueGrad, radius = size.width * 0.28f, center = Offset(size.width * 0.65f, size.height * 0.45f))
    drawCircle(Color.White.copy(0.6f), radius = size.width * 0.08f, center = Offset(size.width * 0.60f, size.height * 0.38f))
}

// ৩ডি পান্না রঙের ওয়ালেট ও গোল্ডেন কয়েন
fun DrawScope.draw3DEmeraldWallet() {
    val greenGrad = Brush.verticalGradient(listOf(Color(0xFF00E676), Color(0xFF006935)))
    drawRoundRect(greenGrad, topLeft = Offset(size.width * 0.1f, size.height * 0.2f), size = Size(size.width * 0.75f, size.height * 0.65f), cornerRadius = CornerRadius(16f))
    
    // Floating 3D Gold Coin
    val goldGrad = Brush.radialGradient(listOf(GoldMetallicLight, GoldMetallicDark))
    drawCircle(goldGrad, radius = size.width * 0.22f, center = Offset(size.width * 0.78f, size.height * 0.55f))
    drawCircle(Color.White.copy(0.7f), radius = size.width * 0.07f, center = Offset(size.width * 0.74f, size.height * 0.48f))
}

// ৩ডি নীল ওয়ালেট
fun DrawScope.draw3DBlueWallet() {
    val blueGrad = Brush.verticalGradient(listOf(Color(0xFF2979FF), Color(0xFF0D47A1)))
    drawRoundRect(blueGrad, topLeft = Offset(size.width * 0.1f, size.height * 0.2f), size = Size(size.width * 0.75f, size.height * 0.65f), cornerRadius = CornerRadius(16f))
    val goldGrad = Brush.radialGradient(listOf(GoldMetallicLight, GoldMetallicDark))
    drawCircle(goldGrad, radius = size.width * 0.2f, center = Offset(size.width * 0.78f, size.height * 0.55f))
}

// ৩ডি টাকার বান্ডিল
fun DrawScope.draw3DStackedCash(isPink: Boolean) {
    val cashGrad = Brush.verticalGradient(listOf(Color(0xFF81C784), Color(0xFF2E7D32)))
    drawRoundRect(cashGrad, topLeft = Offset(size.width * 0.1f, size.height * 0.35f), size = Size(size.width * 0.8f, size.height * 0.45f), cornerRadius = CornerRadius(10f))
    drawRoundRect(cashGrad, topLeft = Offset(size.width * 0.15f, size.height * 0.2f), size = Size(size.width * 0.75f, size.height * 0.45f), cornerRadius = CornerRadius(10f))
    
    // Ribbon Band
    val bandColor = if (isPink) Color(0xFFFF4081) else Color(0xFFFFD54F)
    drawRect(bandColor, topLeft = Offset(size.width * 0.42f, size.height * 0.2f), size = Size(size.width * 0.2f, size.height * 0.6f))
}

// ৩ডি গোল্ডেন লাইটনিং
fun DrawScope.draw3DGlowingLightning() {
    val orbGrad = Brush.radialGradient(listOf(Color(0xFFFFEA00), Color(0xFFFF6D00), Color(0xFF3E2723)))
    drawCircle(orbGrad, radius = size.width * 0.42f, center = Offset(size.width * 0.5f, size.height * 0.5f))
    
    val boltPath = Path().apply {
        moveTo(size.width * 0.55f, size.height * 0.2f)
        lineTo(size.width * 0.35f, size.height * 0.52f)
        lineTo(size.width * 0.52f, size.height * 0.52f)
        lineTo(size.width * 0.45f, size.height * 0.8f)
        lineTo(size.width * 0.68f, size.height * 0.45f)
        lineTo(size.width * 0.52f, size.height * 0.45f)
        close()
    }
    drawPath(boltPath, color = Color.White)
}

// ৩ডি পার্পল আপওয়ার্ড চার্ট
fun DrawScope.draw3DPurpleTrendingChart() {
    val purpleGrad = Brush.verticalGradient(listOf(Color(0xFFE040FB), Color(0xFF651FFF)))
    drawRoundRect(purpleGrad, topLeft = Offset(size.width * 0.15f, size.height * 0.25f), size = Size(size.width * 0.7f, size.height * 0.6f), cornerRadius = CornerRadius(14f))
    
    // 3D Coin Stack
    drawCircle(Brush.radialGradient(listOf(GoldMetallicLight, GoldMetallicDark)), radius = size.width * 0.18f, center = Offset(size.width * 0.75f, size.height * 0.65f))
    drawCircle(Brush.radialGradient(listOf(GoldMetallicLight, GoldMetallicDark)), radius = size.width * 0.18f, center = Offset(size.width * 0.65f, size.height * 0.75f))
}

// ৩ডি সায়ান সেফ লকার
fun DrawScope.draw3DCyanSafeVault() {
    val cyanGrad = Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF006064)))
    drawRoundRect(cyanGrad, topLeft = Offset(size.width * 0.15f, size.height * 0.15f), size = Size(size.width * 0.7f, size.height * 0.7f), cornerRadius = CornerRadius(14f))
    drawCircle(Color(0xFF00363A), radius = size.width * 0.2f, center = Offset(size.width * 0.5f, size.height * 0.5f))
    drawCircle(Color(0xFF80DEEA), radius = size.width * 0.08f, center = Offset(size.width * 0.5f, size.height * 0.5f))
}

// ৩ডি পেপার প্লেন
fun DrawScope.draw3DPaperPlane() {
    val planePath = Path().apply {
        moveTo(size.width * 0.15f, size.height * 0.5f)
        lineTo(size.width * 0.85f, size.height * 0.15f)
        lineTo(size.width * 0.6f, size.height * 0.85f)
        lineTo(size.width * 0.45f, size.height * 0.6f)
        close()
    }
    drawPath(planePath, brush = Brush.linearGradient(listOf(Color(0xFF40C4FF), Color(0xFF0091EA))))
}

// ৩ডি স্মার্টফোন
fun DrawScope.draw3DSmartphone() {
    val phoneGrad = Brush.verticalGradient(listOf(Color(0xFF7C4DFF), Color(0xFF311B92)))
    drawRoundRect(phoneGrad, topLeft = Offset(size.width * 0.25f, size.height * 0.1f), size = Size(size.width * 0.5f, size.height * 0.8f), cornerRadius = CornerRadius(12f))
    drawCircle(Color(0xFFFFEA00), radius = size.width * 0.14f, center = Offset(size.width * 0.5f, size.height * 0.5f))
}

// ৩ডি গিফট বক্স
fun DrawScope.draw3DGiftBox() {
    val boxGrad = Brush.verticalGradient(listOf(Color(0xFFFF5252), Color(0xFFC62828)))
    drawRoundRect(boxGrad, topLeft = Offset(size.width * 0.15f, size.height * 0.25f), size = Size(size.width * 0.7f, size.height * 0.65f), cornerRadius = CornerRadius(12f))
    drawRect(Brush.verticalGradient(listOf(GoldMetallicLight, GoldMetallicDark)), topLeft = Offset(size.width * 0.42f, size.height * 0.25f), size = Size(size.width * 0.16f, size.height * 0.65f))
}

// ৩ডি কীবোর্ড
fun DrawScope.draw3DKeyboard() {
    val kbGrad = Brush.verticalGradient(listOf(Color(0xFFB388FF), Color(0xFF4A148C)))
    drawRoundRect(kbGrad, topLeft = Offset(size.width * 0.1f, size.height * 0.3f), size = Size(size.width * 0.8f, size.height * 0.5f), cornerRadius = CornerRadius(10f))
}

// ৩ডি হেডফোন
fun DrawScope.draw3DHeadphones() {
    val cyanGrad = Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF0091EA)))
    drawArc(cyanGrad, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(size.width * 0.15f, size.height * 0.2f), size = Size(size.width * 0.7f, size.height * 0.6f), style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round))
    drawCircle(cyanGrad, radius = size.width * 0.14f, center = Offset(size.width * 0.2f, size.height * 0.65f))
    drawCircle(cyanGrad, radius = size.width * 0.14f, center = Offset(size.width * 0.8f, size.height * 0.65f))
}

// ৩ডি রিফ্রেশ
fun DrawScope.draw3DCircularRefresh() {
    val redGrad = Brush.radialGradient(listOf(Color(0xFFFF5252), Color(0xFFB71C1C)))
    drawCircle(redGrad, radius = size.width * 0.42f, center = Offset(size.width * 0.5f, size.height * 0.5f))
    drawCircle(Color.White.copy(0.6f), radius = size.width * 0.1f, center = Offset(size.width * 0.4f, size.height * 0.35f))
}

// ৩ডি গিয়ার
fun DrawScope.draw3DMechanicalGear() {
    val cyanGrad = Brush.radialGradient(listOf(Color(0xFF18FFFF), Color(0xFF006064)))
    drawCircle(cyanGrad, radius = size.width * 0.42f, center = Offset(size.width * 0.5f, size.height * 0.5f))
    drawCircle(Color(0xFF07090E), radius = size.width * 0.16f, center = Offset(size.width * 0.5f, size.height * 0.5f))
}

// -------------------------------------------------------------
// লাক্সারি বটম নেভিগেশন বার
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
        containerColor = Color(0xFF090A0E),
        tonalElevation = 10.dp,
        modifier = Modifier.border(
            1.2.dp,
            Brush.horizontalGradient(listOf(Color.Transparent, GoldMetallicMain.copy(0.4f), Color.Transparent)),
            RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
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
                            .size(if (isSelected) 36.dp else 28.dp)
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
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.first,
                        color = if (isSelected) GoldMetallicLight else TextDimGray,
                        fontSize = 10.sp,
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
