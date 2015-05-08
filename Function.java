package soundgraphs;

public class Function {
		
	public float calculate(int function, float input) {
		float output = 0;
		
		switch (function){
			case 1:	output = 1/input;
					break;
			case 2:	output = 1/(input*input);
					break;
			}
		return output;
	}
	
}

