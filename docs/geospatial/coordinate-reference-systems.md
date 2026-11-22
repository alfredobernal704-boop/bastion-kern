# Geospatial coordinate reference system contract

**Status:** Proposed ingestion contract  
**Scope:** Geographic coordinates entering BASTION-KERN from external event feeds and leaving the ingestion layer as GeoJSON.

This document defines the coordinate interpretation boundary. It does not claim that every existing provider adapter or client currently enforces these rules.

## 1. Canonical interchange format

For GeoJSON interchange, BASTION-KERN uses the coordinate position order defined by [RFC 7946](https://www.rfc-editor.org/rfc/rfc7946):

```text
[longitude, latitude]
[longitude, latitude, ellipsoidal-height]
```

Coordinates are expressed in decimal degrees. The optional third ordinate is height in metres above or below the WGS 84 reference ellipsoid, where the source actually supplies that quantity. Do not infer height from a two-dimensional position.

Example:

```json
{
  "type": "Point",
  "coordinates": [-0.1278, 51.5074]
}
```

This position represents longitude -0.1278° and latitude 51.5074°.

RFC 7946 fixes GeoJSON to WGS 84 longitude/latitude coordinates equivalent to OGC CRS84. A GeoJSON producer must not add a legacy `crs` member to declare another coordinate reference system.

## 2. EPSG:4326 axis order is not the GeoJSON tuple order

EPSG:4326 identifies the WGS 84 geographic 2D CRS. Its authority-defined axis order is:

1. Geodetic latitude (north), in degrees.
2. Geodetic longitude (east), in degrees.

That authority axis order differs from the longitude-first position order required by RFC 7946 GeoJSON.

| Context | Coordinate order | Example for the point above |
|---|---|---|
| EPSG:4326 authority axis order | latitude, longitude | `[51.5074, -0.1278]` |
| RFC 7946 GeoJSON position | longitude, latitude | `[-0.1278, 51.5074]` |
| Common GIS x/y API convention | x = longitude, y = latitude | `(-0.1278, 51.5074)` |

Do not infer tuple order from the label “WGS 84” alone. Interpret coordinates using the source format, declared CRS, API contract and library configuration.

PROJ follows authority-defined axis order for authority CRS definitions in relevant APIs, while many visualisation interfaces use longitude/latitude order. Consult the [PROJ axis-order FAQ](https://proj.org/en/stable/faq.html#why-is-the-axis-ordering-in-proj-not-consistent) and inspect CRS axis metadata when behaviour is uncertain.

## 3. Ingestion boundary

Each provider adapter should make the following information explicit before normalisation:

- **Source identity:** provider name and source record identifier.
- **Source representation:** GeoJSON, coordinate pair, WKT, CSV columns, or another declared format.
- **Source CRS:** authority and code when known (for example, `EPSG:4326`); otherwise record it as unknown rather than guessing.
- **Source axis order:** the order required by that provider's format or API.
- **Normalised representation:** GeoJSON-compatible WGS 84 longitude/latitude.
- **Provenance:** retain the original coordinate values and source CRS metadata where operationally and legally appropriate.

Transformation must be an explicit operation from the declared source CRS to the target representation. Do not relabel projected coordinates as WGS 84, and do not swap axes merely because a value appears out of range.

## 4. Validation rules

For a two-dimensional GeoJSON position `[lon, lat]`, validate:

```text
lon is finite and -180 <= lon <= 180
lat is finite and  -90 <= lat <= 90
```

For a three-dimensional position, validate the first two ordinates as above and require the third ordinate to be finite. Its vertical datum and unit must be known before it is used in calculations.

Reject or quarantine a position when:

- the coordinate array has fewer than two ordinates;
- longitude or latitude is non-numeric, NaN, infinite, or outside its valid range;
- the source CRS or axis order is required but cannot be established;
- a transformation fails or its required grid resources are unavailable.

A range check is necessary but not sufficient to detect swapped axes: many incorrect pairs fall within both valid ranges. Use source-specific contracts and known control points; never silently “repair” an ambiguous pair.

For GeoJSON bounding boxes, use the RFC 7946 order:

```text
[west, south, east, north]
```

## 5. Interoperability examples

### GeoJSON (longitude, latitude)

```json
{
  "type": "Feature",
  "properties": {
    "source": "example-provider"
  },
  "geometry": {
    "type": "Point",
    "coordinates": [-0.1278, 51.5074]
  }
}
```

### EPSG:4326 authority axes (latitude, longitude)

```text
latitude  = 51.5074
longitude = -0.1278
```

The same physical point is represented in both examples. The numeric order changes because the interfaces have different axis-order contracts, not because the location changes.

## 6. Verification checklist

Before accepting a provider adapter or coordinate transformation:

- [ ] Identify the provider's coordinate format and declared CRS.
- [ ] Confirm axis order from authoritative provider documentation or CRS metadata.
- [ ] Convert to the GeoJSON longitude/latitude position order at the interchange boundary.
- [ ] Test a known control point with non-symmetric longitude and latitude values.
- [ ] Test invalid ranges, non-finite values, missing ordinates and transformation failures.
- [ ] Verify that bounding boxes use west, south, east, north order.
- [ ] Preserve source provenance and avoid claiming a transformation that was not performed.

## References

- [RFC 7946 — The GeoJSON Format, §3.1.1 and §4](https://www.rfc-editor.org/rfc/rfc7946)
- [PROJ FAQ — Why is the axis ordering not consistent?](https://proj.org/en/stable/faq.html#why-is-the-axis-ordering-in-proj-not-consistent)
- [PROJ `projinfo` — inspect EPSG:4326 axis definitions](https://proj.org/en/stable/apps/projinfo.html)
- [EPSG:4326 — WGS 84](https://epsg.io/4326)
