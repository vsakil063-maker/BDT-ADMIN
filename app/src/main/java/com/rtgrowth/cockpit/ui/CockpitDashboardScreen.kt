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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 🎨 Luxury Gold & Deep 3D Neon Palette
// ==========================================
val DarkCanvasBg = Color(0xFF07080B)
val DarkCardSurface = Color(0xFF11131A)
val DarkCardSurfaceEnd = Color(0xFF0B0C10)

val MetallicGoldLight = Color(0xFFFFDF7A)
val MetallicGold = Color(0xFFF3B737)
val MetallicGoldDark = Color(0xFF9E7015)

// 3D Neon Accent Glow Colors
val GlowBlue = Color(0xFF2979FF)
val GlowGreen = Color(0xFF00E676)
val GlowRose = Color(0xFFFF1744)
val GlowYellow = Color(0xFFFFC400)
val GlowPurple = Color(0xFFD500F9)
val GlowCyan = Color(0xFF00E5FF)

val TextMuted = Color(0xFF8C93A4)
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
                        colors = listOf(Color(0xFF13151F), DarkCanvasBg, Color(0xFF040507))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // ১. টপ বার
            MasterCockpitTopHeader()

            Spacer(modifier = Modifier.height(14.dp))

            // ২. ওয়েলকাম ও ডাটাবেজ ব্যানার
            WelcomeDatabaseLuxuryCard()

            Spacer(modifier = Modifier.height(18.dp))

            // ৩. ৬টি ৩ডি স্ট্যাটিস্টিক্স কার্ড
            OverviewStatistics3DSection()

            Spacer(modifier = Modifier.height(20.dp))

            // ৪. কমান্ড ডেক ও ৩ডি অ্যাকশন মেনু
            CommandWorkspaceDeck3DSection()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ==========================================
// ১. টপ হেডার (গোল্ডেন ক্রাউন ও ব্র্যান্ডিং)
// ==========================================
@Composable
fun MasterCockpitTopHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // মেনু আইকন
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(6.dp, RoundedCornerShape(14.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF1C1F2B), Color(0xFF10121A))), RoundedCornerShape(14.dp))
                .border(1.2.dp, Brush.linearGradient(listOf(MetallicGold.copy(0.6f), Color.Transparent)), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = MetallicGold, modifier = Modifier.size(24.dp))
        }

        // লোগো
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(10.dp, CircleShape, ambientColor = MetallicGold, spotColor = MetallicGold)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, Brush.sweepGradient(listOf(MetallicGoldLight, MetallicGoldDark, MetallicGoldLight)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.size(14.dp, 10.dp)) {
                        val path = Path().apply {
                            moveTo(0f, size.height)
                            lineTo(0f, 2f)
                            lineTo(size.width * 0.25f, size.height * 0.5f)
                            lineTo(size.width * 0.5f, 0f)
                            lineTo(size.width * 0.75f, size.height * 0.5f)
                            lineTo(size.width, 2f)
                            lineTo(size.width, size.height)
                            close()
                        }
                        drawPath(path, color = MetallicGoldLight)
                    }
                    Text(
                        "RT",
                        color = MetallicGoldLight,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    "RT GROWTH",
                    color = MetallicGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    "COCKPIT",
                    color = TextPureWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    letterSpacing = 2.5.sp
                )
                Text(
                    "MASTER COMMAND HUB",
                    color = MetallicGold.copy(alpha = 0.7f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }
        }

        // নোটিফিকেশন ও প্রোফাইল
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Brush.verticalGradient(listOf(Color(0xFF1E212D), Color(0xFF10121A))), CircleShape)
                    .border(1.2.dp, MetallicGold.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = MetallicGold, modifier = Modifier.size(22.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-3).dp, y = 3.dp)
                        .background(GlowRose, CircleShape)
                        .border(1.5.dp, Color.Black, CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Brush.verticalGradient(listOf(Color(0xFF1E212D), Color(0xFF10121A))), CircleShape)
                    .border(1.2.dp, MetallicGold.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = MetallicGold, modifier = Modifier.size(22.dp))
            }
        }
    }
}

// ==========================================
// ২. ওয়েলকাম ও ডাটাবেজ ব্যানার
// ==========================================
@Composable
fun WelcomeDatabaseLuxuryCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = MetallicGold.copy(0.2f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF181A24), DarkCardSurfaceEnd)),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(MetallicGold, Color(0xFF5A4108), MetallicGold)),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Admin Info with 3D Gold Crown
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            Brush.radialGradient(listOf(MetallicGold.copy(0.35f), Color.Transparent)),
                            CircleShape
                        )
                        .border(1.5.dp, MetallicGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Brush.verticalGradient(listOf(MetallicGoldLight, MetallicGoldDark)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("Admin", color = MetallicGoldLight, fontSize = 17.sp, fontWeight = FontWeight.Black)
                    Text("Have a great day!", color = TextMuted, fontSize = 10.sp)
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(44.dp)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xFF4A4E61), Color.Transparent)))
            )

            // Database Info with 3D Cylinder
            Column(horizontalAlignment = Alignment.End) {
                Text("Database Status", color = TextPureWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF00381C), Color(0xFF006B35))))
                        .border(1.dp, GlowGreen, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(GlowGreen, CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Connected", color = GlowGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("typing-5c3e4-default-rtdb", color = TextMuted, fontSize = 9.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
    }
}

// ==========================================
// ৩. ওভারভিউ স্ট্যাটিস্টিক্স সেকশন (৬টি ৩ডি কার্ড)
// ==========================================
@Composable
fun OverviewStatistics3DSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = MetallicGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Overview Statistics", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
        Text("Oct 5, 2026 | 02:03 PM", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DCard(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                percentage = "+12%",
                subtitle = "Active accounts",
                accentGlow = GlowBlue,
                iconType = "users"
            )
            Stat3DCard(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                percentage = "+8%",
                subtitle = "Today: ৳0.00",
                accentGlow = GlowGreen,
                iconType = "deposit"
            )
        }
        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DCard(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                percentage = "+6%",
                subtitle = "Today: ৳0.00",
                accentGlow = GlowRose,
                iconType = "withdraw"
            )
            Stat3DCard(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                percentage = "+0%",
                subtitle = "Completed tasks",
                accentGlow = GlowYellow,
                iconType = "work"
            )
        }
        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DCard(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                percentage = "+15%",
                subtitle = "Total profits",
                accentGlow = GlowPurple,
                iconType = "profits"
            )
            Stat3DCard(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                percentage = "+9%",
                subtitle = "Total assets",
                accentGlow = GlowCyan,
                iconType = "assets"
            )
        }
    }
}

@Composable
fun Stat3DCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    percentage: String,
    subtitle: String,
    accentGlow: Color,
    iconType: String
) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(18.dp), ambientColor = accentGlow.copy(0.3f), spotColor = accentGlow)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161823), DarkCardSurfaceEnd)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(accentGlow.copy(0.8f), accentGlow.copy(0.2f))),
                RoundedCornerShape(18.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 3D Glossy Icon Box
                Glossy3DIcon(iconType = iconType, glowColor = accentGlow)

                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                // Fixed Arrow Upward Icon (No more broken "ij" glitch)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = null,
                        tint = accentGlow,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(percentage, color = accentGlow, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
                Text(subtitle, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ৩ডি গ্লসি আইকন কম্পোনেন্ট
@Composable
fun Glossy3DIcon(iconType: String, glowColor: Color) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = glowColor)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.85f),
                        glowColor.copy(alpha = 0.4f),
                        Color(0xFF0D0F17)
                    )
                ),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Brush.verticalGradient(listOf(Color.White.copy(0.6f), Color.Transparent)),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        val (icon, tint) = when (iconType) {
            "users" -> Icons.Rounded.Groups to Color.White
            "deposit" -> Icons.Rounded.AccountBalanceWallet to Color(0xFFFFD54F)
            "withdraw" -> Icons.Rounded.Payments to Color(0xFFFF80AB)
            "work" -> Icons.Rounded.Bolt to Color(0xFFFFEA00)
            "profits" -> Icons.Rounded.TrendingUp to Color(0xFFEA80FC)
            "assets" -> Icons.Rounded.Lock to Color(0xFF80D8FF)
            else -> Icons.Rounded.Star to Color.White
        }
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
    }
}

// ==========================================
// ৪. কমান্ড ডেক ও ৩ডি অ্যাকশন মেনু
// ==========================================
@Composable
fun CommandWorkspaceDeck3DSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.GridView, contentDescription = null, tint = MetallicGold, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Command Workspace Deck", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
        Text("Manage • Monitor • Grow", color = MetallicGoldDark, fontSize = 10.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    val workspaceList = listOf(
        WorkspaceActionItem("User Directory", "Manage users, profiles and account activity.", "users", GlowBlue, null),
        WorkspaceActionItem("Deposits", "Review and manage deposit requests.", "deposit", GlowYellow, null),
        WorkspaceActionItem("Withdrawals", "Pending withdrawal requests.", "withdraw", GlowGreen, 2),
        WorkspaceActionItem("Send Money Req", "Handle transfer requests.", "send", GlowBlue, 0),
        WorkspaceActionItem("Recharges", "Mobile & wallet recharge requests.", "recharge", GlowYellow, 0),
        WorkspaceActionItem("Gift Vouchers", "Create and manage voucher codes.", "gift", GlowRose, 31),
        WorkspaceActionItem("Typing Tasks", "Create tasks and review submissions.", "typing", GlowPurple, 0),
        WorkspaceActionItem("Support Chat", "View conversations and reply to users.", "chat", GlowCyan, 3),
        WorkspaceActionItem("Balance Reset", "Authorized balance correction tools.", "reset", GlowRose, null),
        WorkspaceActionItem("System Settings", "Configure security, notifications and database.", "settings", GlowCyan, null)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Workspace3DCard(modifier = Modifier.weight(1f), item = workspaceList[i])
                if (i + 1 < workspaceList.size) {
                    Workspace3DCard(modifier = Modifier.weight(1f), item = workspaceList[i + 1])
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

data class WorkspaceActionItem(
    val title: String,
    val desc: String,
    val iconType: String,
    val glowColor: Color,
    val badge: Int?
)

@Composable
fun Workspace3DCard(modifier: Modifier = Modifier, item: WorkspaceActionItem) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161822), DarkCardSurfaceEnd)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.2.dp,
                Brush.verticalGradient(listOf(MetallicGold.copy(0.4f), Color(0xFF262A38))),
                RoundedCornerShape(18.dp)
            )
            .clickable { }
            .padding(12.dp)
    ) {
        // Glowing Badge
        if (item.badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .shadow(6.dp, CircleShape, spotColor = GlowRose)
                    .background(if (item.badge > 0) GlowRose else Color(0xFF651FFF), CircleShape)
                    .border(1.dp, Color.White.copy(0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${item.badge}", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Black)
            }
        }

        Column {
            Glossy3DIcon(iconType = item.iconType, glowColor = item.glowColor)

            Spacer(modifier = Modifier.height(10.dp))
            Text(item.title, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                item.desc,
                color = TextMuted,
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
                    .border(1.dp, MetallicGold.copy(0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MetallicGold, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun TogetherWeGrow3DCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = MetallicGold)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF241C0A), DarkCardSurfaceEnd)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(MetallicGold, Color(0xFF4A3405))),
                RoundedCornerShape(18.dp)
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
                color = MetallicGoldLight,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = MetallicGold, modifier = Modifier.size(36.dp))
        }
    }
}

// ==========================================
// ৫. লাক্সারি বটম নেভিগেশন বার
// ==========================================
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
            Brush.horizontalGradient(listOf(Color.Transparent, MetallicGold.copy(0.4f), Color.Transparent)),
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
                                if (isSelected) MetallicGold.copy(0.2f) else Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.second,
                            contentDescription = item.first,
                            tint = if (isSelected) MetallicGoldLight else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.first,
                        color = if (isSelected) MetallicGoldLight else TextMuted,
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
