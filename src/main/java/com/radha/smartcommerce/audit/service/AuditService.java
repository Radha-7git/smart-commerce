package com.radha.smartcommerce.audit.service;

import com.radha.smartcommerce.audit.entity.AuditAction;

import javax.swing.*;

public interface AuditService {
    void log(
            Long userId,
            AuditAction action,
            String details
    );
}
