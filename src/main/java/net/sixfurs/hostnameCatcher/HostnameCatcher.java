package net.sixfurs.hostnameCatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.sixfurs.hostnameCatcher.mixin.ServerCommonPacketListenerImplAccessor;
import net.sixfurs.hostnameCatcher.mixin.ServerLoginPacketListenerImplAccessor;

public class HostnameCatcher implements ModInitializer {
    private static ConfigManager configManager;

    @Override
    public void onInitialize() {
        configManager = new ConfigManager(
            FabricLoader.getInstance().getConfigDir().resolve("hostname-catcher.yml")
        );
        configManager.load();
        ServerLoginConnectionEvents.INIT.register((handler, server) -> {
            Connection connection = ((ServerLoginPacketListenerImplAccessor) handler).getConnection();
            String domain = ((ClientConnectionExt) connection).getDomainName();
            if (!configManager.isDomainAllowed(domain)) {
                System.out.println("Disconnecting login attempt from domain: " + domain + " (Not Whitelisted)");
                handler.disconnect(Component.literal("Connection Throttled."));
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Connection connection = ((ServerCommonPacketListenerImplAccessor) handler).getConnection();
            String domain = ((ClientConnectionExt) connection).getDomainName();
            System.out.println("Player " + handler.player.getName().getString() + 
                               " connected via domain: " + domain);
        });
    }
}
