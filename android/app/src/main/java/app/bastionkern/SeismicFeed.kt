package app.bastionkern

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** USGS past-day M4.5+ feed: reported events, not earthquake predictions or early warning. */
data class Earthquake(
    val id: String,
    val place: String,
    val magnitude: Double,
    val longitude: Double,
    val latitude: Double,
    val depthKm: Double,
    val reportedAtMs: Long,
    val recordUrl: String,
    val reviewStatus: String
)

object SeismicFeed {
    const val FEED_URL =
        "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_day.geojson"

    fun fetch(): List<Earthquake> {
        val connection = URL(FEED_URL).openConnection() as HttpURLConnection
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        connection.setRequestProperty("Accept", "application/geo+json, application/json")
        connection.setRequestProperty("User-Agent", "BASTION-KERN-Android/0.1")
        return try {
            require(connection.responseCode == HttpURLConnection.HTTP_OK) {
                "USGS returned HTTP ${connection.responseCode}"
            }
            val body = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            require(body.length <= 8_000_000) { "USGS response exceeded size limit" }
            parse(body)
        } finally {
            connection.disconnect()
        }
    }

    /** Pure parser: can be tested without a network or Mapbox token. */
    fun parse(body: String, limit: Int = 100): List<Earthquake> {
        val records = JSONObject(body).getJSONArray("features")
        val output = ArrayList<Earthquake>(minOf(records.length(), limit))
        for (i in 0 until records.length()) {
            val item = records.optJSONObject(i) ?: continue
            val props = item.optJSONObject("properties") ?: continue
            val geom = item.optJSONObject("geometry") ?: continue
            val points = geom.optJSONArray("coordinates") ?: continue
            if (points.length() < 3) continue
            val lng = points.optDouble(0, Double.NaN)
            val lat = points.optDouble(1, Double.NaN)
            val depth = points.optDouble(2, Double.NaN)
            val mag = props.optDouble("mag", Double.NaN)
            val timestamp = props.optLong("time", 0L)
            val id = item.optString("id")
            if (id.isBlank() || !lng.isFinite() || !lat.isFinite() ||
                !depth.isFinite() || !mag.isFinite() || timestamp <= 0L ||
                lng !in -180.0..180.0 || lat !in -90.0..90.0 ||
                mag < 4.5) continue

            val url = props.optString("url")
            output += Earthquake(
                id = id,
                place = props.optString("place").ifBlank { "Location not provided" },
                magnitude = mag,
                longitude = lng,
                latitude = lat,
                depthKm = depth,
                reportedAtMs = timestamp,
                recordUrl = if (url.startsWith("https://earthquake.usgs.gov/")) url else "",
                reviewStatus = props.optString("status").ifBlank { "unknown" }
            )
        }
        return output.distinctBy { it.id }
            .sortedByDescending { it.reportedAtMs }
            .take(limit.coerceIn(1, 500))
    }
}
