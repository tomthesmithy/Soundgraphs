package soundgraphs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

public class TestInterface implements WiimoteListener{
	
		
	    Wiimote wiimote;
	    private Graph[] test1Graphs;
		private Graph[][] test2Graphs;
	    private Graph[] test3Graphs;
		String[] answers;
		int graphsmax;
		int answersindex;
	    private Output output;
		private boolean IROn;
		private boolean startedviewing;
		private boolean currentshift;
		private boolean test1;
		private boolean test2;
		private boolean test3;
		private int	lastInput;
		private int index;
		private int indexOut;
		private int currentarray; /*	graphs array currently being evaluated		*/
		private int currentindex; /*	graph currently being evaluated				*/
			
	public void startInterface(){
		generateGraphs();
		initialiseAudio();
		initialiseWiimote();
		runTests();
		
		int choice = JOptionPane.showConfirmDialog(null, "Would you like to perform another test?", "Startup", 
				JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION){
			runTests();
		}else{
			System.exit(0);
		}
	}

	private void runTests() {
		
		IROn = false;
		currentshift = false;
		answers = new String[21];
		answersindex = 0;
		startedviewing = false;
		lastInput = 0;
		index = 0;
		indexOut = 0;
		currentarray = 0; 
		currentindex = 0;
		
		getDemographics();
		test1();
		test2();
		test3();
		eval();	
	}



//	#####	TESTS	######	
	private void test1(){
		
		JOptionPane.showMessageDialog(null, "Start test one?");
			test1 = true;
			String[] options = new String[] {"1", "2", "3", "4"};
		
		for (Graph g : test1Graphs){
				int choice = JOptionPane.showOptionDialog(null, "Which picture most closely matches the audio?", 
					"Choose picture.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 
					options[0]);
			
				addToArray(options[choice]);
				if (currentindex < test1Graphs.length){
					currentindex++;
				}
			IROn = false;
			wiimote.deactivateIRTRacking();
			wiimote.deactivateMotionSensing();
		}
		test1 = false;
		currentindex = 0;
		
		JOptionPane.showMessageDialog(null, Arrays.toString(answers) + 
			" \n Test one complete");
	}	
//	#####	FIRST TEST END	#####
	
	
//	#####	SECOND TEST	#####
	private void test2() {
		
		JOptionPane.showMessageDialog(null, "Start test two?");
		test2 = true;
		
		while(test2==true){
//			do nothing
		}
		
		IROn = false;
		wiimote.deactivateIRTRacking();
		wiimote.deactivateMotionSensing();
		currentindex = 0;
		
		JOptionPane.showMessageDialog(null, Arrays.toString(answers) + 
			" \n Test two complete");
	}
//	#####	SECOND TEST END	#####
	
	
//	#####	THIRD TEST	#####
	private void test3(){
		JOptionPane.showMessageDialog(null, "Start test three?");
			test3 = true;
				while(test3){
					/*do nothing*/
			}
				
			IROn = false;
			wiimote.deactivateIRTRacking();
			wiimote.deactivateMotionSensing();
			
		JOptionPane.showMessageDialog(null, Arrays.toString(answers) + 
					" \n Test three complete");
		
	}
//	#####	THIRD TEST END	#####


	private void eval() {
		
		wiimote.deactivateIRTRacking();
		wiimote.deactivateMotionSensing();

		String[] options = new String[] {"1", "2", "3", "4", "5", "6", "7"};
		String[] questions = new String[] 
				{"How well could you visualise/imagine the shape of the graph in your head?",
				"How easy did you find it to browse a soundgraph?",
				"How easy did you find it to navigate between soundgraphs?",
				"How easy did you find it to mark a point?",
				"Did you think the choice of sound/pitches was suitable?",
				"How would you rate the usability of the interface as a whole?"};
	
//	Qualitative Evaluation	
		for (int i = 0; i < 6; i++){
				int choice = JOptionPane.showOptionDialog(null, questions[i], 
					"Evaluation.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 
					options[0]);
			
				addToArray(options[choice]);
			}
		
//	Comments
		addToArray(JOptionPane.showInputDialog("comments"));
		
		JOptionPane.showMessageDialog(null, Arrays.toString(answers) + 
				" \n Evaluation complete");
		
// Save results?
		int save = JOptionPane.showConfirmDialog(null, "Would you like to save the results of this test?",
				"Save?", JOptionPane.YES_NO_OPTION);
		if (save == JOptionPane.YES_OPTION){
			save();
		}
		
		int choice = JOptionPane.showConfirmDialog(null, "Would you like to perform another test?", 
				"Again?", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION){
			runTests();
		}else{
			System.exit(0);
		}
	}	
		
	
	@Override
	public void onMotionSensingEvent(MotionSensingEvent arg0) {
				
	}	
	
	@Override
	public void onIrEvent(IREvent arg0) {

		int ax = arg0.getAx();
		ax = ((ax + 5) / 10) * 10; 		/*Round to nearest 10*/

    	if(ax % 30 == 0 || ax == 0){
    		if (test1){
	    		output.playNote(test1Graphs[currentindex].getElement(getIndex(ax)));
    		}
    		else if (test2){
        		output.playNote(test2Graphs[currentarray][currentindex].getElement(getIndex(ax)));
    		}
    		else if (test3){
	    		output.playNote(test3Graphs[currentindex].getElement(getIndex(ax)));
    		}
    		else {
//    			do nothing
    			}
   			}
    	}

	public int getIndex(int f) {

		if (!startedviewing){
			startedviewing = true;
			indexOut = index;
			lastInput = f;
		}
		
		else if (f < lastInput){
			if(test1){
				if ((test1Graphs[currentindex].getLength() >= index) && (index > 0) ){
					index--;
					indexOut = index;
				}
			}
			else if(test2){
				if ((test2Graphs[currentarray][currentindex].getLength() >= index) && (index > 0) ){
					index--;
					indexOut = index;
				}
			}
			else if(test3){
				if ((test3Graphs[currentindex].getLength() >= index) && (index > 0) ){
					index--;
						indexOut = index;
				}
			}
		}
			else if (f > lastInput){
				if (test1){
					if (index < test1Graphs[currentindex].getLength()){
						index++;
							indexOut = index;
					}
			}
			else if (test2){
				if (index < test2Graphs[currentarray][currentindex].getLength()){							
						index++;
							indexOut = index;
						}
				}
			else if (test3){
				if (index < test3Graphs[currentindex].getLength()){
						index++;
							indexOut = index;
					}
				}
			}
		else if (f == lastInput){
//			## do nothing ##
		}else {
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
				System.out.println("b! yes");
				IROn = true;
				}
			else{
				wiimote.deactivateIRTRacking();
				wiimote.deactivateMotionSensing();
				IROn = false;
				System.out.println("b! no");
			}
		}
	
		else if(arg0.isButtonHomeJustPressed()){
			System.exit(0);
		}
//	
////		#### testing control buttons ####
////		
////		if(arg0.isButtonRightPressed()){
////			wiimote.activateMotionSensing();
////			wiimote.deactivateIRTRacking();
////			output.playNote(60);
////		}
////		else if(arg0.isButtonLeftPressed()){
////			wiimote.deactivateMotionSensing();
////			output.playNote(40);
////		}
////		else if(arg0.isButtonUpPressed()){
////			wiimote.activateIRTRacking();
////			wiimote.deactivateMotionSensing();
////				output.playNote(60);
////		}
////		else if (arg0.isButtonDownPressed()){
////			wiimote.deactivateIRTRacking();
////				output.playNote(40);
////		}
	}

//	###### Control Events from Nunchuk ######
	@Override
	public void onExpansionEvent(ExpansionEvent arg0) {
		if(arg0 instanceof NunchukEvent){
			
//	##### Events from Nunchuk joystick	#####
			NunchukEvent nunchuk = (NunchukEvent) arg0;
				JoystickEvent joystick = nunchuk.getNunchukJoystickEvent();
					float joystickangle = joystick.getAngle();
						float joystickmagnitude = joystick.getMagnitude();
					
//	#####	Events from Nunchuk accelerometers	#####
			MotionSensingEvent motion = nunchuk.getNunchukMotionSensingEvent();
				RawAcceleration acceleration = motion.getRawAcceleration();
					int xaccel = (((acceleration.getX() + 5) / 10) * 10);
					int yaccel = (((acceleration.getY() + 5) / 10) * 10);
						
//	##### 	Events from Nunchuk buttons	#####
			NunchukButtonsEvent button = nunchuk.getButtonsEvent();
				if(button.isButtonZJustPressed()){}
			
//	###### 	React to Nunchuk forward tilt 	######
			if (yaccel > 140 && !currentshift){
				currentshift = true;
					if(test1){
						test1YAxis();
					}
					else if(test2){
						test2YAxis();
					}
					else if(test3){
						test3YAxis();
					}
			}
//	###### 	React to Nunchuk sideways tilt 	######
			else if (xaccel < 110 && !currentshift){
				currentshift = true;
				boolean switchleft = true;
					if(test1){
						test1XAxis(switchleft);
						}
					else if(test2){
						test2XAxis(switchleft);
						}
					else if (test3){
						test3XAxis(switchleft);
						}
				
			}else if (xaccel > 140 && !currentshift){
				currentshift = true;
				boolean switchleft = false;
					if(test1){
						test1XAxis(switchleft);
						}
					else if(test2){
						test2XAxis(switchleft);
						}
					else if (test3){
						test3XAxis(switchleft);
						}
					
			}else if (currentshift){
				if( xaccel < 140 && xaccel > 110 && yaccel < 150){
					currentshift = false;
				}
			}
		}
	}

//	#####	Respond to Nunchuk Y Axis Event	#####
	private void test1YAxis() {}

	private void test2YAxis() {
		
		output.notification();
		
		String[][] graphs = {	{"y = x", "y = sin(x)", "y = 1/(x squared)", "y = x squared"},
								{"y + exp(x)", "y = x cubed", "y = x", "y = (x cubed) - 20x"}};
		
		int add = JOptionPane.showConfirmDialog(null, "Are you happy with the graph you've selected?",
				"Mark?", JOptionPane.YES_NO_OPTION);
		if (add == JOptionPane.YES_OPTION){
			
			addToArray(graphs[currentarray][currentindex]);
								
			IROn = false;
			wiimote.deactivateIRTRacking();
			wiimote.deactivateMotionSensing();
		
			if (currentarray < test2Graphs.length-1){
				currentarray++;
			}else{
				test2 = false;
			}
		}
	}
	
	private void test3YAxis() {
		
		output.notification();
		
		int add = JOptionPane.showConfirmDialog(null, "Are you happy with the point that you've marked?",
				"Mark?", JOptionPane.YES_NO_OPTION);
		if (add == JOptionPane.YES_OPTION){
						
			addToArray(test3Graphs[currentindex].getValue(index));
			
			IROn = false;
			wiimote.deactivateIRTRacking();
			
			wiimote.deactivateMotionSensing();
			
			if (currentindex < test3Graphs.length -1){
				currentindex++;
			}else{
				test3 = false;
			}
		}
	}

	
//	#####	Respond to Nunchuk X Axis Event	#####
	private void test1XAxis(boolean switchleft){}
	
	private void test2XAxis(boolean switchleft){
	
		if(switchleft){
			if(currentindex > 0){
				currentindex--;
				output.notification();
			}else{
				output.endOfArray();
			}
			
		}else{
			if(currentindex < test2Graphs[currentarray].length-1){
				currentindex++;
				output.notification();
			}else{
				output.endOfArray();
			}
		}
	}
	
	private void test3XAxis(boolean switchleft){}
	
	
//	#####  	Start audio output	#####
	private void initialiseAudio() {
	    output = new Output();
	    output.startUp();
	}
	
//	##### 	Initalise audio		#####
	private void initialiseWiimote() {
		Wiimote[] wiimotes = WiiUseApiManager.getWiimotes(1, true);
	    Wiimote wiimote = wiimotes[0]; /*can add capability to connect more than one WiiMote*/
	    wiimote.addWiiMoteEventListeners(this);
	}

//	##### Demographic questions	#####
	private void getDemographics() {
		String[] quals = new String[] {"PhD", "Masters", "Degree", "A level", "GCSE", "None"};
		String[] wii = new String[] {"Daily", "Weekly", "Monthly", "Less than once a month",
				"Never"};
			
			addToArray(JOptionPane.showInputDialog(null, "Please enter your name:"));
			addToArray(JOptionPane.showInputDialog(null, "Please enter your age:"));
			addToArray(JOptionPane.showInputDialog(null, "Please state your gender"));
			addToArray(JOptionPane.showInputDialog(null, "Please specify any hearing difficulties:"));
			addToArray(quals[JOptionPane.showOptionDialog(null, "What is the highest level qualification "
					+ "you have in maths?", "Highest maths qualification.", JOptionPane.DEFAULT_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, quals, quals[0])]);
			addToArray(wii[JOptionPane.showOptionDialog(null, "How frequently do you use a Wii console?", 
					"How often do you Wii?.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, wii, 
					wii[0])]);
			}

//	##### Add information to array	#####
	private void addToArray(String entry) {
			answers[answersindex] = entry;
				answersindex++;	
		}
	//	##### Add data from incoming arrays to array	#####
@SuppressWarnings("unused")
private void addToArray(String[] entries) { 
	/* for planned use of test classes returning arrays of data */
			for (String i: entries){
				addToArray(i);
			}
		}
	
//	#####	Generate graph data for each test	#####
	
	private void generateGraphs() {
			
			test1Graphs = new Graph[] {		new Graph(20, 1)	/*	y = x				*/, 
											new Graph(20, 2)	/*	y = x squared		*/,
											new Graph(20, 4)	/*	y = sqrt(x)			*/};
			
			
			test2Graphs = new Graph[][] {{	new Graph(20, 1),	/*	y = x				*/
											new Graph(20, 6),	/*	y = sin(x)			*/
											new Graph(20, 9),	/*	y = 1/(x squared)	*/
											new Graph(20, 2)	/*	y = x squared		*/
										},
										{	new Graph(20, 8),	/*	y = exp(x)			*/
											new Graph(20, 3),	/*	y = x cubed			*/
											new Graph(20, 1),	/*	y = x				*/
											new Graph(20, 5)	/*	y = x cubed - 20x	*/
										}};
			
			test3Graphs = new Graph[] { 	new Graph(20, 9), 	/*	y = 1/(x squared)	*/
											new Graph(20, 3),	/*	y = x cubed			*/
											new Graph(20, 2)};	/*	y = x squared		*/
			
			for(Graph g : test1Graphs){
				g.generate();
			}
			for(Graph[]a : test2Graphs){
				for (Graph g : a){
					g.generate();
				}
			}
			for(Graph g : test3Graphs){
				g.generate();
				for(int i = 0; i < 20; i++){
					System.out.println("Element " + i + " + " + g.getElement(i));
				}
			}
		}
	
	
	private void save() {
		try{
			FileWriter writer = new FileWriter("testresults.txt", true);
				writer.write("\n" + Arrays.toString(answers));
				writer.close();
			}
			catch(IOException e){
				e.printStackTrace();
			};
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
