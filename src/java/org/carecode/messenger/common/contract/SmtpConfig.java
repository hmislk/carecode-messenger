package org.carecode.messenger.common.contract;

/**
 * Plain JSON-B-friendly bean (Payara's JAX-RS runtime deserializes with
 * Yasson/JSON-B, not Jackson, even though Jackson is on the compile
 * classpath) - needs a no-arg constructor and public fields.
 */
public class SmtpConfig {
    public String username;
    public String password;
    public String smtpHost;
    public Integer smtpPort;
    public Boolean smtpAuth;
    public Boolean smtpStarttlsEnable;
    public Boolean smtpSslEnable;

    public SmtpConfig() {
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String smtpHost() {
        return smtpHost;
    }

    public Integer smtpPort() {
        return smtpPort;
    }

    public Boolean smtpAuth() {
        return smtpAuth;
    }

    public Boolean smtpStarttlsEnable() {
        return smtpStarttlsEnable;
    }

    public Boolean smtpSslEnable() {
        return smtpSslEnable;
    }

    @Override
    public String toString() {
        return "SmtpConfig{" +
                "username='" + username + '\'' +
                ", password='***'" +
                ", smtpHost='" + smtpHost + '\'' +
                ", smtpPort=" + smtpPort +
                ", smtpAuth=" + smtpAuth +
                ", smtpStarttlsEnable=" + smtpStarttlsEnable +
                ", smtpSslEnable=" + smtpSslEnable +
                '}';
    }
}
