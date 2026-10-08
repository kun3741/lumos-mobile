package ua.lumos.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF111827)
private val Night = Color(0xFF101827)
private val Amber = Color(0xFFFFD166)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LumosBrief() }
    }
}

@Composable
private fun LumosBrief() {
    MaterialTheme {
        Surface(color = Color(0xFFF7F8FA), modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Hero() }
                item {
                    BriefCard(title = stringResource(R.string.screens_title)) {
                        val screens = listOf(
                            stringResource(R.string.screen_catalog),
                            stringResource(R.string.screen_details),
                            stringResource(R.string.screen_settings)
                        )
                        screens.forEachIndexed { index, screen ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("0${index + 1}", color = Color(0xFFB7791F), fontWeight = FontWeight.Bold)
                                Text(screen, modifier = Modifier.padding(start = 16.dp), color = Ink)
                            }
                            if (index < screens.lastIndex) Spacer(Modifier.height(12.dp))
                        }
                    }
                }
                item {
                    BriefCard(title = stringResource(R.string.data_title)) {
                        Text(stringResource(R.string.data_description), color = Ink, lineHeight = 22.sp)
                    }
                }
                item {
                    BriefCard(title = stringResource(R.string.saved_title)) {
                        Text(stringResource(R.string.saved_description), color = Ink, lineHeight = 22.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun Hero() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Night, RoundedCornerShape(28.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✦", fontSize = 36.sp, color = Amber)
            Text("  ЛЮМОС", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
        Text(
            stringResource(R.string.prototype_badge),
            color = Amber,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            stringResource(R.string.tagline),
            color = Color.White,
            fontSize = 21.sp,
            lineHeight = 28.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.size(8.dp).background(Amber, CircleShape))
            Text("  Івано-Франківщина · Львівщина · Волинь", color = Color(0xFFD4DAE5), fontSize = 12.sp)
        }
    }
}

@Composable
private fun BriefCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(title, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LumosBriefPreview() {
    LumosBrief()
}
