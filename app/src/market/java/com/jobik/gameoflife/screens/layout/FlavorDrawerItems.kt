package com.jobik.gameoflife.screens.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jobik.gameoflife.R
import com.jobik.gameoflife.billing.BillingPurchaseStore
import com.jobik.gameoflife.navigation.Donations

fun flavorDrawerButtons(): List<AppDrawerItemInfo> = listOf(
    AppDrawerItemInfo(
        route = Donations,
        title = R.string.donations_title,
        icon = Icons.Outlined.FavoriteBorder,
        description = R.string.donations_navigation_description,
        isSupportItem = true,
    )
)

@Composable
fun flavorHasAnyPurchase(): Boolean {
    val context = LocalContext.current
    val store = remember(context) {
        BillingPurchaseStore.apply { initialize(context) }
    }
    val summary by store.summary.collectAsStateWithLifecycle()
    return summary.hasAnyPurchase
}
