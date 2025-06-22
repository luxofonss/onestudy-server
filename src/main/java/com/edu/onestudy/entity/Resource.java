package com.edu.onestudy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path; //  e.g., 'documents/reports/q1_report.pdf'

    @Column(name = "url")
    private String url;

    @Column(name = "owner_id")
    private UUID ownerId; // ID of the user who uploaded the resource

    @Column(name = "extension")
    private String extension; // Extension of the file, e.g., 'mp3', 'jpg'.

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "cloud_id")
    private String cloudId; // ID assigned by the cloud storage provider (e.g., S3 ETag, Google Drive File ID)

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

}
