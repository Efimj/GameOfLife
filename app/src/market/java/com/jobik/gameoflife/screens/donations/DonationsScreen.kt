package com.jobik.gameoflife.screens.donations

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jobik.gameoflife.R
import com.jobik.gameoflife.billing.BillingMessage
import com.jobik.gameoflife.billing.DonationProduct
import com.jobik.gameoflife.billing.DonationsBillingState
import com.jobik.gameoflife.billing.SubscriptionPlan
import com.jobik.gameoflife.screens.layout.ModalDrawer
import com.jobik.gameoflife.screens.layout.ModalDrawerImplementation
import com.jobik.gameoflife.ui.composables.Counter
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import com.jobik.gameoflife.util.SnackbarHostUtil
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun DonationsScreen(
    viewModel: DonationsViewModel = viewModel(
        factory = DonationsViewModel.Factory(LocalContext.current)
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context.findActivity()
    val compact = currentWidthSizeClass() == WindowWidthSizeClass.Compact

    val snackbarMessage = state.message?.takeUnless {
        it == BillingMessage.PURCHASE_COMPLETE
    }
    if (snackbarMessage != null) {
        val text = stringResource(snackbarMessage.stringResource())
        LaunchedEffect(snackbarMessage, text) {
            SnackbarHostUtil.snackbarHostState.showSnackbar(text)
            viewModel.clearMessage()
        }
    }

    val showThankYou = state.message == BillingMessage.PURCHASE_COMPLETE
    val loadError = state.loadError

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (compact) DonationsAppBar()

            when {
                state.isLoading -> DonationsLoading()
                loadError != null -> DonationsError(
                    error = loadError,
                    onRetry = viewModel::refresh,
                )

                else -> DonationsContent(
                    state = state,
                    isCompact = compact,
                    onSubscriptionPurchase = { plan ->
                        activity?.let {
                            viewModel.purchaseSubscription(it, plan.basePlanId)
                        }
                    },
                    onDonationPurchase = {
                        activity?.let(viewModel::purchaseDonation)
                    },
                )
            }
        }

        if (showThankYou) {
            ThankYouModal(onDismiss = viewModel::clearMessage)
        }
    }
}

@Composable
private fun DonationsContent(
    state: DonationsBillingState,
    isCompact: Boolean,
    onSubscriptionPurchase: (SubscriptionPlan) -> Unit,
    onDonationPurchase: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 48.dp,
            end = 16.dp,
            bottom = 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            DonationsHeader(
                showTitle = !isCompact,
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                state.subscriptionPlans.forEach { plan ->
                    SubscriptionCard(
                        modifier = Modifier.weight(1f),
                        plan = plan,
                        isSubscribed = state.isSubscribed,
                        activeBasePlanId = state.activeSubscriptionBasePlanId,
                        onPurchase = { onSubscriptionPurchase(plan) },
                    )
                }
            }
        }

        item {
            DonationCard(
                product = state.donation,
                onPurchase = onDonationPurchase,
            )
        }

        item { BottomWindowInsetsSpacer() }
    }
}

@Composable
private fun DonationsHeader(showTitle: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showTitle) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.donations_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(Modifier.height(if (showTitle) 48.dp else 24.dp))
        Surface(
            modifier = Modifier.size(128.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            shadowElevation = 2.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.size(108.dp),
                    painter = painterResource(R.drawable.ic_app),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.donations_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DonationsLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DonationsError(
    error: BillingMessage,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(error.stringResource()),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.donations_retry))
        }
    }
}

@Composable
private fun SubscriptionCard(
    modifier: Modifier,
    plan: SubscriptionPlan,
    isSubscribed: Boolean,
    activeBasePlanId: String?,
    onPurchase: () -> Unit,
) {
    val isPurchased = isSubscribed && activeBasePlanId == plan.basePlanId
    PurchaseCard(
        modifier = modifier,
        label = stringResource(R.string.donations_subscription_label),
        title = plan.localizedName ?: stringResource(R.string.donations_unavailable),
        localizedPrice = plan.localizedPrice,
        isAvailable = plan.isAvailable && !isSubscribed,
        isPurchased = isPurchased,
        topEndContent = if (isPurchased) {
            { CrownIcon() }
        } else {
            null
        },
        onPurchase = onPurchase,
    )
}

@Composable
private fun DonationCard(
    product: DonationProduct,
    onPurchase: () -> Unit,
) {
    PurchaseCard(
        modifier = Modifier.fillMaxWidth(),
        label = stringResource(R.string.donations_repeatable_label),
        title = stringResource(R.string.donations_one_time_name),
        localizedPrice = product.localizedPrice,
        isAvailable = product.isAvailable,
        isPurchased = false,
        topEndContent = {
            DonationCounter(count = product.purchaseCount)
        },
        onPurchase = onPurchase,
    )
}

@Composable
private fun PurchaseCard(
    modifier: Modifier,
    label: String,
    title: String,
    localizedPrice: String?,
    isAvailable: Boolean,
    isPurchased: Boolean,
    topEndContent: (@Composable () -> Unit)? = null,
    onPurchase: () -> Unit,
) {
    Surface(
        modifier = modifier.heightIn(min = 164.dp),
        onClick = onPurchase,
        enabled = isAvailable && !isPurchased,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Box {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 80.dp,
                )
            ) {
                Text(
                    modifier = Modifier.padding(end = 48.dp),
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isAvailable && !isPurchased,
                    onClick = onPurchase,
                ) {
                    Text(
                        text = localizedPrice
                            ?: stringResource(R.string.donations_unavailable),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (topEndContent != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    topEndContent()
                }
            }
        }
    }
}

@Composable
private fun CrownIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Filled.Verified,
        contentDescription = stringResource(R.string.donations_active_subscription),
        tint = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun DonationCounter(count: Int) {
    val description = stringResource(R.string.donations_purchase_count, count)
    Surface(
        modifier = Modifier.semantics { contentDescription = description },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.donations_purchase_count_label),
                style = MaterialTheme.typography.labelMedium,
            )
            Counter(
                count = count,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Composable
private fun ThankYouModal(onDismiss: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val parties = remember { donationConfettiParties() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 480.dp),
                onClick = {},
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(R.string.donations_thank_you_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.donations_thank_you_letter),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }

            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = parties,
            )
        }
    }
}

private fun donationConfettiParties(): List<Party> = Party(
    speed = 10f,
    maxSpeed = 30f,
    damping = 0.9f,
    emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(100),
).let { party ->
    listOf(
        party.copy(
            angle = 45,
            position = Position.Relative(0.0, 0.0),
            spread = 90,
        ),
        party.copy(
            angle = 90,
            position = Position.Relative(0.5, 0.0),
            spread = 360,
        ),
        party.copy(
            angle = 135,
            position = Position.Relative(1.0, 0.0),
            spread = 90,
        ),
    )
}

private fun BillingMessage.stringResource(): Int = when (this) {
    BillingMessage.PURCHASE_COMPLETE -> R.string.donations_purchase_complete
    BillingMessage.PURCHASE_PENDING -> R.string.donations_purchase_pending
    BillingMessage.SERVICE_UNAVAILABLE -> R.string.donations_service_unavailable
    BillingMessage.PRODUCT_UNAVAILABLE -> R.string.donations_products_unavailable
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DonationsAppBar(modalDrawer: ModalDrawer = ModalDrawerImplementation) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .horizontalWindowInsetsPadding()
            .topWindowInsetsPadding(),
        title = {
            Text(
                text = stringResource(R.string.donations_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { modalDrawer.drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu_button),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        windowInsets = WindowInsets.ime,
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
