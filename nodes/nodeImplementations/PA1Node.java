package projects.paxosa1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import projects.paxosa1.nodes.messages.PA1Message;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.tools.Tools;

public class PA1Node extends Node {
	/*!< Status Values */
	private static final int IDLE    = 0;
	private static final int TRYING  = 1;
	private static final int POLLING = 2;
	
	/*!< DEBUG */
	//private final static boolean DEBUG = true;
	
	/* Node Actual Status */
	private int state;
	
	/*!< Unique Proposal ID */
	private ProposalID proposalID;
	
	/*!< Last Accepeted ID */
	private ProposalID lastAcceptedID;
	
	/*!< Value Proposed */
	private int proposedValue;
	
	private List<PA1Node> votes;
	
	private List<PA1Node> quorum;
	
	/*!< Text Size */
	private int textSize;

	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext()) {
			PA1Message msg = (PA1Message) inbox.next();
			PA1Message rps;
			
			switch(msg.type) {
				case PA1Message.PROPOSE:
					rps = new PA1Message(PA1Message.AGREE, this);
					send(rps, msg.sender);
					break;
					
				case PA1Message.AGREE:
					votes.add(msg.sender);
					
					if(votes.size() > (Tools.getNodeList().size() / 2) + 1) {
					}
					break; 
			}
		}
	}

	@Override
	public void preStep() {
	}

	@Override
	public void init() {
		textSize = 20;
		state = IDLE;
		proposedValue = 0;
		
		votes  = new ArrayList<PA1Node>();
		quorum = new ArrayList<PA1Node>();
		
		proposalID = new ProposalID(0, String.valueOf(this.ID));
	}

	@Override
	public void neighborhoodChange() {
		if(state == TRYING) {
			PA1Message propose = new PA1Message(PA1Message.PROPOSE, this, proposalID);
			broadcast(propose);
		}
	}

	@Override
	public void postStep() {
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}

	private static int radius;
	{ try {
		radius = Configuration.getIntegerParameter("GeometricNodeCollection/rMax");
	} catch(CorruptConfigurationEntryException e) {
		Tools.fatalError(e.getMessage());
	}}
	
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// Draws The Node
		Color bckup = g.getColor();
		g.setColor(Color.BLACK);
		String text = Integer.toString(proposedValue) + "|" + votes.size();
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.WHITE);
		
		// Draws The Radio
		pt.translateToGUIPosition(this.getPosition());
		int r = (int) (radius * pt.getZoomFactor());
		g.drawOval(pt.guiX - r, pt.guiY - r, r*2, r*2);
		g.setColor(bckup);
	}
	
	@NodePopupMethod(menuText="Start")
	public void start() {
		state = TRYING;
		votes.clear();
		proposalID.incrementNumber();
		
		Tools.appendToOutput("Start Proposing from node " + this.ID + "\n");
		this.setColor(Color.RED);
	}
	
	/*private void printDEBUG(String text) {
		if(DEBUG)
			System.out.println(text);
	}*/
}
