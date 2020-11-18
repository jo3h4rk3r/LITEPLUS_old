
package liteplus.events;


import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;

public class EventDrawContainer extends Event {

	private HandledScreen<?> screen;
	public int mouseX;
	public int mouseY;
	public MatrixStack matrix;

	public EventDrawContainer(HandledScreen<?> screen, int mX, int mY, MatrixStack matrix) {
		this.screen = screen;
		this.mouseX = mX;
		this.mouseY = mY;
		this.matrix = matrix;
	}

	public Screen getScreen() {
		return screen;
	}
}
