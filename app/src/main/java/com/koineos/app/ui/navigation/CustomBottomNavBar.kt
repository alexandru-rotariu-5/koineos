package com.koineos.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.utils.AndroidStringProvider

@Composable
fun CustomBottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stringProvider = AndroidStringProvider(context)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Alphabet,
        BottomNavItem.Vocabulary,
        BottomNavItem.Learn,
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Colors.Surface,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column {
            // Top divider
            HorizontalDivider(
                thickness = 0.5.dp,
                color = Colors.Outline
            )

            // Indicator row - contains the sliding indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.rootDestination.route
                    } == true

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 1.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (isSelected) {
                            // The indicator pill that shows the selected item
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(3.dp)
                                    .clip(CircleShape)
                                    .background(brush = Colors.PrimaryGradient)
                            )
                        }
                    }
                }
            }

            // Navigation items
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.rootDestination.route
                    } == true

                    // Each navigation item
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(item.rootDestination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        // Icon with background
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    color = if (isSelected) Colors.Primary.copy(alpha = 0.1f) else Colors.Surface
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconComponent(
                                icon = item.icon,
                                contentDescription = stringProvider.getString(item.labelResId),
                                isSelected = isSelected,
                                tint = if (isSelected) Colors.Primary else Colors.OnSurfaceVariant,
                                size = 24.dp
                            )
                        }

                        // Small space between icon and text
                        Spacer(modifier = Modifier.height(2.dp))

                        // Label text
                        Text(
                            text = stringProvider.getString(item.labelResId),
                            color = if (isSelected) Colors.Primary else Colors.OnSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomBottomNavBarPreview() {
    KoineosTheme {
        CustomBottomNavBar(
            navController = rememberNavController()
        )
    }
}