package projects.paxosa1.nodes.messages;

import projects.paxosa1.nodes.nodeImplementations.PA1Node;
import sinalgo.nodes.messages.Message;

public class PA1Message extends Message {
	/*!< Message types */
	public final static int NEXT_BALLOT = 1;
	public final static int LAST_VOTE   = 2;
	
	public int type;
	public int data;
	public PA1Node sender;
	
	public PA1Message(int type, int data, PA1Node sender) {
		this.type = type;
		this.data = data;
		this.sender = sender;
	}

	@Override
	public Message clone() {
		return new PA1Message(type, data, sender);
	}
}
