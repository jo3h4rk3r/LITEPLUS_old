package liteplus.mixin;

import liteplus.litepluscore;
import liteplus.utils.FabricReflect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static liteplus.litepluscore.VERSION;

@Mixin({InGameHud.class})
public class ScreenHudMixin extends DrawableHelper {
    @Shadow private float field_27959;
    private final TextRenderer fontRenderer;
    private long prevTime = 0;
    private double tps = 20;
    private long lastPacket = 0;
    int arrayCount = 0;
    //private static int tps = 1;
    private static int mspt = 1;
    private int counter = 0;
    protected MinecraftClient mc = MinecraftClient.getInstance();
    public ScreenHudMixin(float field_27959, TextRenderer fontRenderer) {
        this.field_27959 = field_27959;
        this.fontRenderer = fontRenderer;
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"))
    public void renderer(MatrixStack matrices, CallbackInfo ci) {
        if (litepluscore.ShowHUD()) {



            String ServerVersion = mc.getCurrentServerEntry().version.asString();
            String ServerIP = mc.getCurrentServerEntry() == null ? "Singleplayer" : mc.getCurrentServerEntry().address;
            boolean nether = mc.world.getRegistryKey().getValue().getPath().contains("nether");
            BlockPos pos = mc.player.getBlockPos();
            Vec3d vec = mc.player.getPos();
            BlockPos pos2 = nether ? new BlockPos(vec.getX() * 8, vec.getY(), vec.getZ() * 8)
                    : new BlockPos(vec.getX() / 8, vec.getY(), vec.getZ() / 8);
            PlayerListEntry playerEntry = mc.player.networkHandler.getPlayerListEntry(mc.player.getGameProfile().getId());
            int ping = playerEntry == null ? 0 : playerEntry.getLatency();
            String PlayersOnline = mc.getCurrentServerEntry().playerCountLabel.asString();
            int MaxPlayersOnline = 0;

            counter++;
            if (counter == 500) {
                long time = System.currentTimeMillis();
                lastPacket = System.currentTimeMillis();
                if (time - lastPacket > 500) {
                    String text = "Connection Lost: " + ((time - lastPacket) / 1000d) + "s";
                    mc.textRenderer.drawWithShadow(matrices, text, mc.getWindow().getScaledWidth() / 2 - mc.textRenderer.getWidth(text) / 2,
                            Math.min((time - lastPacket - 500) / 20 - 20, 10), 0xd0d0d0);
                }
                String suffix = "\u00a77";
                if (lastPacket + 7500 < System.currentTimeMillis())
                    suffix += "....";
                else if (lastPacket + 5000 < System.currentTimeMillis())
                    suffix += "...";
                else if (lastPacket + 2500 < System.currentTimeMillis())
                    suffix += "..";
                else if (lastPacket + 1200 < System.currentTimeMillis())
                    suffix += ".";
                if (time < 500)
                    return;
                long timeOffset = Math.abs(1000 - (time - prevTime)) + 1000;
                tps = Math.round(MathHelper.clamp(20 / ((double) timeOffset / 1000), 0, 20) * 100d) / 100d;
                prevTime = time;
                counter = 0;
            }



            int FPSCount = (int) FabricReflect.getFieldValue(MinecraftClient.getInstance(), "field_1738", "currentFps");

            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§e§lLITE§9§lPLUS §f§l" + VERSION + " §f" + ServerVersion, 10, 10, 0, false);

            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§f§lFPS:§a " + FPSCount, 10, 30, 0, false);


            if (litepluscore.ShowHUDStats()) {

                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§9Online:§b " + PlayersOnline, 10, 50, 0, false);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§9PING:§b " + ping + "ms", 10, 60, 0, false);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§9TPS:§b " + tps + "<- broke", 10, 70, 0, false);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§9SERVER IP:§b " + ServerIP, 10, 80, 0, false);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "§9POS: " + (nether ? "\u00a74" : "\u00a7b") + pos.getX() + " " + pos.getY() + " " + pos.getZ()
                        + " \u00a77[" + (nether ? "\u00a7b" : "\u00a74") + pos2.getX() + " " + pos2.getY() + " " + pos2.getZ() + "\u00a77]", 10, 90, 0, false);

            }


        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {










        //System.out.println();


    }




}