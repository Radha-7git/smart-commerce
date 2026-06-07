package com.radha.smartcommerce.audit.entity;

import com.radha.smartcommerce.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="audit_logs")
@Getter
@Setter

public class AuditLog extends BaseEntity {
    private Long userId;
    @Enumerated(EnumType.STRING)
    private AuditAction action;
    private String details;
}
