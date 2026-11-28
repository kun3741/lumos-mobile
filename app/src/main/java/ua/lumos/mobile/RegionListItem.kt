package ua.lumos.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

@Composable
fun RegionListItem(
    region: Region,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    val isConnected = region.availability == RegionAvailability.CONNECTED

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
            ) {
                RegionCover(region = region)
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), CircleShape)
                ) {
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
                        tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(
                        if (isConnected) R.string.available_badge else R.string.planned_badge
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isConnected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    color = if (isConnected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Text(
                    text = region.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RegionCharacteristic(
                        label = stringResource(R.string.region_code),
                        value = region.id.uppercase(),
                        modifier = Modifier.weight(1f)
                    )
                    RegionCharacteristic(
                        label = stringResource(R.string.schedule_days_label),
                        value = region.schedule_days?.toString() ?: "—",
                        modifier = Modifier.weight(1f)
                    )
                    RegionCharacteristic(
                        label = stringResource(R.string.today_schedule_label),
                        value = when (region.available) {
                            true -> stringResource(R.string.today_schedule_available)
                            false -> stringResource(R.string.today_schedule_unavailable)
                            null -> "—"
                        },
                        modifier = Modifier.weight(1.35f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RegionCover(region: Region) {
    if (region.image_url.isBlank()) {
        RegionCoverFallback()
        return
    }

    var isLoading by remember(region.image_url) { mutableStateOf(true) }
    var hasError by remember(region.image_url) { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(region.image_url)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.region_image_description, region.name),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onLoading = {
                isLoading = true
                hasError = false
            },
            onSuccess = {
                isLoading = false
                hasError = false
            },
            onError = {
                isLoading = false
                hasError = true
            }
        )
        when {
            hasError -> RegionCoverFallback()
            isLoading -> RegionCoverLoading()
        }
    }
}

@Composable
private fun RegionCoverLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = 3.dp
        )
    }
}

@Composable
private fun RegionCoverFallback() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✦",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
private fun RegionCharacteristic(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
