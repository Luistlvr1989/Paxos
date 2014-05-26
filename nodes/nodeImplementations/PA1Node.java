package projects.paxosa1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

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
	private static final int TRYING  = 0;
	private static final int POLLING = 0;
	
	/*!< DEBUG */
	private final static boolean DEBUG = true;
	
	private int status;
	
	/*!<  */
	private Integer outcome = null;
	
	private int lastTried;
	
	private int prevBal;
	
	private Integer prevDec = null;
	
	private int nextBal;

	@Override
	public void handleMessages(Inbox inbox) {
	}

	@Override
	public void preStep() {
		double try_cs = Math.random();
	}

	@Override
	public void init() {
	}

	@Override
	public void neighborhoodChange() {
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
	
	private void printDEBUG(String text) {
		if(DEBUG)
			System.out.println(text);
	}
}
