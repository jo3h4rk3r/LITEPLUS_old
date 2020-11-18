package liteplus.mixin;

import liteplus.TitleScreen.MainMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }


    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        MinecraftClient.getInstance().openScreen(new MainMenu());

        addButton(new ButtonWidget(width / 2 - 124, height / 4 + 96, 20, 20, new LiteralText("TEST"), button -> {
            MainMenu.customTitleScreen = !MainMenu.customTitleScreen;
            client.openScreen(new TitleScreen(false));
        }));

    }




}
