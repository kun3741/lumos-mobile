package ua.lumos.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

private val Ink = Color(0xFF172033)
private val Night = Color(0xFF101827)
private val Amber = Color(0xFFFFC857)
private val ScreenBackground = Color(0xFFF3F5F8)
private val MutedText = Color(0xFF697386)

private val regionFilters = RegionFilter.entries

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LumosApp() }
    }
}

@Composable
private fun LumosApp(viewModel: RegionCatalogViewModel = viewModel()) {
    LumosScreen(
        uiState = viewModel.uiState,
        onFilterSelected = viewModel::onFilterSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LumosScreen(
    uiState: RegionCatalogUiState,
    onFilterSelected: (RegionFilter) -> Unit
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Night)
                )
            }
        ) { innerPadding ->
            RegionCatalog(
                uiState = uiState,
                onFilterSelected = onFilterSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun RegionCatalog(
    uiState: RegionCatalogUiState,
    onFilterSelected: (RegionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
            }
        }

        item {
            RegionFilterRow(
                filters = regionFilters,
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = onFilterSelected
            )
        }

        if (uiState.visibleRegions.isEmpty()) {
            item { EmptyRegionState() }
        } else {
            items(items = uiState.visibleRegions, key = Region::id) { region ->
                RegionCard(region)
            }
        }
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
private fun EmptyRegionState() {
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
            Text("⌕", color = MutedText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = stringResource(R.string.empty_title),
            color = Ink,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.empty_description),
            color = MutedText,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun RegionCard(region: Region) {
    val isConnected = region.availability == RegionAvailability.CONNECTED

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(regionImageRes(region.id)),
                    contentDescription = stringResource(R.string.region_image_description, region.name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(172.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(
                        if (isConnected) R.string.available_badge else R.string.planned_badge
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isConnected) Amber else Color(0xFFD8E0EE))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    color = Night,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Text(
                    text = region.name,
                    color = Ink,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Characteristic(
                        label = stringResource(R.string.region_code),
                        value = region.id.uppercase(),
                        modifier = Modifier.weight(1f)
                    )
                    Characteristic(
                        label = stringResource(R.string.queues),
                        value = "1.1–3.2",
                        modifier = Modifier.weight(1f)
                    )
                    Characteristic(
                        label = stringResource(R.string.source),
                        value = stringResource(
                            if (isConnected) R.string.power_company else R.string.source_preparing
                        ),
                        modifier = Modifier.weight(1.35f)
                    )
                }
            }
        }
    }
}

@Composable
private fun Characteristic(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(ScreenBackground, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(text = label, color = MutedText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@DrawableRes
private fun regionImageRes(id: String): Int = when (id) {
    "if" -> R.drawable.region_if
    "lviv" -> R.drawable.region_lviv
    "volyn" -> R.drawable.region_volyn
    "ternopil" -> R.drawable.region_ternopil
    "rivne" -> R.drawable.region_rivne
    "kyiv" -> R.drawable.region_kyiv
    else -> R.drawable.region_if
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LumosAppPreview() {
    LumosScreen(
        uiState = RegionCatalogUiState(),
        onFilterSelected = {}
    )
}
