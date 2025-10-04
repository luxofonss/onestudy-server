CREATE TABLE resources (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path TEXT, -- Full path including folders, e.g., 'documents/reports/q1_report.pdf'
    extension VARCHAR(10), -- 'mp3' or 'jpg',...
    size_bytes BIGINT, -- NULL for folders, size in bytes for files
    cloud_id VARCHAR(255) UNIQUE, -- ID assigned by the cloud storage provider (e.g., S3 ETag, Google Drive File ID)
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE, -- NULL if not deleted
    metadata JSONB -- Store additional key-value metadata
);