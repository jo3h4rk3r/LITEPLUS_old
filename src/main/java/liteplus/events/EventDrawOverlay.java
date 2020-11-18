
package liteplus.events;


import net.minecraft.client.util.math.MatrixStack;

public class EventDrawOverlay extends Event {

	public MatrixStack matrix;

	public EventDrawOverlay(MatrixStack matrix) {
		this.matrix = matrix;
	}
}
