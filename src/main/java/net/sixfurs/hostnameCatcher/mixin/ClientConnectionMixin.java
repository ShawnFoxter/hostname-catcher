package net.sixfurs.hostnameCatcher.mixin;

import net.minecraft.network.Connection;
import net.sixfurs.hostnameCatcher.ClientConnectionExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Connection.class)
public class ClientConnectionMixin implements ClientConnectionExt {
    
    @Unique
    private String domainName;

    @Override
    public String getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
