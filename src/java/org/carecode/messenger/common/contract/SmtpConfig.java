package org.carecode.messenger.common.contract;

public record SmtpConfig(String username, String password, String smtpHost, Integer smtpPort, Boolean smtpAuth,
                         Boolean smtpStarttlsEnable, Boolean smtpSslEnable) {
}
