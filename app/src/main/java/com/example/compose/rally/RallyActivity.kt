/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        /* BEGIN-4.2 - Set up the NavController */
        // The NavController is the central component when using Navigation in
        // Compose. It keeps track of back stack composable entries, moves the
        // stack forward, enables back stack manipulation, and navigates between
        // destination states. Because NavController is central to navigation,
        // it has to be created as a first step in setting up Compose
        // Navigation.
        // You should always create and place the NavController at the top level
        // in your composable hierarchy, usually within your App composable.
        // Then, all composables that need to reference the NavController have
        // access to it. This follows the principles of state hoisting and
        // ensures the NavController is the main source of truth for navigating
        // between composable screens and maintaining the back stack.
        val navController = rememberNavController()
        /* END-4.2 */
        /* BEGIN-5.3 - Fixing the tab UI */
        val currentBackStack by navController.currentBackStackEntryAsState()
        // Fetch your currentDestination:
        val currentDestination = currentBackStack?.destination
        // var currentScreen: RallyDestination by
        //    remember { mutableStateOf(Overview) }
        // Change the variable to this and use Overview as a backup screen if
        // this returns null
        val currentScreen = rallyTabRowScreens.find {
            it.route == currentDestination?.route
        } ?: Overview
        /* END-5.3 */
        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    /* BEGIN-5 - Integrate RallyTabRow with navigation */
                    // To make your code testable and reusable, it is advised
                    // not to pass the entire navController to your composables
                    // directly. Instead, you should always provide callbacks
                    // that define the exact navigation actions you wish to
                    // trigger.
                    // onTabSelected = { screen -> currentScreen = screen },
                    onTabSelected = { newScreen ->
                        /* BEGIN-5.1 - Launching a single copy of a destination */
                        // navController.navigate(newScreen.route)
                        navController.navigateSingleTopTo(newScreen.route)
                        /* END-5.1 */
                    },
                    /* END-5 */
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            /* BEGIN-4.4 - Calling the NavHost composable with the navigation
            graph */
            // The 3 main parts of Navigation are the NavController, NavGraph,
            // and NavHost. The NavController is always associated with a single
            // NavHost composable. The NavHost acts as a container and is
            // responsible for displaying the current destination of the graph.
            // As you navigate between composables, the content of the NavHost
            // is automatically recomposed. It also links the NavController with
            // a navigation graph (NavGraph) that maps out the composable
            // destinations to navigate between. It is essentially a collection
            // of fetchable destinations.
//            Box(Modifier.padding(innerPadding)) {
//                currentScreen.screen()
//            }
            NavHost(
                navController = navController,
                // Route to know which destination to show when the app is
                // launched
                startDestination = Overview.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // builder for defining and building the navigation graph
                /* BEGIN-4.3 - Routes in Compose Navigation */
                // When using Navigation within Compose, each composable
                // destination in your navigation graph is associated with a
                // route. Routes are represented as Strings that define the path
                // to your composable and guide your navController to land on
                // the right place. You can think of it as an implicit deep link
                // that leads to a specific destination. Each destination must
                // have a unique route.
                /* END-4.3 */
                /* BEGIN-4.5 - Adding destinations to the NavGraph */
                composable(route = Overview.route) {
                    Overview.screen()
                }
                composable(route = Accounts.route) {
                    Accounts.screen()
                }
                composable(route = Bills.route) {
                    Bills.screen()
                }
                /* END-4.5 */
            }
            /* END-4.4 */
        }
    }
}

/* BEGIN-5.1 - Launching a single copy of a destination */
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        /* BEGIN-5.2 - Controlling the navigation options and back stack
        state */
        // Pop up to the start destination of the graph to avoid building up a
        // large stack of destinations on the back stack as you select tabs
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Determines whether this navigation action should restore any state
        // previously saved by PopUpToBuilder.saveState or the popUpToSaveState
        // attribute.
        restoreState = true
        /* END-5.2 */
        // Make sure there will be at most one copy of a given destination on
        // the top of the back stack
        launchSingleTop = true
    }
/* END-5.1 */
