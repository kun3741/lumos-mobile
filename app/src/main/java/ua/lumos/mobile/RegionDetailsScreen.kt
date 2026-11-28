package ua.lumos.mobile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability
import ua.lumos.mobile.ui.theme.LumosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionDetailsScreen(
    region: Region?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.details_title),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.back_description),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    },
                    actions = {
                        if (region != null) {
                            IconButton(onClick = onFavoriteClick) {
                                Icon(
                                    painter = painterResource(
                                        if (isFavorite) {
                                            R.drawable.ic_favorite
                                        } else {
                                            R.drawable.ic_favorite_border
                                        }
                                    ),
                                    contentDescription = stringResource(
                                        if (isFavorite) {
                                            R.string.remove_from_favorites
                                        } else {
                                            R.string.add_to_favorites
                                        }
                                    ),
                                    tint = if (isFavorite) {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        ) { innerPadding ->
            if (region == null) {
                RegionNotFound(
                    onBackClick = onBackClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            } else {
                RegionDetailsContent(
                    region = region,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
    }
}

@Composable
private fun RegionDetailsContent(region: Region, modifier: Modifier = Modifier) {
    val isConnected = region.availability == RegionAvailability.CONNECTED

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "✦",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 24.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = stringResource(
                            if (isConnected) R.string.available_badge else R.string.planned_badge
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 18.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isConnected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        color = if (isConnected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = region.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(R.string.details_loaded_description),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        DetailField(
            title = stringResource(R.string.details_api_id),
            value = region.id
        )
        DetailField(
            title = stringResource(R.string.details_api_status),
            value = stringResource(
                if (isConnected) R.string.status_connected else R.string.status_planned
            )
        )
        DetailField(
            title = stringResource(R.string.details_image_url),
            value = region.image_url.ifBlank { stringResource(R.string.not_specified) }
        )
        DetailField(
            title = stringResource(R.string.details_schedule_days),
            value = region.schedule_days?.toString() ?: stringResource(R.string.not_specified)
        )
    }
}

@Composable
private fun DetailField(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun RegionNotFound(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("?", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.displaySmall)
        }
        Text(
            text = stringResource(R.string.not_found_title),
            modifier = Modifier.padding(top = 18.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(R.string.not_found_description),
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 22.dp)
        ) {
            Text(stringResource(R.string.back_to_catalog))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegionDetailsDarkPreview() {
    LumosTheme(darkTheme = true) {
        RegionDetailsScreen(
            region = Region("lviv", "Львівщина", "", RegionAvailability.CONNECTED, true, 7),
            isFavorite = true,
            onFavoriteClick = {},
            onBackClick = {}
        )
    }
}
