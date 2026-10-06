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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 🎨 Luxury Dark Palette
val BgDark = Color(0xFF0A0B0E)
val CardDark = Color(0xFF13151D)
val GoldPrimary = Color(0xFFF5BA42)
val GoldDark = Color(0xFF9E741D)
val TextGray = Color(0xFF8E95A5)
val TextLight = Color(0xFFF1F5F9)

// 🎨 3D Neon Palette
val NeonBlue = Color(0xFF3B82F6)
val NeonGreen = Color(0xFF10B981)
val NeonRose = Color(0xFFF43F5E)
val NeonYellow = Color(0xFFFBBF24)
val NeonPurple = Color(0xFFA855F7)
val NeonCyan = Color(0xFF06B6D4)

@Composable
fun CockpitDashboardScreen() {
    Scaffold(
        bottomBar = { CockpitBottomNavigation() },
        containerColor = BgDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF14161F), BgDark, Color(0xFF060709))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            TopCockpitHeader()
            Spacer(modifier = Modifier.height(14.dp))
            WelcomeDatabaseCard()
            Spacer(modifier = Modifier.height(18.dp))
            OverviewStatisticsSection()
            Spacer(modifier = Modifier.height(20.dp))
            CommandWorkspaceDeckSection()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TopCockpitHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .background(CardDark, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = GoldPrimary)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(2.dp, Brush.sweepGradient(listOf(GoldPrimary, GoldDark, GoldPrimary)), CircleShape)
                    .background(Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                    Text("RT", color = GoldPrimary, fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("RT GROWTH", color = GoldPrimary, fontWeight = FontWeight.Black, fontSize = 17.sp, letterSpacing = 1.sp)
                Text("COCKPIT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                Text("MASTER COMMAND HUB", color = TextGray, fontSize = 8.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, GoldPrimary.copy(alpha = 0.3f), CircleShape)
                    .background(CardDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = GoldPrimary)
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                        .background(NeonRose, CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, GoldPrimary.copy(alpha = 0.3f), CircleShape)
                    .background(CardDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = GoldPrimary)
            }
        }
    }
}

@Composable
fun WelcomeDatabaseCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.2.dp, Brush.horizontalGradient(listOf(GoldPrimary, GoldDark)), RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(Brush.radialGradient(listOf(GoldPrimary.copy(0.3f), Color.Transparent)), CircleShape)
                        .border(1.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Verified, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Welcome Back,", color = TextGray, fontSize = 11.sp)
                    Text("Admin", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Have a great day!", color = TextGray, fontSize = 10.sp)
                }
            }

            Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.DarkGray))

            Column(horizontalAlignment = Alignment.End) {
                Text("Database Status", color = TextLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeonGreen.copy(alpha = 0.2f))
                        .border(1.dp, NeonGreen, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Connected", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("typing-5c3e4-default-rtdb", color = TextGray, fontSize = 9.sp)
            }
        }
    }
}

@Composable
fun OverviewStatisticsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.BarChart, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Overview Statistics", color = TextLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Text("Oct 5, 2026 | 02:03 PM", color = TextGray, fontSize = 10.sp)
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Total Users",
                value = "919",
                change = "+12%",
                subtitle = "Active accounts",
                accentColor = NeonBlue,
                icon = Icons.Rounded.Group
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Total Deposit",
                value = "৳64,597.00",
                change = "+8%",
                subtitle = "Today: ৳0.00",
                accentColor = NeonGreen,
                icon = Icons.Rounded.AccountBalanceWallet
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Total Withdraw",
                value = "৳57,414.00",
                change = "+6%",
                subtitle = "Today: ৳0.00",
                accentColor = NeonRose,
                icon = Icons.Rounded.Payments
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Work Value Done",
                value = "৳0.00",
                change = "+0%",
                subtitle = "Completed tasks",
                accentColor = NeonYellow,
                icon = Icons.Rounded.Bolt
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "User Profits",
                value = "৳310,073.00",
                change = "+15%",
                subtitle = "Total profits",
                accentColor = NeonPurple,
                icon = Icons.Rounded.TrendingUp
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Asset Volume",
                value = "৳53,496.90",
                change = "+9%",
                subtitle = "Total assets",
                accentColor = NeonCyan,
                icon = Icons.Rounded.Lock
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    subtitle: String,
    accentColor: Color,
    icon: ImageVector
) {
    Card(
        modifier = modifier.border(1.2.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(title, color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    Text(value, color = TextLight, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("↑ $change", color = accentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = TextGray, fontSize = 9.sp)
            }
        }
    }
}

@Composable
fun CommandWorkspaceDeckSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.GridView, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Command Workspace Deck", color = TextLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Text("Manage • Monitor • Grow", color = GoldDark, fontSize = 10.sp, fontStyle = FontStyle.Italic)
    }

    Spacer(modifier = Modifier.height(12.dp))

    val workspaceList = listOf(
        WorkspaceData("User Directory", "Manage users, profiles and account activity.", Icons.Rounded.People, NeonBlue, null),
        WorkspaceData("Deposits", "Review and manage deposit requests.", Icons.Rounded.AccountBalanceWallet, NeonYellow, null),
        WorkspaceData("Withdrawals", "Pending withdrawal requests.", Icons.Rounded.Payments, NeonGreen, 2),
        WorkspaceData("Send Money Req", "Handle transfer requests.", Icons.Rounded.Send, NeonBlue, 0),
        WorkspaceData("Recharges", "Mobile & wallet recharge requests.", Icons.Rounded.Bolt, NeonYellow, 0),
        WorkspaceData("Gift Vouchers", "Create and manage voucher codes.", Icons.Rounded.CardGiftcard, NeonRose, 31),
        WorkspaceData("Typing Tasks", "Create tasks and review submissions.", Icons.Rounded.Keyboard, NeonPurple, 0),
        WorkspaceData("Support Chat", "View conversations and reply to users.", Icons.Rounded.Headphones, NeonCyan, 3),
        WorkspaceData("Balance Reset", "Authorized balance correction tools.", Icons.Rounded.Sync, NeonRose, null),
        WorkspaceData("System Settings", "Configure security, notifications and database.", Icons.Rounded.Settings, NeonCyan, null)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in workspaceList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                WorkspaceActionCard(modifier = Modifier.weight(1f), data = workspaceList[i])
                if (i + 1 < workspaceList.size) {
                    WorkspaceActionCard(modifier = Modifier.weight(1f), data = workspaceList[i + 1])
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            TogetherWeGrowCard(modifier = Modifier.weight(1f))
        }
    }
}

data class WorkspaceData(
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val iconTint: Color,
    val badge: Int?
)

@Composable
fun WorkspaceActionCard(modifier: Modifier = Modifier, data: WorkspaceData) {
    Card(
        modifier = modifier
            .border(1.dp, GoldPrimary.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            if (data.badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(18.dp)
                        .background(if (data.badge > 0) NeonRose else Color(0xFF6366F1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${data.badge}", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF1B1E29), CircleShape)
                        .border(1.dp, data.iconTint.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(data.icon, contentDescription = null, tint = data.iconTint, modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(data.title, color = TextLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    data.desc,
                    color = TextGray,
                    fontSize = 9.sp,
                    lineHeight = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(22.dp)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(10.dp))
                }
            }
        }
    }
}

@Composable
fun TogetherWeGrowCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Together\nWe Grow",
                color = GoldPrimary,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun CockpitBottomNavigation() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home" to Icons.Rounded.Home, "Users" to Icons.Rounded.Group, "Reports" to Icons.Rounded.BarChart, "Settings" to Icons.Rounded.Settings)

    NavigationBar(
        containerColor = Color(0xFF0D0E14),
        tonalElevation = 8.dp,
        modifier = Modifier.border(1.dp, GoldPrimary.copy(alpha = 0.2f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { selectedItem = index },
                icon = {
                    Icon(
                        item.second,
                        contentDescription = item.first,
                        tint = if (isSelected) GoldPrimary else TextGray
                    )
                },
                label = {
                    Text(
                        item.first,
                        color = if (isSelected) GoldPrimary else TextGray,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = GoldPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}
