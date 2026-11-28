package ua.lumos.mobile

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun LumosBottomNavigation(
    favoritesSelected: Boolean,
    onCatalogClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = !favoritesSelected,
            onClick = onCatalogClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_catalog),
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.bottom_catalog)) }
        )
        NavigationBarItem(
            selected = favoritesSelected,
            onClick = onFavoritesClick,
            icon = {
                Icon(
                    painter = painterResource(
                        if (favoritesSelected) {
                            R.drawable.ic_favorite
                        } else {
                            R.drawable.ic_favorite_border
                        }
                    ),
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.bottom_favorites)) }
        )
    }
}
