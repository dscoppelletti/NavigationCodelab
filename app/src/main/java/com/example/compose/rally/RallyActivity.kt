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
            /* BEGIN-9 - Extract the NavHost into RallyNavHost */
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
            /* END-9 */
        }
    }
}
