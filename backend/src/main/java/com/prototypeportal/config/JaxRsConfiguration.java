package com.prototypeportal.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS アプリケーション設定
 *
 * @ApplicationPath でベースパスを /api/v1 に設定
 */
@ApplicationPath("/api/v1")
public class JaxRsConfiguration extends Application {
    // 自動的にすべての JAX-RS リソースがスキャンされます
}
