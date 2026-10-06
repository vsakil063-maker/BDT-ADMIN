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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 🎨 Luxury Gold & Deep Obsidian Palette
val BgBlack = Color(0xFF060709)
val CardSurfaceTop = Color(0xFF141620)
val CardSurfaceBottom = Color(0xFF090A0E)

val GoldBright = Color(0xFFFFD54F)
val GoldMetallic = Color(0xFFF5BA42)
val GoldDark = Color(0xFF8B6010)

val BorderBlue = Color(0xFF2979FF)
val BorderGreen = Color(0xFF00E676)
val BorderRose = Color(0xFFFF1744)
val BorderYellow = Color(0xFFFFD600)
val BorderPurple = Color(0xFFD500F9)
val BorderCyan = Color(0xFF00E5FF)

val TextDim = Color(0xFF8A92A6)
val TextWhite = Color(0xFFFFFFFF)

@Composable
fun CockpitDashboardScreen() {
    Scaffold(
        bottomBar = { CockpitBottomBar() },
        containerColor = BgBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF141624), BgBlack, Color(0xFF030406))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            HeaderBar()
            Spacer(modifier = Modifier.height(14.dp))
            WelcomeCard()
            Spacer(modifier = Modifier.height(18.dp))
            OverviewStatsGrid()
            Spacer(modifier = Modifier.height(20.dp))
            WorkspaceDeckGrid()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// -------------------------------------------------------------
// ১. টপ হেডার (গোল্ডেন ৩ডি ক্রাউন ও লোগো)
// -------------------------------------------------------------
@Composable
fun HeaderBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF1F2230), Color(0xFF10121A))), RoundedCornerShape(14.dp))
                .border(1.2.dp, GoldMetallic.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldBright, modifier = Modifier.size(24.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(12.dp, CircleShape, ambientColor = GoldMetallic, spotColor = GoldMetallic)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, Brush.sweepGradient(listOf(GoldBright, GoldDark, GoldBright)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = GoldBright, modifier = Modifier.size(15.dp))
                    Text("RT", color = GoldBright, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("RT GROWTH", color = GoldMetallic, fontWeight = FontWeight.Black, fontSize = 19.sp, letterSpacing = 1.sp)
                Text("COCKPIT", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 13.sp, letterSpacing = 3.sp)
                Text("MASTER COMMAND HUB", color = GoldMetallic.copy(alpha = 0.75f), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF161824), CircleShape)
                    .border(1.2.dp, GoldMetallic.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = GoldBright, modifier = Modifier.size(22.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-3).dp, y = 3.dp)
                        .background(BorderRose, CircleShape)
                        .border(1.5.dp, Color.Black, CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF161824), CircleShape)
                    .border(1.2.dp, GoldMetallic.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = GoldBright, modifier = Modifier.size(22.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// ২. ওয়েলকাম ও ডাটাবেজ ব্যানার
// -------------------------------------------------------------
@Composable
fun WelcomeCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF181B26), CardSurfaceBottom)),
                RoundedCornerShape(22.dp)
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(GoldMetallic, Color(0xFF553C07), GoldMetallic)),
                RoundedCornerShape(22.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Brush.radialGradient(listOf(GoldMetallic.copy(0.35f), Color.Transparent)), CircleShape)
                        .border(1.5.dp, GoldMetallic, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Brush.verticalGradient(listOf(GoldBright, GoldDark)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextDim, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("Admin", color = GoldBright, fontSize = 17.sp, fontWeight = FontWeight.Black)
                    Text("Have a great day!", color = TextDim, fontSize = 10.sp)
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(46.dp)
                    .background(Color(0xFF323647))
            )

            Column(horizontalAlignment = Alignment.End) {
                Text("Database Status", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF00381C))
                        .border(1.dp, BorderGreen, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(BorderGreen, CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Connected", color = BorderGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("typing-5c3e4-default-rtdb", color = TextDim, fontSize = 9.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
    }
}

// -------------------------------------------------------------
// ৩. ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড (৬টি ৩ডি গ্লসি কার্ড)
// -------------------------------------------------------------
@Composable
fun OverviewStatsGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = GoldMetallic, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Overview Statistics", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
        Text("Oct 5, 2026 | 02:03 PM", color = TextDim, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Row 1
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DItemCard(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                change = "+12%",
                subtitle = "Active accounts",
                accentColor = BorderBlue,
                iconType = "users"
            )
            Stat3DItemCard(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                change = "+8%",
                subtitle = "Today: ৳0.00",
                accentColor = BorderGreen,
                iconType = "deposit"
            )
        }
        // Row 2
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DItemCard(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                change = "+6%",
                subtitle = "Today: ৳0.00",
                accentColor = BorderRose,
                iconType = "withdraw"
            )
            Stat3DItemCard(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                change = "+0%",
                subtitle = "Completed tasks",
                accentColor = BorderYellow,
                iconType = "work"
            )
        }
        // Row 3
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DItemCard(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                change = "+15%",
                subtitle = "Total profits",
                accentColor = BorderPurple,
                iconType = "profits"
            )
            Stat3DItemCard(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                change = "+9%",
                subtitle = "Total assets",
                accentColor = BorderCyan,
                iconType = "assets"
            )
        }
    }
}

@Composable
fun Stat3DItemCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    subtitle: String,
    accentColor: Color,
    iconType: String
) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = accentColor)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161824), CardSurfaceBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(accentColor.copy(0.85f), accentColor.copy(0.2f))),
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
                // 3D Rendered Offline Canvas Icon
                Claymorphic3DIcon(type = iconType, color = accentColor)

                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextDim, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        value,
                        color = TextWhite,
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
                        tint = accentColor,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(change, color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
                Text(subtitle, color = TextDim, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// -------------------------------------------------------------
// 3D Claymorphic Canvas Icon Renderer (১০০% অফলাইন এবং ক্র্যাশ-প্রুফ)
// -------------------------------------------------------------
@Composable
fun Claymorphic3DIcon(type: String, color: Color) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = color)
            .background(
                Brush.linearGradient(listOf(color.copy(0.85f), color.copy(0.35f), Color(0xFF0F111A))),
                RoundedCornerShape(14.dp)
            )
            .border(
                1.2.dp,
                Brush.verticalGradient(listOf(Color.White.copy(0.6f), Color.Transparent)),
                RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            "users" -> Icon(Icons.Rounded.Groups, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
            "deposit" -> Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = Color(0xFFFFE082), modifier = Modifier.size(26.dp))
            "withdraw" -> Icon(Icons.Rounded.Payments, contentDescription = null, tint = Color(0xFFFF80AB), modifier = Modifier.size(26.dp))
            "work" -> Icon(Icons.Rounded.Bolt, contentDescription = null, tint = Color(0xFFFFEA00), modifier = Modifier.size(28.dp))
            "profits" -> Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = Color(0xFFEA80FC), modifier = Modifier.size(26.dp))
            "assets" -> Icon(Icons.Rounded.Lock, contentDescription = null, tint = Color(0xFF80D8FF), modifier = Modifier.size(24.dp))
            "send" -> Icon(Icons.Rounded.Send, contentDescription = null, tint = Color(0xFF82B1FF), modifier = Modifier.size(24.dp))
            "gift" -> Icon(Icons.Rounded.CardGiftcard, contentDescription = null, tint = Color(0xFFFF8A80), modifier = Modifier.size(26.dp))
            "keyboard" -> Icon(Icons.Rounded.Keyboard, contentDescription = null, tint = Color(0xFFB388FF), modifier = Modifier.size(26.dp))
            "headset" -> Icon(Icons.Rounded.Headphones, contentDescription = null, tint = Color(0xFF84FFFF), modifier = Modifier.size(26.dp))
            "reset" -> Icon(Icons.Rounded.Sync, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(26.dp))
            "gear" -> Icon(Icons.Rounded.Settings, contentDescription = null, tint = Color(0xFF18FFFF), modifier = Modifier.size(26.dp))
            else -> Icon(Icons.Rounded.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

// -------------------------------------------------------------
// ৪. কমান্ড ডেক ও ৩ডি অ্যাকশন মেনু
// -------------------------------------------------------------
@Composable
fun WorkspaceDeckGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.GridView, contentDescription = null, tint = GoldMetallic, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Command Workspace Deck", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
        Text("Manage • Monitor • Grow", color = GoldDark, fontSize = 10.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    val workspaceList = listOf(
        WorkspaceDeckItem("User Directory", "Manage users, profiles and account activity.", "users", BorderBlue, null),
        WorkspaceDeckItem("Deposits", "Review and manage deposit requests.", "deposit", BorderYellow, null),
        WorkspaceDeckItem("Withdrawals", "Pending withdrawal requests.", "withdraw", BorderGreen, 2),
        WorkspaceDeckItem("Send Money Req", "Handle transfer requests.", "send", BorderBlue, 0),
        WorkspaceDeckItem("Recharges", "Mobile & wallet recharge requests.", "work", BorderYellow, 0),
        WorkspaceDeckItem("Gift Vouchers", "Create and manage voucher codes.", "gift", BorderRose, 31),
        WorkspaceDeckItem("Typing Tasks", "Create tasks and review submissions.", "keyboard", BorderPurple, 0),
        WorkspaceDeckItem("Support Chat", "View conversations and reply to users.", "headset", BorderCyan, 3),
        WorkspaceDeckItem("Balance Reset", "Authorized balance correction tools.", "reset", BorderRose, null),
        WorkspaceDeckItem("System Settings", "Configure security, notifications and database.", "gear", BorderCyan, null)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                WorkspaceDeckCard(modifier = Modifier.weight(1f), item = workspaceList[i])
                if (i + 1 < workspaceList.size) {
                    WorkspaceDeckCard(modifier = Modifier.weight(1f), item = workspaceList[i + 1])
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            TogetherWeGrowCard(modifier = Modifier.weight(1f))
        }
    }
}

data class WorkspaceDeckItem(
    val title: String,
    val desc: String,
    val iconType: String,
    val glowColor: Color,
    val badge: Int?
)

@Composable
fun WorkspaceDeckCard(modifier: Modifier = Modifier, item: WorkspaceDeckItem) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161822), CardSurfaceBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.2.dp,
                Brush.verticalGradient(listOf(GoldMetallic.copy(0.45f), Color(0xFF262A38))),
                RoundedCornerShape(18.dp)
            )
            .clickable { }
            .padding(12.dp)
    ) {
        if (item.badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .shadow(6.dp, CircleShape, spotColor = BorderRose)
                    .background(if (item.badge > 0) BorderRose else Color(0xFF651FFF), CircleShape)
                    .border(1.dp, Color.White.copy(0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${item.badge}", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Black)
            }
        }

        Column {
            Claymorphic3DIcon(type = item.iconType, color = item.glowColor)

            Spacer(modifier = Modifier.height(10.dp))
            Text(item.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                item.desc,
                color = TextDim,
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
                    .border(1.dp, GoldMetallic.copy(0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = GoldBright, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun TogetherWeGrowCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = GoldMetallic)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF241C0A), CardSurfaceBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(GoldMetallic, Color(0xFF4A3405))),
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
                color = GoldBright,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = GoldBright, modifier = Modifier.size(36.dp))
        }
    }
}

// -------------------------------------------------------------
// ৫. লাক্সারি বটম নেভিগেশন বার
// -------------------------------------------------------------
@Composable
fun CockpitBottomBar() {
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
            Brush.horizontalGradient(listOf(Color.Transparent, GoldMetallic.copy(0.4f), Color.Transparent)),
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
                                if (isSelected) GoldMetallic.copy(0.2f) else Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.second,
                            contentDescription = item.first,
                            tint = if (isSelected) GoldBright else TextDim,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.first,
                        color = if (isSelected) GoldBright else TextDim,
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
