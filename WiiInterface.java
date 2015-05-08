package soundgraphs;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.values.GForce;
import wiiusej.values.Orientation;
import wiiusej.values.RawAcceleration;
import wiiusej.wiiusejevents.physicalevents.ExpansionEvent;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.JoystickEvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.NunchukButtonsEvent;
import wiiusej.wiiusejevents.physicalevents.NunchukEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.DisconnectionEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.StatusEvent;

public class WiiInterface implements WiimoteListener{
	
	Wiimote[] wiimotes = WiiUseApiManager.getWiimotes(1, true);
    Wiimote wiimote = wiimotes[0];
    
	private Graph[] Graphs;
    private Output output;
    private ArrayList<String> markedPoints;
	private boolean IROn;
	private boolean startedViewing;
	private boolean currentshift;
	private int	lastInput = 0;
	private int index = 0;
	private int indexOut = 0;
	private int current = 0; /*	graph currently being evaluated		*/

		
	public void startInterface(){
//	 initialise parameters 
		IROn = false;
		currentshift = false;
		Graphs = new Graph[10];
		markedPoints = new ArrayList<String>();
		
		generateGraphs();
		initialiseAudio();
		initialiseWiimote();
		JOptionPane.showMessageDialog(null, "does this work?");
	}


	@Override
	public void onMotionSensingEvent(MotionSensingEvent arg0) {
				
		RawAcceleration acceleration = arg0.getRawAcceleration();
    	acceleration.getZ();
    }
	
	@Override
	public void onIrEvent(IREvent arg0) {
		
		arg0.getX();
		int ax = arg0.getAx();
		
		ax = ((ax + 5) / 10) * 10; 		/*Round to nearest 10*/
		
    	if(ax % 30 == 0 || ax == 0){
    		output.outputMessage(Graphs[current].getElement(getIndex(ax)), indexOut);
    		output.playNote(Graphs[current].getElement(index));
    	}
	}

	private int getIndex(int f) {

		if (!startedViewing){
			startedViewing = true;
			indexOut = index;
			lastInput = f;
		}		
		else if (f < lastInput){
			if ((Graphs[current].getLength() >= index) && (index > 0) ){
				index--;
				indexOut = index;
			}
		}
		else if (f > lastInput){
			if (index < Graphs[current].getLength()){
				index++;
				System.out.println("index updated");
				indexOut = index;
				}
		}
		else if (f == lastInput){
//			## do nothing ##
		}
		else {
			JOptionPane.showMessageDialog(null, "Error in WiiInterface.getIndex().");
		}

		lastInput = f;
		return indexOut;
	}

	@Override
	public void onButtonsEvent(WiimoteButtonsEvent arg0) {
		
		if(arg0.isButtonBJustPressed()){
			if (!IROn){
				wiimote.activateIRTRacking();
				wiimote.activateMotionSensing();
				IROn = true;
			}
			else{
				wiimote.deactivateIRTRacking();
				wiimote.deactivateMotionSensing();
				IROn = false;
			}
		}
		
		if(arg0.isButtonHomeJustPressed()){
			System.exit(0);
		}
	}

//	###### Control Events from Nunchuk ######
	@Override
	public void onExpansionEvent(ExpansionEvent arg0) {
		if(arg0 instanceof NunchukEvent){
			NunchukEvent nunchuk = (NunchukEvent) arg0;
				JoystickEvent joystick = nunchuk.getNunchukJoystickEvent();
					joystick.getAngle();
						joystick.getMagnitude();
					
			MotionSensingEvent motion = nunchuk.getNunchukMotionSensingEvent();
				RawAcceleration acceleration = motion.getRawAcceleration();
					int xaccel = (((acceleration.getX() + 5) / 10) * 10);
					int yaccel = (((acceleration.getY() + 5) / 10) * 10);
						
			NunchukButtonsEvent button = nunchuk.getButtonsEvent();
				if(button.isButtonZJustPressed()){	
			}
						
//			###### 	React to Nunchuk forward movement 	######
			if (yaccel > 140 && !currentshift){
				currentshift = true;
				markedPoints.add(Graphs[current].getValue(index));
				System.out.println(Graphs[current].getValue(index));
				
				output.notification();
			}
//			###### 	React to Nunchuk sideways movement 	######
			else if (xaccel < 110 && !currentshift){
				currentshift = true;
					if(current > 0){
						output.notification();
						current--;
							System.out.println(current + " " + xaccel);
					}
			} else if (xaccel > 140 && !currentshift){
				currentshift = true;
					if(current < 8){
						output.notification();
						current++;
							System.out.println(current + " " + xaccel);
						}
			}else if (currentshift){
				if( xaccel < 140 && xaccel > 110 && yaccel < 150){
				currentshift = false;
				}
			}
		}
	}
//	Graph declaration - (no. of points to calculate, function to evaluate)	
	private void generateGraphs() {
		
		Graphs[0] = new Graph(20, 1); 	/*	y = x					*/
		Graphs[1] = new Graph(20, 2); 	/*	y = x squared			*/ 
		Graphs[2] = new Graph(20, 3); 	/*	y = x cubed				*/
		Graphs[3] = new Graph(20, 4); 	/*	y = sqrt(x)				*/
		Graphs[4] = new Graph(20, 5);	/*	y = x cubed - 9x		*/ 
		Graphs[5] = new Graph(20, 6);	/*	y = sin(x)				*/
		Graphs[6] = new Graph(20, 7);	/*	y = log(x)				*/
		Graphs[7] = new Graph(20, 8);	/*	y = exp(x)				*/
		Graphs[8] = new Graph(20, 9);	/*	y = 1/(x squared + 10)	*/
		Graphs[9] = new Graph(20,10);	/*	y = 0.1 * x squared		*/
		
//	  	Generate Graph objects
	    for(int i = 0; i < Graphs.length; i++){
		   	Graphs[i].generate();
		 }
	}

//	  Start audio output
	private void initialiseAudio() {
	    output = new Output();
	    output.startUp();
	}
	
	private void initialiseWiimote() {
		Wiimote[] wiimotes = WiiUseApiManager.getWiimotes(1, true);
	    Wiimote wiimote = wiimotes[0]; /*can add capability to connect more than one WiiMote*/
	    wiimote.addWiiMoteEventListeners(this);
	}
	
	
	
	
//	############### Unimplemented inherited methods ##############
	
	@Override
	public void onClassicControllerInsertedEvent(
			ClassicControllerInsertedEvent arg0) {}
//	do nothing
	
	@Override
	public void onClassicControllerRemovedEvent(
			ClassicControllerRemovedEvent arg0) {}
//	do nothing
	
	@Override
	public void onDisconnectionEvent(DisconnectionEvent arg0) {}
//	do nothing

	@Override
	public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent arg0) {}
//	do nothing
	
	@Override
	public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent arg0) {}
//	do nothing
	
	@Override
	public void onNunchukInsertedEvent(NunchukInsertedEvent arg0) {}
//	do nothing
	
	@Override
	public void onNunchukRemovedEvent(NunchukRemovedEvent arg0) {}
//	do nothing
	
	@Override
	public void onStatusEvent(StatusEvent arg0) {}
//	do nothing
	
}
