PRAGMA foreign_keys=ON;
CREATE TABLE IF NOT EXISTS events(event_id TEXT PRIMARY KEY,provider_id TEXT NOT NULL,category TEXT NOT NULL,severity TEXT NOT NULL DEFAULT 'unknown',verification TEXT NOT NULL,lifecycle TEXT NOT NULL,availability TEXT NOT NULL,observed_at TEXT,ingested_at TEXT NOT NULL,longitude REAL,latitude REAL,payload_json TEXT NOT NULL);
CREATE INDEX IF NOT EXISTS idx_events_category_observed ON events(category,observed_at DESC);
CREATE INDEX IF NOT EXISTS idx_events_provider_observed ON events(provider_id,observed_at DESC);
CREATE INDEX IF NOT EXISTS idx_events_coordinates ON events(latitude,longitude);
CREATE TABLE IF NOT EXISTS provider_runs(run_id INTEGER PRIMARY KEY AUTOINCREMENT,provider_id TEXT NOT NULL,status TEXT NOT NULL,fetched_at TEXT NOT NULL,records_count INTEGER NOT NULL DEFAULT 0,message TEXT);
CREATE INDEX IF NOT EXISTS idx_provider_runs_provider_time ON provider_runs(provider_id,fetched_at DESC);
