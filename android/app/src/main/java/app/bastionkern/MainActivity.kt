package app.bastionkern

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val Navy = Color(0xFF081421)
private val Panel = Color(0xF2111F2E)
private val Gold = Color(0xFFF4C253)
private val TextPrimary = Color(0xFFF7F8FC)
private val TextSecondary = Color(0xFFA8B5C3)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Gold, background = Navy, surface = Panel,
                onPrimary = Navy, onSurface = TextPrimary
            )) {
                BastionScreen()
            }
        }
    }
}

private sealed interface FeedState {
    data object Loading : FeedState
    data class Loaded(val earthquakes: List<Earthquake>) : FeedState
    data class Failed(val reason: String) : FeedState
}

@Composable
private fun BastionScreen() {
    val context = LocalContext.current
    val publicToken = stringResource(R.string.mapbox_access_token)
    val viewport = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(0.0, 25.0))
            zoom(1.5)
        }
    }
    var satellite by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<Earthquake?>(null) }
    var feedState by remember { mutableStateOf<FeedState>(FeedState.Loading) }
    val scope = rememberCoroutineScope()
    var showAccount by remember { mutableStateOf(false) }

    suspend fun refresh() {
        feedState = FeedState.Loading
        feedState = withContext(Dispatchers.IO) {
            runCatching { SeismicFeed.fetch() }.fold(
                onSuccess = { FeedState.Loaded(it) },
                onFailure = { FeedState.Failed(it.message ?: "Network unavailable") }
            )
        }
    }
    LaunchedEffect(Unit) { refresh() }

    val events = (feedState as? FeedState.Loaded)?.earthquakes.orEmpty()
    Box(Modifier.fillMaxSize().background(Navy)) {
        if (publicToken.startsWith("pk.")) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = viewport,
                style = {
                    MapStyle(
                        style = if (satellite) Style.STANDARD_SATELLITE else Style.STANDARD
                    )
                }
            ) {
                val pin = rememberIconImage(R.drawable.ic_event_pin)
                events.forEach { earthquake ->
                    key(earthquake.id) {
                        PointAnnotation(
                            point = Point.fromLngLat(
                                earthquake.longitude, earthquake.latitude
                            )
                        ) {
                            iconImage = pin
                            interactionsState.onClicked {
                                selected = earthquake
                                true
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                Modifier.fillMaxSize().background(Navy),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Mapbox public token not configured.\n" +
                        "Set MAPBOX_PUBLIC_TOKEN in Gradle properties.",
                    color = TextSecondary,
                    modifier = Modifier.padding(30.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Surface(shape = RoundedCornerShape(18.dp), color = Panel) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("BASTION—KERN", color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("Global intelligence · USGS", color = TextSecondary,
                            style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(onClick = { scope.launch { refresh() } }) {
                        Text("Refresh", color = Gold)
                    }
                    TextButton(onClick = { showAccount = true }) {
                        Text("Account", color = Gold)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(14.dp), color = Panel) {
                Row(Modifier.padding(horizontal = 8.dp)) {
                    TextButton(onClick = { satellite = false }) {
                        Text("Standard", color = if (!satellite) Gold else TextSecondary)
                    }
                    TextButton(onClick = { satellite = true }) {
                        Text("Satellite", color = if (satellite) Gold else TextSecondary)
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            shape = RoundedCornerShape(22.dp),
            color = Panel,
            shadowElevation = 4.dp
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("GLOBAL EVENTS", color = Gold,
                        style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.weight(1f))
                    Text("USGS · past 24 h · M4.5+", color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.height(10.dp))
                if (selected != null) {
                    val e = selected!!
                    Text("M ${"%.1f".format(Locale.US, e.magnitude)} · ${e.place}",
                        color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Text("Depth ${"%.1f".format(Locale.US, e.depthKm)} km · " +
                        "Source status: ${e.reviewStatus}", color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall)
                    Text(utc(e.reportedAtMs), color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { selected = null }) {
                            Text("All events")
                        }
                        Spacer(Modifier.weight(1f))
                        if (e.recordUrl.isNotEmpty()) TextButton(onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(e.recordUrl)))
                        }) { Text("Official record") }
                    }
                } else when (val s = feedState) {
                    FeedState.Loading -> {
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                        Text("Retrieving reported earthquakes…", color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall)
                    }
                    is FeedState.Failed -> {
                        Text("Feed unavailable: ${s.reason}", color = TextPrimary)
                        Text("No live information is available. Retry when connected.",
                            color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    }
                    is FeedState.Loaded -> {
                        Text("${events.size} reported events · select a marker or record",
                            color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        if (events.isEmpty()) {
                            Text("No qualifying reports in this feed.",
                                color = TextPrimary)
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 180.dp)) {
                                items(events.take(5), key = { it.id }) { e ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .clickable { selected = e }
                                            .padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "M ${"%.1f".format(Locale.US, e.magnitude)}",
                                            color = Gold, fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(e.place, color = TextPrimary, maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                style = MaterialTheme.typography.bodyMedium)
                                            Text(utc(e.reportedAtMs), color = TextSecondary,
                                                style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
                Text(
                    "Reported earthquakes only · Not early warning or prediction",
                    color = TextSecondary, style = MaterialTheme.typography.labelSmall
                )
            }
        }
        if (showAccount) {
            ModalBottomSheet(
                onDismissRequest = { showAccount = false },
                containerColor = Panel
            ) {
                FirebaseAccountPanel()
            }
        }
    }
}

private fun utc(ms: Long): String =
    SimpleDateFormat("dd MMM yyyy · HH:mm 'UTC'", Locale.UK).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(ms))
