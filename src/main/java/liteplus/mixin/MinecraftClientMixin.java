package liteplus.mixin;


import liteplus.litepluscore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements litepluscore.WindowHolder {
    @Shadow
    @Final
    private Window window;

    @Override
    public Window getWindow() {
        return window;
    }
}