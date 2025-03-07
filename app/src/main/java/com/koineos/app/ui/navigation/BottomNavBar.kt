package com.koineos.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.koineos.app.R
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.navigation.alphabet.AlphabetDestination
import com.koineos.app.ui.navigation.home.HomeDestination
import com.koineos.app.ui.navigation.learn.LearnDestination
import com.koineos.app.ui.navigation.practice.PracticeDestination
import com.koineos.app.ui.navigation.read.ReadDestination
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.utils.AndroidStringProvider

sealed class BottomNavItem(
    val rootDestination: RootDestination,
    val icon: AppIcon,
    @StringRes val labelResId: Int
) {
    data object Home : BottomNavItem(
        rootDestination = RootDestination.HomeRoot,
        icon = AppIcon.Home,
        labelResId = R.string.bottom_nav_bar_home
    )

    data object Learn : BottomNavItem(
        rootDestination = RootDestination.LearnRoot,
        icon = AppIcon.Learn,
        labelResId = R.string.bottom_nav_bar_learn
    )

    data object Practice : BottomNavItem(
        rootDestination = RootDestination.PracticeRoot,
        icon = AppIcon.Practice,
        labelResId = R.string.bottom_nav_bar_practice
    )

    data object Alphabet : BottomNavItem(
        rootDestination = RootDestination.AlphabetRoot,
        icon = AppIcon.Alphabet,
        labelResId = R.string.bottom_nav_bar_alphabet
    )

    data object Read : BottomNavItem(
        rootDestination = RootDestination.ReadRoot,
        icon = AppIcon.Read,
        labelResId = R.string.bottom_nav_bar_read
    )
}

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val context = LocalContext.current
    val stringProvider = AndroidStringProvider(context)
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        HomeDestination.HomeScreen.route,
        LearnDestination.LearnHome.route,
        PracticeDestination.PracticeHome.route,
        AlphabetDestination.AlphabetHome.route,
        ReadDestination.ReadHome.route -> {
            bottomBarState.value = true
        }

        else -> {
            bottomBarState.value = false
        }
    }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Learn,
        BottomNavItem.Practice,
        BottomNavItem.Alphabet,
        BottomNavItem.Read
    )

//    AnimatedVisibility(
//        visible = bottomBarState.value,
//        enter = slideInVertically(initialOffsetY = { it }),
//        exit = slideOutVertically(targetOffsetY = { it }),
//        content = {
    if (bottomBarState.value) {
        Column {
            HorizontalDivider(thickness = 0.5.dp, color = Colors.Outline)
            NavigationBar(containerColor = Colors.BottomNavBarBackground) {
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route == item.rootDestination.route
                    } == true

                    NavigationBarItem(
                        icon = {
                            IconComponent(
                                icon = item.icon,
                                contentDescription = stringProvider.getString(item.labelResId),
                                isSelected = isSelected,
                                tint = if (isSelected) Colors.BottomNavBarSelectedIconColor else Colors.BottomNavBarUnselectedIconColor
                            )
                        },
                        label = {
                            Text(
                                text = stringProvider.getString(item.labelResId),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Colors.BottomNavBarSelectedIconColor,
                            selectedTextColor = Colors.BottomNavBarSelectedTextColor,
                            indicatorColor = Colors.BottomNavBarSelectedIndicatorColor,
                            unselectedIconColor = Colors.BottomNavBarUnselectedIconColor,
                            unselectedTextColor = Colors.BottomNavBarUnselectedTextColor
                        ),
                        onClick = {
                            navController.navigate(item.rootDestination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
//        }
//    )
}