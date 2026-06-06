# Hostname Catcher

[![Minecraft Version](https://img.shields.io/badge/Minecraft-26.1-blue.svg)](https://minecraft.net/)
[![Loader](https://img.shields.io/badge/Loader-Fabric-lightgrey.svg)](https://fabricmc.net/)

This is a lightweight server-side Minecraft Fabric Mod that gets the hostname used by the player when joining the server.
It can log what hostname uses a player to join the server and work as a whitelist to force specific hostname.

---

## Features
- **Domain Logging:** Logs the domain or IP address each player uses to connect to the server (e.g. `mc.example.com` or the IP address of the running server).
- **Domain Whitelisting:** Restricts connections to only approved hostnames/IPs, automatically disconnecting unapproved attempts.
- **Wildcard Support:** Easily whitelist entire subdomains (e.g., `*.example.com`).
---

## Configuration
Upon first launch, the mod generates a configuration file at `config/hostname-catcher.yml` in your server directory. 

### Config file
```yaml
# Listed domains are allowed to connect, other requests will be blocked.
# If the whitelist is empty, then any connection will be allowed.
# Wildcard supported.
allowed-domains:
  - localhost
  - 127.0.0.1
  - *.example.com
```

### Behavior

1. **Empty List:** If the list is empty, all connection attempts are allowed, but the connection hostname will still be logged in the console.
2. **Exact Matches:** Adding `play.myserver.net` will allow connections only through that exact address.
3. **Wildcards:** Prefixing a domain with `*.` (e.g., `*.myserver.net`) will match both the root domain (`myserver.net`) and any subdomains (e.g., `lobby.myserver.net`, `proxy.myserver.net`).

---

## Build & Installation
### Requirements
- **Java 25**.
- **Gradle 9.0** or higher.
- **Fabric Loader** 0.19.3 or higher.
- **Minecraft** 26.1.x.

### Building from Source
1. Install Java 25.
2. Clone this repository.
3. Run this command:
```bash
# Windows
./gradlew.bat build

# Linux/macOS
./gradlew build

#The compiled mod JAR will be located in the `build/libs/` directory.
```
4. Copy the generated `hostname-catcher-<version>.jar` into your Minecraft server's `mods` folder.
5. Install **Fabric API**.
6. Start the server to generate the default config file.
7. Edit the config file and restart the server to apply changes.
