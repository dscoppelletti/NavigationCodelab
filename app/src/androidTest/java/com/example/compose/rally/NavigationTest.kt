package com.example.compose.rally

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/* BEGIN-10.1 - Prepare the NavigationTest class */
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /* BEGIN-10.2 - Write your first test */
    private lateinit var navController: TestNavHostController

    @Before
    fun setupRallyNavHost() {
        // Set the Compose content that you want to test
        composeTestRule.setContent {
            // Creates a TestNavHostController
            navController = TestNavHostController(LocalContext.current)
            // Sets a ComposeNavigator to the navController so it can navigate
            // through composables
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            // Set up your current test subject
            RallyNavHost(navController = navController)
        }
    }

    @Test
    fun rallyNavHost_verifyOverviewStartDestination() {
        composeTestRule
            .onNodeWithContentDescription("Overview Screen")
            .assertIsDisplayed()
    }
    /* END-10.2 */

    /* BEGIN-10.3.1 - Testing via UI clicks and screen contentDescription */
    @Test
    fun rallyNavHost_clickAllAccount_navigatesToAccounts() {
        composeTestRule
            .onNodeWithContentDescription("All Accounts")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Accounts Screen")
            .assertIsDisplayed()
    }
    /* END-10.3.1 */

    /* BEGIN-10.3.2 - Testing via UI clicks and routes comparison */
    @Test
    fun rallyNavHost_clickAllBills_navigateToBills() {
        composeTestRule.onNodeWithContentDescription("All Bills")
            // Make sure you first scroll to the Bills subsection on your
            // Overview screen, otherwise the test will fail as it wouldn't be
            // able to find a node with contentDescription "All Bills".
            .performScrollTo()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "bills")
    }
    /* END-10.3.2 */
}
/* END-10.1 */
