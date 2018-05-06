package simpleChat;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods shard between Client and Server
 * @author Cade
 *
 */
public abstract class ChatObject {

	private List<InputListener> inputListeners = new ArrayList<InputListener>();

	public List<InputListener> getInputListeners() {
		return inputListeners;
	}

	public void addInputListener(InputListener toAdd) {
		inputListeners.add(toAdd);
	}

	public void removeInputListener(InputListener toRemove) {
		inputListeners.remove(toRemove);
	}

	public void receiveMessage(String s) {
		// Notify everybody that may be interested.
		for (InputListener il : inputListeners)
			il.messageReceived(s);
	}
}
