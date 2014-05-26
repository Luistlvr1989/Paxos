package projects.paxosa1.nodes.messages;

import projects.paxosa1.nodes.nodeImplementations.PA1Node;
import projects.paxosa1.nodes.nodeImplementations.ProposalID;
import sinalgo.nodes.messages.Message;

public class PA1Message extends Message {
	/*!< Message Types */
	public static final int PROPOSE = 0;
	public static final int AGREE   = 1;
	
	public static final int ACCEPT  = 3;
	
	public int type;
	public PA1Node sender;
	private ProposalID proposalID;
	
	public PA1Message(int type, PA1Node sender, ProposalID proposalID) {
		this.type   = type;
		this.sender = sender;
		this.proposalID = proposalID;
	}

	@Override
	public Message clone() {
		return new PA1Message(this.type, this.sender, this.proposalID);
	}

}
