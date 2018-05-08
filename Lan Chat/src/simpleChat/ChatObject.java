package simpleChat;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods shared between Client and Server
 * @author Cade
 *
 */
public abstract class ChatObject {
	
	private String name;
	
	public void setName(String s) {
		name = s;
		System.out.println("Name: "+name);
	}
	
	public String getName() {
		return name;
	}

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
