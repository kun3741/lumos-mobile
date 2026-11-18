package ua.lumos.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.lumos.mobile.data.Region
import ua.lumos.mobile.data.RegionAvailability

private val ItemInk = Color(0xFF172033)
private val ItemNight = Color(0xFF101827)
private val ItemAmber = Color(0xFFFFC857)
private val ItemBackground = Color(0xFFF3F5F8)
private val ItemMutedText = Color(0xFF697386)

@Composable
fun RegionListItem(region: Region, onClick: () -> Unit) {
    val isConnected = region.availability == RegionAvailability.CONNECTED

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .background(if (isConnected) ItemNight else Color(0xFF334155))
            ) {
                Text(
                    text = "✦",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp),
                    color = ItemAmber,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = stringResource(
                        if (isConnected) R.string.available_badge else R.string.planned_badge
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isConnected) ItemAmber else Color(0xFFD8E0EE))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    color = ItemNight,
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
                    color = ItemInk,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
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
                        label = stringResource(R.string.data_label),
                        value = stringResource(R.string.data_api),
                        modifier = Modifier.weight(1f)
                    )
                    RegionCharacteristic(
                        label = stringResource(R.string.status_label),
                        value = stringResource(
                            if (isConnected) R.string.status_connected else R.string.status_planned
                        ),
                        modifier = Modifier.weight(1.35f)
                    )
                }
            }
        }
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
            .background(ItemBackground, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            color = ItemMutedText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = ItemInk,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
