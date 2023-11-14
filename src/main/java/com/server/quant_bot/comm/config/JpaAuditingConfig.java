package com.server.quant_bot.comm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing annotation을 bean으로 주입하기 위한 클래스
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
