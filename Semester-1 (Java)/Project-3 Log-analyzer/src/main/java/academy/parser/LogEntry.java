package academy.parser;

import java.time.Instant;

/** DTO для строки лога NGINX. Value Object: неизменяемый record. */
public record LogEntry(
        String remoteAddr,
        String remoteUser,
        Instant timestamp,
        String method,
        String resource,
        String protocol,
        int status,
        long bodyBytesSent,
        String referer,
        String userAgent) {}
