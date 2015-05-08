package soundgraphs;

import java.util.Arrays;

import javax.swing.JOptionPane;

public class Test1{
	
	private Graph[] Graphs;
	private String[] answers;
	private boolean run;
    private boolean running;
    private boolean startedviewing;
    private int current;
    private int index;
    private int indexout;
    private int lastinput;
    
	public String[] run(){
		
		running = true;
		run = true;
		current = 0;
		Graphs = new Graph[3];
		answers = new String[3];
		generateGraphs();
		runtest();
		
		return answers;
	}
	
	private void runtest(){
		
		JOptionPane.showMessageDialog(null, "Start test one?");
		
		running = true;
		
		String[] options = new String[] {"1", "2", "3", "4"};
		
		for (int i = 0; i < Graphs.length; i++){
			current = i;
				int choice = JOptionPane.showOptionDialog(null, "Which picture most closely matches the audio?", 
					"Choose picture.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 
					options[0]);
				
				answers[i] = Integer.toString(choice);
		}
		running = false;
			
		JOptionPane.showMessageDialog(null, Arrays.toString(answers) + 
					" \n Test one complete");
	
	}

	private int getIndex(int f) {

		if (!startedviewing){
			startedviewing = true;
			indexout = index;
			lastinput = f;
		}		
		else if (f < lastinput){
			if ((Graphs[current].getLength() >= index) && (index > 0) ){
				index--;
				indexout = index;
			}
		}
		else if (f > lastinput){
			if (index < Graphs[current].getLength()){
				index++;
				System.out.println("index updated");
				indexout = index;
				}
		}
		else if (f == lastinput){
//			## do nothing ##
		}
		else {
			JOptionPane.showMessageDialog(null, "Error in WiiInterface.getIndex().");
		}

		lastinput = f;
		return indexout;
	}

	private void generateGraphs() {
		
		Graphs[0] = new Graph(20, 1); 	/*	y = x					*/
		Graphs[1] = new Graph(20, 2); 	/*	y = x squared			*/ 
		Graphs[2] = new Graph(20, 4); 	/*	y = x cubed				*/
		
//	  	Generate Graph objects
	    for(int i = 0; i < Graphs.length; i++){
		   	Graphs[i].generate();
	    	}
	   	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void currentIncrement(){
		if (current < Graphs.length){
			current++;
		}
	}
	public void currentDecrement(){
		if (current > 0){
			current--;
		}
	}

	public void playNote(int ax) {
		Graphs[current].getElement(getIndex(ax));
//		Output.playNote(Graphs[current].getElement(index));
	}
}