package net.sixfurs.hostnameCatcher.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.server.network.ServerHandshakePacketListenerImpl;
import net.sixfurs.hostnameCatcher.ClientConnectionExt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHandshakePacketListenerImpl.class)
public class ServerHandshakeNetworkHandlerMixin {
    @Shadow
    @Final
    private Connection connection;

    @Inject(method = "handleIntention", at = @At("HEAD"))
    private void captureDomainName(ClientIntentionPacket packet, CallbackInfo ci) {
        String domain = packet.hostName();
        if (domain != null && domain.contains("\0")) {
            domain = domain.split("\0")[0];
        }
        ((ClientConnectionExt) this.connection).setDomainName(domain);
    }
}
