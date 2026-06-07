package com.radha.smartcommerce.audit.service;

import com.radha.smartcommerce.audit.entity.AuditAction;
import com.radha.smartcommerce.audit.entity.AuditLog;
import com.radha.smartcommerce.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService{
    private final AuditLogRepository auditLogRepository;
    @Override
    public void log(Long userId, AuditAction action, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }
}
