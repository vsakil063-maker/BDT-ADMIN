package com.rtgrowth.cockpit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ==========================================
// 🎨 ছবির সাথে ১০০% মেলানো কালার প্যালেট
// ==========================================
val DarkCanvasBg = Color(0xFF07080B)
val CardDarkBgTop = Color(0xFF13151E)
val CardDarkBgBottom = Color(0xFF090A0E)

val GoldAccentBright = Color(0xFFFFD54F)
val GoldAccentMain = Color(0xFFF3B737)
val GoldAccentDark = Color(0xFF8D6210)

val NeonBlueBorder = Color(0xFF2979FF)
val NeonGreenBorder = Color(0xFF00E676)
val NeonRoseBorder = Color(0xFFFF1744)
val NeonYellowBorder = Color(0xFFFFD600)
val NeonPurpleBorder = Color(0xFFD500F9)
val NeonCyanBorder = Color(0xFF00E5FF)

val TextMutedGray = Color(0xFF8E95A5)
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
                        colors = listOf(Color(0xFF141622), DarkCanvasBg, Color(0xFF030406))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // ১. টপ হেডার
            MasterCockpitTopHeader()

            Spacer(modifier = Modifier.height(14.dp))

            // ২. ওয়েলকাম ও ডাটাবেজ স্ট্যাটাস ব্যানার
            WelcomeDatabaseLuxuryCard()

            Spacer(modifier = Modifier.height(18.dp))

            // ৩. ওভারভিউ স্ট্যাটিস্টিক্স (৬টি ৩ডি কার্ড)
            OverviewStatistics3DGrid()

            Spacer(modifier = Modifier.height(20.dp))

            // ৪. কমান্ড ওয়ার্কস্পেস ডেক
            CommandWorkspaceDeck3DGrid()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ==========================================
// ১. টপ হেডার
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
                .shadow(8.dp, RoundedCornerShape(14.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF1D202D), Color(0xFF10121A))), RoundedCornerShape(14.dp))
                .border(1.2.dp, GoldAccentMain.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldAccentBright, modifier = Modifier.size(24.dp))
        }

        // লোগো (গোল্ডেন ৩ডি ক্রাউন ও RT)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(12.dp, CircleShape, ambientColor = GoldAccentMain, spotColor = GoldAccentMain)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, Brush.sweepGradient(listOf(GoldAccentBright, GoldAccentDark, GoldAccentBright)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = GoldAccentBright, modifier = Modifier.size(14.dp))
                    Text("RT", color = GoldAccentBright, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("RT GROWTH", color = GoldAccentMain, fontWeight = FontWeight.Black, fontSize = 19.sp, letterSpacing = 1.sp)
                Text("COCKPIT", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 13.sp, letterSpacing = 3.sp)
                Text("MASTER COMMAND HUB", color = GoldAccentMain.copy(alpha = 0.75f), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
            }
        }

        // নোটিফিকেশন ও প্রোফাইল
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF161823), CircleShape)
                    .border(1.2.dp, GoldAccentMain.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = GoldAccentBright, modifier = Modifier.size(22.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-3).dp, y = 3.dp)
                        .background(NeonRoseBorder, CircleShape)
                        .border(1.5.dp, Color.Black, CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF161823), CircleShape)
                    .border(1.2.dp, GoldAccentMain.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = GoldAccentBright, modifier = Modifier.size(22.dp))
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
            .shadow(12.dp, RoundedCornerShape(22.dp), ambientColor = GoldAccentMain.copy(0.2f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF181B26), CardDarkBgBottom)),
                RoundedCornerShape(22.dp)
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(GoldAccentMain, Color(0xFF533B07), GoldAccentMain)),
                RoundedCornerShape(22.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Admin Info with 3D Crown Orb
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Brush.radialGradient(listOf(GoldAccentMain.copy(0.35f), Color.Transparent)), CircleShape)
                        .border(1.5.dp, GoldAccentMain, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Brush.verticalGradient(listOf(GoldAccentBright, GoldAccentDark)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextMutedGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("Admin", color = GoldAccentBright, fontSize = 17.sp, fontWeight = FontWeight.Black)
                    Text("Have a great day!", color = TextMutedGray, fontSize = 10.sp)
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(46.dp)
                    .background(Color(0xFF323647))
            )

            // Database Info with 3D Cylinder
            Column(horizontalAlignment = Alignment.End) {
                Text("Database Status", color = TextPureWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF00381C))
                        .border(1.dp, NeonGreenBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(NeonGreenBorder, CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Connected", color = NeonGreenBorder, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("typing-5c3e4-default-rtdb", color = TextMutedGray, fontSize = 9.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
    }
}

// ==========================================
// ৩. ওভারভিউ স্ট্যাটিস্টিক্স গ্রিড (৬টি ৩ডি কার্ড)
// ==========================================
@Composable
fun OverviewStatistics3DGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = GoldAccentMain, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Overview Statistics", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
        Text("Oct 5, 2026 | 02:03 PM", color = TextMutedGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Row 1: Users & Deposit
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DGlossyCard(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                change = "+12%",
                subtitle = "Active accounts",
                borderColor = NeonBlueBorder,
                imageUrl = "https://cdn3d.iconscout.com/3d/premium/thumb/user-group-5527370-4621251.png"
            )
            Stat3DGlossyCard(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                change = "+8%",
                subtitle = "Today: ৳0.00",
                borderColor = NeonGreenBorder,
                imageUrl = "https://cdn3d.iconscout.com/3d/premium/thumb/wallet-with-coins-5591321-4663953.png"
            )
        }
        // Row 2: Withdraw & Work Done
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DGlossyCard(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                change = "+6%",
                subtitle = "Today: ৳0.00",
                borderColor = NeonRoseBorder,
                imageUrl = "https://cdn3d.iconscout.com/3d/premium/thumb/money-stack-5527364-4621245.png"
            )
            Stat3DGlossyCard(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                change = "+0%",
                subtitle = "Completed tasks",
                borderColor = NeonYellowBorder,
                imageUrl = "https://cdn3d.iconscout.com/3d/premium/thumb/flash-5590924-4663675.png"
            )
        }
        // Row 3: Profits & Assets
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat3DGlossyCard(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                change = "+15%",
                subtitle = "Total profits",
                borderColor = NeonPurpleBorder,
                imageUrl = "https://cdn3d.iconscout.com/3d/premium/thumb/growth-chart-5527369-4621250.png"
            )
            Stat3DGlossyCard(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                change = "+9%",
                subtitle = "Total assets",
                borderColor = NeonCyanBorder,
                imageUrl = "https://cdn3d.iconscout.com/3d/premium/thumb/safe-box-5590918-4663669.png"
            )
        }
    }
}

@Composable
fun Stat3DGlossyCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    subtitle: String,
    borderColor: Color,
    imageUrl: String
) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = borderColor)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161824), CardDarkBgBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(borderColor.copy(0.85f), borderColor.copy(0.2f))),
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
                // 3D Rendered Asset Image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.size(46.dp),
                    contentScale = ContentScale.Fit
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextMutedGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                        tint = borderColor,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(change, color = borderColor, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
                Text(subtitle, color = TextMutedGray, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ==========================================
// ৪. কমান্ড ডেক ও ৩ডি অ্যাকশন মেনু
// ==========================================
@Composable
fun CommandWorkspaceDeck3DGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.GridView, contentDescription = null, tint = GoldAccentMain, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Command Workspace Deck", color = TextPureWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
        }
        Text("Manage • Monitor • Grow", color = GoldAccentDark, fontSize = 10.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    val workspaceList = listOf(
        Workspace3DItem("User Directory", "Manage users, profiles and account activity.", "https://cdn3d.iconscout.com/3d/premium/thumb/user-profile-5527362-4621243.png", null),
        Workspace3DItem("Deposits", "Review and manage deposit requests.", "https://cdn3d.iconscout.com/3d/premium/thumb/wallet-5527367-4621248.png", null),
        Workspace3DItem("Withdrawals", "Pending withdrawal requests.", "https://cdn3d.iconscout.com/3d/premium/thumb/money-5527363-4621244.png", 2),
        Workspace3DItem("Send Money Req", "Handle transfer requests.", "https://cdn3d.iconscout.com/3d/premium/thumb/paper-plane-5527359-4621240.png", 0),
        Workspace3DItem("Recharges", "Mobile & wallet recharge requests.", "https://cdn3d.iconscout.com/3d/premium/thumb/smartphone-5527366-4621247.png", 0),
        Workspace3DItem("Gift Vouchers", "Create and manage voucher codes.", "https://cdn3d.iconscout.com/3d/premium/thumb/gift-box-5527368-4621249.png", 31),
        Workspace3DItem("Typing Tasks", "Create tasks and review submissions.", "https://cdn3d.iconscout.com/3d/premium/thumb/keyboard-5527365-4621246.png", 0),
        Workspace3DItem("Support Chat", "View conversations and reply to users.", "https://cdn3d.iconscout.com/3d/premium/thumb/headphone-5527360-4621241.png", 3),
        Workspace3DItem("Balance Reset", "Authorized balance correction tools.", "https://cdn3d.iconscout.com/3d/premium/thumb/refresh-5527361-4621242.png", null),
        Workspace3DItem("System Settings", "Configure security, notifications and database.", "https://cdn3d.iconscout.com/3d/premium/thumb/setting-5527358-4621239.png", null)
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
        // Together We Grow Golden Banner
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            TogetherWeGrowGoldenCard(modifier = Modifier.weight(1f))
        }
    }
}

data class Workspace3DItem(
    val title: String,
    val desc: String,
    val imageUrl: String,
    val badge: Int?
)

@Composable
fun Workspace3DCard(modifier: Modifier = Modifier, item: Workspace3DItem) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161822), CardDarkBgBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.2.dp,
                Brush.verticalGradient(listOf(GoldAccentMain.copy(0.45f), Color(0xFF262A38))),
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
                    .shadow(6.dp, CircleShape, spotColor = NeonRoseBorder)
                    .background(if (item.badge > 0) NeonRoseBorder else Color(0xFF651FFF), CircleShape)
                    .border(1.dp, Color.White.copy(0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${item.badge}", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Black)
            }
        }

        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.size(44.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(item.title, color = TextPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                item.desc,
                color = TextMutedGray,
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
                    .border(1.dp, GoldAccentMain.copy(0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = GoldAccentBright, modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun TogetherWeGrowGoldenCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = GoldAccentMain)
            .background(
                Brush.verticalGradient(listOf(Color(0xFF241C0A), CardDarkBgBottom)),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.3.dp,
                Brush.verticalGradient(listOf(GoldAccentMain, Color(0xFF4A3405))),
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
                color = GoldAccentBright,
                fontWeight = FontWeight.Black,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = GoldAccentBright, modifier = Modifier.size(36.dp))
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
            Brush.horizontalGradient(listOf(Color.Transparent, GoldAccentMain.copy(0.4f), Color.Transparent)),
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
                                if (isSelected) GoldAccentMain.copy(0.2f) else Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.second,
                            contentDescription = item.first,
                            tint = if (isSelected) GoldAccentBright else TextMutedGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.first,
                        color = if (isSelected) GoldAccentBright else TextMutedGray,
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
