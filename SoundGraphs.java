package soundgraphs;

import javax.swing.JOptionPane;

public class SoundGraphs {

	public static void main(String[] args) {
	
		int choice = JOptionPane.showConfirmDialog(null, "Would you like to begin a test?", "Startup", 
				JOptionPane.YES_NO_OPTION);
		
		if (choice == JOptionPane.YES_OPTION){
			TestInterface Interface = new TestInterface();
			Interface.startInterface();
		}else{
			WiiInterface Interface = new WiiInterface();
			Interface.startInterface();
		}		
	}
}
