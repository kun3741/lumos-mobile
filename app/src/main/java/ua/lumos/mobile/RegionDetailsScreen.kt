package ua.lumos.mobile

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

private val DetailsInk = Color(0xFF172033)
private val DetailsNight = Color(0xFF101827)
private val DetailsAmber = Color(0xFFFFC857)
private val DetailsBackground = Color(0xFFF3F5F8)
private val DetailsMutedText = Color(0xFF697386)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionDetailsScreen(
    region: Region?,
    onBackClick: () -> Unit
) {
    MaterialTheme {
        Scaffold(
            containerColor = DetailsBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.details_title),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.back_description),
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = DetailsNight)
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
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                        .background(DetailsNight)
                ) {
                    Text(
                        text = "✦",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 24.dp),
                        color = DetailsAmber,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black
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
                                if (isConnected) DetailsAmber else Color(0xFFD8E0EE)
                            )
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        color = DetailsNight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = region.name,
                        color = DetailsInk,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = stringResource(R.string.details_loaded_description),
                        color = DetailsMutedText,
                        fontSize = 15.sp,
                        lineHeight = 23.sp
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
    }
}

@Composable
private fun DetailField(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = title,
                color = DetailsMutedText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                color = DetailsInk,
                fontSize = 16.sp,
                lineHeight = 23.sp
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
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("?", color = DetailsMutedText, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = stringResource(R.string.not_found_title),
            modifier = Modifier.padding(top = 18.dp),
            color = DetailsInk,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.not_found_description),
            modifier = Modifier.padding(top = 8.dp),
            color = DetailsMutedText,
            fontSize = 15.sp,
            lineHeight = 22.sp,
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
