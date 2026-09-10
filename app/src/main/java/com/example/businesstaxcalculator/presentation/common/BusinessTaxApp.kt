package com.example.businesstaxcalculator.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.presentation.home.HomeViewModel
import com.example.businesstaxcalculator.presentation.home.screen.HomeScreen
import com.example.businesstaxcalculator.presentation.history.HistoryViewModel
import com.example.businesstaxcalculator.presentation.history.screen.HistoryScreen
import com.example.businesstaxcalculator.presentation.profile.ProfileViewModel
import com.example.businesstaxcalculator.presentation.profile.screen.ProfileScreen
import com.example.businesstaxcalculator.presentation.settings.SettingsViewModel
import com.example.businesstaxcalculator.presentation.settings.screen.SettingsScreen

private enum class Tab(val title: Int, val icon: Int) {
    Home(R.string.home, R.drawable.icon_home),
    History(R.string.history, R.drawable.ic_money_),
    Settings(R.string.settings, R.drawable.icon_settings),
    Profile(R.string.profile, R.drawable.icon_profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessTaxApp(
    homeViewModel: HomeViewModel,
    historyViewModel: HistoryViewModel,
    settingsViewModel: SettingsViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = entry?.destination?.route == tab.name,
                        onClick = {
                            navController.navigate(tab.name) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(painterResource(tab.icon), contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text(stringResource(tab.title)) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = Tab.Home.name, modifier = Modifier.padding(padding).consumeWindowInsets(padding)) {
            composable(Tab.Home.name) {
                HomeScreen(homeViewModel)
            }
            composable(Tab.History.name) { HistoryScreen(historyViewModel) }
            composable(Tab.Settings.name) { SettingsScreen(settingsViewModel) }
            composable(Tab.Profile.name) { ProfileScreen(profileViewModel) }
        }
    }
}
