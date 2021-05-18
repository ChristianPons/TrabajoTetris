package gamecore.resources;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * 
 * @author Miguel Ruiz
 *
 */
public abstract class GameAction implements NativeKeyListener {

	private boolean pressed = false;
	private int lastKey = 0;
	private List<Integer> pressedKeys = new ArrayList<>();
	private final List<Integer> actionKeys;

	public abstract void pressTask();

	public abstract void releaseTask();

	public GameAction(int keyCode) {
		actionKeys = new ArrayList<>();
		actionKeys.add(keyCode);
	}

	public GameAction(List<Integer> actionKeys) {
		this.actionKeys = actionKeys;
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {

	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (actionKeys.contains(e.getKeyCode())) {
			if ((e.getKeyCode() != lastKey) && !pressed) {
				pressTask();
				pressed = true;
			}
			lastKey = e.getKeyCode();
			if(!pressedKeys.contains(e.getKeyCode())) {
				pressedKeys.add(e.getKeyCode());
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		pressedKeys.removeAll(Arrays.asList(e.getKeyCode()));
		if (pressedKeys.isEmpty() && pressed) {
			releaseTask();
			pressed = false;
			lastKey = 0;
		}
	}

	public boolean getPressed() {
		return pressed;
	}

	public int getLastKey() {
		return lastKey;
	}
}
