package liteplus;

import com.google.common.eventbus.EventBus;

import liteplus.hud.HudInfoRenderer;
import liteplus.utils.KeyBindingHandler;
import liteplus.utils.file.FileMang;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.locks.LockSupport;

public class litepluscore implements ModInitializer {
    public static final String MOD_ID = "litepluscore";
    public static final String VERSION = "v1.0";

    public static EventBus eventBus = new EventBus();
    private static long lastRender;

    private static boolean isForcingLowFPS = false;
    private static boolean ShowHUD = true;
    private static boolean ShowHUDStats = false;
    private static boolean hasRenderedLastFrame = false;

    public static boolean isForcingLowFPS() {
        return isForcingLowFPS;
    }

    public static boolean ShowHUD() {
        return ShowHUD;
    }

    public static boolean ShowHUDStats() { return ShowHUDStats; }


    private static final KeyBinding LimitFPS = new KeyBinding(
            "key." + MOD_ID + ".toggle",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "key.categories.misc"
    );
    private static final KeyBinding ShowHUDPanel = new KeyBinding(
            "Toggle HUD Panel",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "key.categories.misc"
    );
    private static final KeyBinding ShowHUDPanelStats = new KeyBinding(
            "Toggle HUD Panel Stats",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "key.categories.misc"
    );
    @Override
    public void onInitialize() {

        System.out.println("LITEPLUS INIT");
        FileMang.init();
        KeyBindingHelper.registerKeyBinding(LimitFPS);
        KeyBindingHelper.registerKeyBinding(ShowHUDPanel);
        KeyBindingHelper.registerKeyBinding(ShowHUDPanelStats);
        ClientTickEvents.END_CLIENT_TICK.register(new KeyBindingHandler(
                LimitFPS,
                () -> isForcingLowFPS = !isForcingLowFPS
        ));
        ClientTickEvents.END_CLIENT_TICK.register(new KeyBindingHandler(
                ShowHUDPanel,
                () -> ShowHUD = !ShowHUD
        ));
        ClientTickEvents.END_CLIENT_TICK.register(new KeyBindingHandler(
                ShowHUDPanelStats,
                () -> ShowHUDStats = !ShowHUDStats
        ));

        HudRenderCallback.EVENT.register(new HudInfoRenderer());



    }
    public static boolean checkForRender() {
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = ((WindowHolder) client).getWindow();

        long currentTime = Util.getMeasuringTimeMs();
        long timeSinceLastRender = currentTime - lastRender;

        boolean isVisible = GLFW.glfwGetWindowAttrib(window.getHandle(), GLFW.GLFW_VISIBLE) != 0;
        boolean shouldReduceFPS = isForcingLowFPS || !client.isWindowFocused();
        if (!shouldReduceFPS && hasRenderedLastFrame) {
            hasRenderedLastFrame = false;
        }

        boolean shouldRender = isVisible && (!shouldReduceFPS || timeSinceLastRender > 1000);
        if (shouldRender) {
            lastRender = currentTime;
        } else {
            if (!hasRenderedLastFrame) {
                hasRenderedLastFrame = true;
                return true;
            }
            LockSupport.parkNanos("waiting to render", 30_000_000); // 30 ms
        }
        return shouldRender;
    }

    public interface WindowHolder {
        Window getWindow();
    }
}
