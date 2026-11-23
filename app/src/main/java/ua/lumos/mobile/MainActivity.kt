package ua.lumos.mobile

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.lumos.mobile.data.Region

private val Ink = Color(0xFF172033)
private val Night = Color(0xFF101827)
private val Amber = Color(0xFFFFC857)
private val ScreenBackground = Color(0xFFF3F5F8)
private val MutedText = Color(0xFF697386)

private val regionFilters = RegionFilter.entries
private const val CatalogRoute = "catalog"
private const val SettingsRoute = "settings"
private const val RegionIdArgument = "regionId"
private const val DetailsRoute = "details/{$RegionIdArgument}"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LumosApp() }
    }
}

@Composable
private fun LumosApp(catalogViewModel: RegionCatalogViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogRoute
    ) {
        composable(CatalogRoute) {
            RegionCatalogScreen(
                uiState = catalogViewModel.uiState,
                onFilterSelected = catalogViewModel::onFilterSelected,
                onSearchQueryChange = catalogViewModel::onSearchQueryChange,
                onClearSearch = catalogViewModel::clearSearch,
                onRetry = catalogViewModel::retry,
                onRefresh = catalogViewModel::refresh,
                onSettingsClick = {
                    navController.navigate(SettingsRoute)
                },
                onItemClick = { regionId ->
                    navController.navigate("details/$regionId")
                }
            )
        }
        composable(SettingsRoute) {
            val viewModel: SettingsViewModel = viewModel()
            val sortAlphabetically by viewModel.sortAlphabetically.collectAsState()
            val context = LocalContext.current
            var notificationsEnabled by remember {
                mutableStateOf(hasNotificationPermission(context))
            }
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                notificationsEnabled = granted
                if (granted) showNotificationPermissionConfirmed(context)
            }
            SettingsScreen(
                sortAlphabetically = sortAlphabetically,
                notificationsEnabled = notificationsEnabled,
                onSortAlphabeticallyChange = viewModel::onSortAlphabeticallyChange,
                onNotificationAction = {
                    if (hasNotificationPermission(context)) {
                        notificationsEnabled = true
                        showNotificationPermissionConfirmed(context)
                    } else {
                        notificationPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = DetailsRoute,
            arguments = listOf(
                navArgument(RegionIdArgument) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val regionId = backStackEntry.arguments?.getString(RegionIdArgument)
            val region = catalogViewModel.uiState.regions.firstOrNull { it.id == regionId }
            RegionDetailsScreen(
                region = region,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegionCatalogScreen(
    uiState: RegionCatalogUiState,
    onFilterSelected: (RegionFilter) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onSettingsClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    MaterialTheme {
        Scaffold(
            containerColor = ScreenBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(Amber, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✦", color = Night, fontWeight = FontWeight.Black)
                            }
                            Text(
                                text = stringResource(R.string.app_name),
                                modifier = Modifier.padding(start = 10.dp),
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = stringResource(R.string.settings_description),
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Night)
                )
            }
        ) { innerPadding ->
            RegionCatalog(
                uiState = uiState,
                onFilterSelected = onFilterSelected,
                onSearchQueryChange = onSearchQueryChange,
                onClearSearch = onClearSearch,
                onRetry = onRetry,
                onRefresh = onRefresh,
                onItemClick = onItemClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegionCatalog(
    uiState: RegionCatalogUiState,
    onFilterSelected: (RegionFilter) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "catalog_header") {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.catalog_title),
                        color = Ink,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = stringResource(R.string.catalog_subtitle),
                        color = MutedText,
                        fontSize = 15.sp,
                        lineHeight = 21.sp
                    )
                    Text(
                        text = stringResource(
                            if (uiState.sortAlphabetically) {
                                R.string.sort_status_alphabetical
                            } else {
                                R.string.sort_status_availability
                            }
                        ),
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        color = MutedText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item(key = "catalog_search") {
                CatalogSearchField(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onClear = onClearSearch
                )
            }

            if (!uiState.isSearchActive) {
                item(key = "catalog_filters") {
                    RegionFilterRow(
                        filters = regionFilters,
                        selectedFilter = uiState.selectedFilter,
                        onFilterSelected = onFilterSelected
                    )
                }
            }

            when {
                uiState.isLoading -> item(key = "catalog_loading") { LoadingRegionsState() }
                uiState.error != null -> item(key = "catalog_error_${uiState.error.name}") {
                    RegionErrorState(error = uiState.error, onRetry = onRetry)
                }
                uiState.regions.isEmpty() -> item(key = "catalog_empty_api") {
                    EmptyApiState(onRetry = onRetry)
                }
                uiState.hasEmptySearchResult -> item(key = "catalog_empty_search") {
                    EmptySearchState(
                        query = uiState.appliedSearchQuery,
                        onClearSearch = onClearSearch
                    )
                }
                uiState.visibleRegions.isEmpty() -> item(key = "catalog_empty_filter") {
                    EmptyFilterState()
                }
                else -> {
                    items(items = uiState.visibleRegions, key = Region::id) { region ->
                        RegionListItem(
                            region = region,
                            onClick = { onItemClick(region.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val clearSearchDescription = stringResource(R.string.clear_search)

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text(stringResource(R.string.search_label)) },
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = {
            Text(
                text = "⌕",
                color = MutedText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.semantics {
                        contentDescription = clearSearchDescription
                    }
                ) {
                    Text(
                        text = "×",
                        color = MutedText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            null
        }
    )
}

@Composable
private fun LoadingRegionsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(color = Color(0xFFB7791F))
        Text(
            text = stringResource(R.string.loading_regions),
            color = MutedText,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun RegionFilterRow(
    filters: List<RegionFilter>,
    selectedFilter: RegionFilter,
    onFilterSelected: (RegionFilter) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = filters, key = RegionFilter::name) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = { Text(stringResource(filter.titleRes())) }
            )
        }
    }
}

@StringRes
private fun RegionFilter.titleRes(): Int = when (this) {
    RegionFilter.ALL -> R.string.filter_all
    RegionFilter.CONNECTED -> R.string.filter_connected
    RegionFilter.PLANNED -> R.string.filter_planned
    RegionFilter.PAUSED -> R.string.filter_paused
}

@Composable
private fun RegionErrorState(error: RegionLoadError, onRetry: () -> Unit) {
    val title = when (error) {
        RegionLoadError.NO_NETWORK -> R.string.error_network_title
        RegionLoadError.SERVER -> R.string.error_server_title
        RegionLoadError.INVALID_RESPONSE -> R.string.error_format_title
    }
    val description = when (error) {
        RegionLoadError.NO_NETWORK -> R.string.error_network_description
        RegionLoadError.SERVER -> R.string.error_server_description
        RegionLoadError.INVALID_RESPONSE -> R.string.error_format_description
    }

    CatalogMessageCard(
        symbol = "!",
        title = stringResource(title),
        description = stringResource(description),
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry
    )
}

@Composable
private fun EmptyApiState(onRetry: () -> Unit) {
    CatalogMessageCard(
        symbol = "∅",
        title = stringResource(R.string.empty_api_title),
        description = stringResource(R.string.empty_api_description),
        actionLabel = stringResource(R.string.refresh),
        onAction = onRetry
    )
}

@Composable
private fun EmptyFilterState() {
    CatalogMessageCard(
        symbol = "⌕",
        title = stringResource(R.string.empty_filter_title),
        description = stringResource(R.string.empty_filter_description)
    )
}

@Composable
private fun EmptySearchState(query: String, onClearSearch: () -> Unit) {
    CatalogMessageCard(
        symbol = "⌕",
        title = stringResource(R.string.search_empty_title),
        description = stringResource(R.string.search_empty_description, query),
        actionLabel = stringResource(R.string.clear_search),
        onAction = onClearSearch
    )
}

@Composable
private fun CatalogMessageCard(
    symbol: String,
    title: String,
    description: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(ScreenBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(symbol, color = MutedText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = title,
            color = Ink,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            color = MutedText,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        if (actionLabel != null && onAction != null) {
            Button(
                onClick = onAction,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LumosAppPreview() {
    RegionCatalogScreen(
        uiState = RegionCatalogUiState(),
        onFilterSelected = {},
        onSearchQueryChange = {},
        onClearSearch = {},
        onRetry = {},
        onRefresh = {},
        onSettingsClick = {},
        onItemClick = {}
    )
}
