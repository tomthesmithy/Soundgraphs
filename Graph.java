package soundgraphs;

public class Graph {
		
	private double[] input;
	public double[] output;
	private double[] normalised;
	private double[] percentage;
	
	private int function;
	private int difference;
	
	public Graph(int elements, int function){
		
		this.function = function;
		difference = 20/elements;
		elements ++;
		input = new double[elements];
		input[0] = -10;
		output = new double[elements];
		normalised = new double[elements];
		percentage = new double[elements];
	}
	
//	##public methods##
	public void generate(){
		fillInputs();
		generateData();
		normaliseData();
		generatePercentage();
	}
	
	public double getElement(int index){
		
		double x = -1;
		
			if (index < 0 | index >= percentage.length){
//				do nothing
			}else{
				x = percentage[index];			
			}
		return x;
	}
	
	public String getValue(int index){
		String value = null;
		
		if (index < 0 | index >= output.length){
//			do nothing
		}else{
			value = input[index] + "/" + output[index];			
		}
		return value;
	}
	public int getLength(){
		return output.length;
	}
		
	public int getDifference(){
		return difference;
	}
	
	public double getPercentage(int index){
		return percentage[index-1];
	}
	
//	##private methods##	
	private void fillInputs(){
		for (int i = 1; i < input.length; i++){
			input[i] = input[i-1] + difference;
		}		
	}
	
	private void generateData(){
		for (int i = 0; i < output.length; i++){
			
		switch (function){
			case 1:	output[i] = input[i];
					break;
			case 2:	output[i] = Math.pow(input[i], 2);
					break;
			case 3:	output[i] = Math.pow(input[i], 3);
					break;
			case 4:	output[i] = Math.sqrt(input[i]+10);
					break;
			case 5:	output[i] = (input[i] * input[i] * input[i]) - 20*input[i];
					break;
			case 6:	output[i] = Math.sin(input[i]/1.6);
					break;
			case 7:	output[i] = Math.log(input[i]+11);
					break;
			case 8:	output[i] = (Math.exp(input[i])/1090);
					break;
			case 9:	output[i] = 1/(Math.pow(input[i], 2) + 10);
					break;
			case 10:output[i] = 0.1 * Math.pow(input[i], 2);
			}
		}
	}
	
	private void normaliseData() {
		
		double lowest = 0;
		
		for (int i=0; i < output.length; i++){
			if (output[i] < lowest){
				lowest = output[i];
			}
			else if (output[i] >5000){
				output[i] = 0;}
		}
		for(int i=0; i<output.length; i++){
			normalised[i] = output[i]-lowest;
		}
	}
		
	private void generatePercentage(){
		
		double highest = 0;
		
			for (int i=0; i<normalised.length; i++){
				if (normalised[i] > highest){
					highest = normalised[i];
				}
			}
			
			for (int i = 0; i < percentage.length; i++){
				percentage[i] = (100/highest) * normalised[i];
			}
		}
	}
	
	

