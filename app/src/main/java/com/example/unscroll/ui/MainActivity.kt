package com.example.unscroll.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.unscroll.ui.appPicker.AppPickerScreen
import com.example.unscroll.ui.limitsetup.LimitSetupScreen
import com.example.unscroll.ui.monitoring.MonitoredAppsScreen
import com.example.unscroll.ui.navigation.Routes
import com.example.unscroll.ui.theme.UnscrollTheme
import com.example.unscroll.ui.theme.unscrollStatusColors
import com.example.unscroll.ui.theme.utils.LightDarkPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            UnscrollTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Routes.MONITORED_APPS,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable(Routes.MONITORED_APPS) {
                            MonitoredAppsScreen(
                                onAddApp = {
                                    navController.navigate(Routes.APP_PICKER)
                                }
                            )
                        }

                        composable(Routes.APP_PICKER) {
                            AppPickerScreen(
                                onAppSelected = { packageName, appName ->
                                    navController.navigate(
                                        Routes.limitSetup(packageName, appName)
                                    )
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = Routes.LIMIT_SETUP,
                            arguments = listOf(
                                navArgument("packageName") {
                                    type = NavType.StringType
                                },
                                navArgument("appName") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val packageName =
                                backStackEntry.arguments?.getString("packageName")!!

                            val appName =
                                backStackEntry.arguments?.getString("appName")!!

                            LimitSetupScreen(
                                packageName = packageName,
                                appName = appName,
                                onSaved = {
                                    navController.popBackStack(
                                        route = Routes.MONITORED_APPS,
                                        inclusive = false
                                    )
                                },
                                onCancel = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = MaterialTheme.unscrollStatusColors.warning
    )
}

@Composable
@LightDarkPreview
fun GreetingPreview() {
    UnscrollTheme {
        Greeting("Android")
    }
}
