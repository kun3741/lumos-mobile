package ua.lumos.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.lumos.mobile.data.Region

private val FavoritesInk = Color(0xFF172033)
private val FavoritesNight = Color(0xFF101827)
private val FavoritesAmber = Color(0xFFFFC857)
private val FavoritesBackground = Color(0xFFF3F5F8)
private val FavoritesMutedText = Color(0xFF697386)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoriteRegionsUiState,
    onCatalogClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRetry: () -> Unit
) {
    MaterialTheme {
        Scaffold(
            containerColor = FavoritesBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.favorites_title),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = FavoritesNight)
                )
            },
            bottomBar = {
                LumosBottomNavigation(
                    favoritesSelected = true,
                    onCatalogClick = onCatalogClick,
                    onFavoritesClick = onFavoritesClick
                )
            }
        ) { innerPadding ->
            when {
                uiState.isLoading -> FavoritesLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                uiState.error != null -> FavoritesError(
                    error = uiState.error,
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                uiState.favoriteRegions.isEmpty() -> EmptyFavorites(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(key = "favorites_heading") {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = stringResource(R.string.favorites_heading),
                                color = FavoritesInk,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = stringResource(R.string.favorites_subtitle),
                                color = FavoritesMutedText,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                    items(uiState.favoriteRegions, key = Region::id) { region ->
                        RegionListItem(
                            region = region,
                            isFavorite = true,
                            onFavoriteClick = { onFavoriteClick(region.id) },
                            onClick = { onItemClick(region.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = FavoritesAmber)
        Text(
            text = stringResource(R.string.loading_favorites),
            modifier = Modifier.padding(top = 12.dp),
            color = FavoritesMutedText
        )
    }
}

@Composable
private fun EmptyFavorites(modifier: Modifier = Modifier) {
    FavoritesMessage(
        symbol = "♡",
        title = stringResource(R.string.favorites_empty_title),
        description = stringResource(R.string.favorites_empty_description),
        modifier = modifier
    )
}

@Composable
private fun FavoritesError(
    error: RegionLoadError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val description = when (error) {
        RegionLoadError.NO_NETWORK -> stringResource(R.string.error_network_description)
        RegionLoadError.SERVER -> stringResource(R.string.error_server_description)
        RegionLoadError.INVALID_RESPONSE -> stringResource(R.string.error_format_description)
    }
    FavoritesMessage(
        symbol = "!",
        title = stringResource(R.string.favorites_error_title),
        description = description,
        actionLabel = stringResource(R.string.retry),
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
private fun FavoritesMessage(
    symbol: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(symbol, color = FavoritesMutedText, fontSize = 34.sp)
        }
        Text(
            text = title,
            modifier = Modifier.padding(top = 18.dp),
            color = FavoritesInk,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(18.dp),
            color = FavoritesMutedText,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Button(
                onClick = onAction,
                modifier = Modifier.padding(top = 18.dp)
            ) {
                Text(actionLabel)
            }
        }
    }
}
