package com.example.businesstaxcalculator.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import com.example.businesstaxcalculator.di.AppContainer
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.businesstaxcalculator.resources.*
import com.example.businesstaxcalculator.presentation.home.HomeViewModel
import com.example.businesstaxcalculator.presentation.home.screen.HomeScreen
import com.example.businesstaxcalculator.presentation.history.HistoryViewModel
import com.example.businesstaxcalculator.presentation.history.screen.HistoryScreen
import com.example.businesstaxcalculator.presentation.profile.ProfileViewModel
import com.example.businesstaxcalculator.presentation.profile.screen.ProfileScreen
import com.example.businesstaxcalculator.presentation.settings.SettingsViewModel
import com.example.businesstaxcalculator.presentation.settings.screen.SettingsScreen

private enum class Tab(val title: StringResource, val icon: DrawableResource) {
    Home(Res.string.home, Res.drawable.icon_home),
    History(Res.string.history, Res.drawable.ic_money_),
    Settings(Res.string.settings, Res.drawable.icon_settings),
    Profile(Res.string.profile, Res.drawable.icon_profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessTaxApp(
    container: AppContainer, biometricAvailable: Boolean
) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
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
                val model = viewModel {
                    HomeViewModel(container.validator, container.settings, container.historyRepository)
                }
                HomeScreen(model)
            }
            composable(Tab.History.name) {
                val model = viewModel { HistoryViewModel(container.historyRepository, container.settings) }
                HistoryScreen(model)
            }
            composable(Tab.Settings.name) {
                val model = viewModel {
                    SettingsViewModel(
                        container.validator, container.dataStorage, container.settings,
                        container.credentials, container.historyRepository, container.currencyRateRepository
                    )
                }
                SettingsScreen(model, biometricAvailable)
            }
            composable(Tab.Profile.name) {
                val model = viewModel { ProfileViewModel(container.settings) }
                ProfileScreen(model)
            }
        }
    }
}
