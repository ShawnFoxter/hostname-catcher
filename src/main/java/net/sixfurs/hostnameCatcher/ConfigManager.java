package net.sixfurs.hostnameCatcher;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final Path configPath;
    private final List<String> allowedDomains = new ArrayList<>();

    public ConfigManager(Path configPath) {
        this.configPath = configPath;
    }

    public void load() {
        allowedDomains.clear();
        if (!Files.exists(configPath)) {
            saveDefault();
        }
        try (BufferedReader reader = Files.newBufferedReader(configPath)) {
            String line;
            boolean inSection = false;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                if (trimmed.startsWith("allowed-domains:")) {
                    String value = trimmed.substring("allowed-domains:".length()).trim();
                    if (value.startsWith("[") && value.endsWith("]")) {
                        String content = value.substring(1, value.length() - 1);
                        for (String part : content.split(",")) {
                            String d = part.trim();
                            if (!d.isEmpty()) {
                                allowedDomains.add(normalizeDomain(d));
                            }
                        }
                    } else {
                        inSection = true;
                    }
                    continue;
                }
                if (inSection) {
                    if (trimmed.startsWith("-")) {
                        String d = trimmed.substring(1).trim();
                        if ((d.startsWith("\"") && d.endsWith("\"")) || (d.startsWith("'") && d.endsWith("'"))) {
                            d = d.substring(1, d.length() - 1);
                        }
                        if (!d.isEmpty()) {
                            allowedDomains.add(normalizeDomain(d));
                        }
                    } else if (line.matches("^\\S+:.*")) {
                        inSection = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDefault() {
        try {
            Files.createDirectories(configPath.getParent());
            String defaultContent = "# Listed domains are allowed to connect, other requests will be blocked.\n" +
                    "# If the whitelist is empty, then any connection will be allowed\n" +
                    "# Wildcard supported.\n" +
                    "allowed-domains:\n" +
                    "  - localhost\n" +
                    "  - 127.0.0.1\n" +
                    "  - *.example.com\n";
            Files.writeString(configPath, defaultContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String normalizeDomain(String d) {
        if (d == null) {
            return null;
        }
        String normalized = d.trim();
        if (normalized.endsWith(".")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    public boolean isDomainAllowed(String domain) {
        if (domain == null)
            return false;
        domain = normalizeDomain(domain);
        if (allowedDomains.isEmpty())
            return true;
        for (String allowed : allowedDomains) {
            if (allowed.startsWith("*.")) {
                String suffix = allowed.substring(2);
                if (domain.equalsIgnoreCase(suffix) || domain.toLowerCase().endsWith("." + suffix.toLowerCase())) {
                    return true;
                }
            } else {
                if (domain.equalsIgnoreCase(allowed)) {
                    return true;
                }
            }
        }
        return false;
    }
}
