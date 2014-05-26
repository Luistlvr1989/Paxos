package projects.paxosa1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	
	/*!< Status */
	private int status;
	
	/*!< Set of votes received  */
	private List<PA1Node> prevVotes;
	
	/*!< The number of the last ballot in which it voted */
	private int prevBal;
	
	/*!< The number of the last ballot in which it agreed to participate */
	private int nextBal;
	
	/*!< Number of the last ballot tried */
	private int lastTried;
	
	private HashMap<PA1Node, Integer> nodes;
	////////////////////////////////////////////////
	private int quorumSize;
	
	private int proposedValue;
	
	private int lastAccepted;
	
	private HashSet<String> promisesReceived;
	

	@Override
	public void handleMessages(Inbox inbox) {
		while(inbox.hasNext()) {
			PA1Message msg = (PA1Message) inbox.next();
			PA1Message response;
			
			switch(msg.type) {
				case PA1Message.NEXT_BALLOT:
					if(msg.data > nextBal) {
						nextBal = msg.data;
					}
					
					if(nextBal > prevBal) {
						response = new PA1Message(PA1Message.LAST_VOTE, nextBal, this);
						send(response, msg.sender);
					}
					
					break;
					
				case PA1Message.LAST_VOTE:
					if(msg.data == lastTried && status == TRYING) {
						prevVotes.add(msg.sender);
					}
					
					break;
			}
		}
	}

	@Override
	public void preStep() {
		if(status == TRYING && prevVotes.size() >= (Tools.getNodeList().size() / 2) + 1) { 
		}
	}

	@Override
	public void init() {
		status = IDLE;
		lastTried = 0;
		prevVotes = new ArrayList<PA1Node>();
		nextBal = 0;
		prevBal = 0;
	}

	@Override
	public void neighborhoodChange() {
		if(status == TRYING) {
			PA1Message msg = new PA1Message(PA1Message.NEXT_BALLOT, lastTried, this);
			broadcast(msg);
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
	
	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
		Color bckup = g.getColor();
		g.setColor(Color.BLACK);
		this.drawingSizeInPixels = (int) (defaultDrawingSizeInPixels * pt.getZoomFactor());
		super.drawAsDisk(g, pt, highlight, drawingSizeInPixels);
		g.setColor(Color.BLUE);
		pt.translateToGUIPosition(this.getPosition());
		int r = (int) (radius * pt.getZoomFactor());
		g.drawOval(pt.guiX - r, pt.guiY - r, r*2, r*2);
		g.setColor(bckup);
	}
	
	/**
	 * Initiate a message to be sent by this node in the next round. This starts the
	 * process of resending the message infinitely.
	 */
	@NodePopupMethod(menuText="Start")
	public void start() {
		status = TRYING;
		prevVotes.clear();
		lastTried++;
		
		this.setColor(Color.RED);
		Tools.appendToOutput("Start Propose from node " + this.ID + "\n");
	}
	
	/*private void printDEBUG(String text) {
		if(DEBUG)
			System.out.println(text);
	}*/
}
