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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Contract for information needed on every Rally navigation destination
 */
interface RallyDestination {
    val icon: ImageVector
    val route: String
    /* BEGIN-6 - Extracting screen composables from RallyDestinations */
    // Until now, for simplicity, we were using the screen property from the
    // RallyDestination interface and the screen objects extending from it, to
    // add the composable UI in the NavHost.
    // However, the following steps in this codelab require passing additional
    // information to your composable screens directly. In a production
    // environment, there will certainly be even more data that would need to be
    // passed.
    // The correct- and cleaner!- way of achieving this would be to add the
    // composables directly in the NavHost navigation graph and extract them
    // from the RallyDestination. After that, RallyDestination and the screen
    // objects would only hold navigation-specific information, like the icon
    // and route, and would be decoupled from anything Compose UI related.
    // val screen: @Composable () -> Unit
    /* END-6 */
}

/**
 * Rally app navigation destinations
 */
object Overview : RallyDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "overview"
    /* BEGIN-6 - Extracting screen composables from RallyDestinations */
    // override val screen: @Composable () -> Unit = { OverviewScreen() }
    /* END-6 */
}

object Accounts : RallyDestination {
    override val icon = Icons.Filled.AttachMoney
    override val route = "accounts"
    /* BEGIN-6 - Extracting screen composables from RallyDestinations */
    // override val screen: @Composable () -> Unit = { AccountsScreen() }
    /* END-6 */
}

object Bills : RallyDestination {
    override val icon = Icons.Filled.MoneyOff
    override val route = "bills"
    /* BEGIN-6 - Extracting screen composables from RallyDestinations */
    // override val screen: @Composable () -> Unit = { BillsScreen() }
    /* END-6 */
}

object SingleAccount : RallyDestination {
    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
    // part of the RallyTabRow selection
    override val icon = Icons.Filled.Money
    override val route = "single_account"
    /* BEGIN-6 - Extracting screen composables from RallyDestinations */
    // override val screen: @Composable () -> Unit = { SingleAccountScreen() }
    /* END-6 */
    const val accountTypeArg = "account_type"
}

// Screens to be displayed in the top RallyTabRow
val rallyTabRowScreens = listOf(Overview, Accounts, Bills)

