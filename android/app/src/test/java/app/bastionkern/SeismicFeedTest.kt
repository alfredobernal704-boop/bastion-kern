package app.bastionkern

import org.junit.Assert.assertEquals
import org.junit.Test

class SeismicFeedTest {
    @Test fun parsesOfficialFieldsAndRejectsInvalidGeometry() {
        val data = """
          {"type":"FeatureCollection","features":[
            {"id":"us123","geometry":{"type":"Point","coordinates":[-120.5,38.2,12.3]},
              "properties":{"mag":5.1,"place":"Near test location",
                "time":1793400000000,"url":"https://earthquake.usgs.gov/earthquakes/eventpage/us123",
                "status":"reviewed"}},
            {"id":"invalid","geometry":{"type":"Point","coordinates":[900,38.2,3]},
              "properties":{"mag":6,"time":1793400000000}},
            {"id":"below-filter","geometry":{"type":"Point","coordinates":[-70,10,4]},
              "properties":{"mag":3.2,"time":1793400000000}}
          ]}
        """.trimIndent()
        val result = SeismicFeed.parse(data)
        assertEquals(1, result.size)
        assertEquals("us123", result[0].id)
        assertEquals(5.1, result[0].magnitude, 0.0001)
        assertEquals("reviewed", result[0].reviewStatus)
    }

    @Test fun stripsNonUsgsRecordUrls() {
        val data = """
          {"features":[{"id":"x","geometry":{"coordinates":[0,0,1]},
          "properties":{"mag":4.5,"time":1793400000000,
          "url":"https://example.org/untrusted"}}]}
        """.trimIndent()
        assertEquals("", SeismicFeed.parse(data).single().recordUrl)
    }
}
