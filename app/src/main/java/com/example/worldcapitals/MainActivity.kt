package com.example.worldcapitals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var selected by remember { mutableStateOf<Country?>(null) }
    val current = selected
    if (current == null) {
        CapitalList(onSelect = { selected = it })
    } else {
        FlagScreen(current) { selected = null }
    }
}

@Composable
fun CapitalList(onSelect: (Country) -> Unit) {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            now = LocalDateTime.now()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "🕒 Столицы мира",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Нажмите на столицу, чтобы увидеть флаг",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyColumn {
            items(countries) { country ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(country) }
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(country.flag, fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(country.capital, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(country.name, fontSize = 12.sp, color = Color.Gray)
                    }
                    val zoned = now.atZone(ZoneId.systemDefault())
                        .withZoneSameInstant(ZoneId.of(country.timezone))
                    Text(
                        text = zoned.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun FlagScreen(country: Country, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onBack() },
        contentAlignment = Alignment.Center
    ) {
        Text(country.flag, fontSize = 160.sp)
        Text(
            text = "← Назад",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(country.capital, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(country.name, fontSize = 18.sp, color = Color.Gray)
        }
    }
}