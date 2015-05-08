package soundgraphs;

import com.jsyn.JSyn;
import com.jsyn.data.SegmentedEnvelope;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.WhiteNoise;
import com.softsynth.jsyn.EnvelopePlayer;
import com.softsynth.jsyn.SynthEnvelope;
import com.softsynth.jsyn.SynthException;

public class Output{
	
	private com.jsyn.Synthesizer synth1;
	private WhiteNoise noise;
//	private FilterStateVariable myFilter;
	private LineOut output;
//	private SawtoothOscillator osc;
	private SineOscillator sine;
	private SegmentedEnvelope ampEnvelope;
	private SegmentedEnvelope noiseAmpEnvelope;
	private SegmentedEnvelope notificationAmpEnvelope;
	private VariableRateMonoReader ampEnvelopeOut;
	private VariableRateMonoReader noiseEnvelopeOut;
	private VariableRateMonoReader notificationEnvelopeOut;
	private double[] envelopeData;
	private double[] noiseEnvelopeData;
	private double[] notificationData;
	private double lastNote;
	private double lowestFrequency;
	private double highestFrequency;

	
	public void startUp(){
		synth1 = JSyn.createSynthesizer();
		synth1.start( 44100, AudioDeviceManager.USE_DEFAULT_DEVICE, 2, 
					AudioDeviceManager.USE_DEFAULT_DEVICE, 2 );
		
		synth1.add( noise = new WhiteNoise() );
//		synth1.add( myFilter = new FilterStateVariable() );
//		synth1.add( osc = new SawtoothOscillator());
		synth1.add( sine = new SineOscillator());
		synth1.add( output = new LineOut() );
		synth1.add(notificationEnvelopeOut = new VariableRateMonoReader());
		synth1.add(ampEnvelopeOut = new VariableRateMonoReader());
		synth1.add(noiseEnvelopeOut = new VariableRateMonoReader());
		
		sine.frequency.set(400.00);
		envelopeData = new double[] {0, 0, 0.015, 0.5, 0.015, 0};
		ampEnvelope = new SegmentedEnvelope( envelopeData );
		notificationData = new double[] {0, 0, 0.015, 1, 0.015, 0};
		notificationAmpEnvelope = new SegmentedEnvelope( notificationData );
		noiseEnvelopeData = new double[] {0, 0, 0.0, 1, 0.010, 0};
		noiseAmpEnvelope = new SegmentedEnvelope( noiseEnvelopeData );
		ampEnvelopeOut.dataQueue.queue( ampEnvelope );	
		noiseEnvelopeOut.dataQueue.queue( noiseAmpEnvelope );
		notificationEnvelopeOut.dataQueue.queue( notificationAmpEnvelope );
		
		lastNote = 0;
		lowestFrequency = 250;
		highestFrequency = 1000;
		
		noiseEnvelopeOut.output.connect( noise.amplitude );
		noise.output.connect(0, output.input, 0);
		noise.output.connect(0, output.input, 1);
		ampEnvelopeOut.output.connect( sine.amplitude );
		sine.output.connect(0, output.input, 0);
		sine.output.connect(0, output.input, 1);
		notificationEnvelopeOut.output.connect( sine.amplitude );
		sine.output.connect(0, output.input, 0);
		sine.output.connect(0, output.input, 1);
					
		noiseEnvelopeOut.start();
		ampEnvelopeOut.start();
		notificationEnvelopeOut.start();
		output.start();
		
 	}
	
	public void playNote(double n){
		
//		if (n <= 30000){
		if (n==-1){
//			do nothing
		}else{
		double frequency = ((highestFrequency * (n/100)) + lowestFrequency);
		sine.frequency.set(frequency);
//		else {
//			n = lastNote;
//		}
			if (n != lastNote){
				ampEnvelopeOut.dataQueue.clear();
				ampEnvelopeOut.dataQueue.queueOn( ampEnvelope );
			}
		
		lastNote = n;	
		}
	}
	
	public void notification(){
		noiseEnvelopeOut.dataQueue.queueOn( noiseAmpEnvelope );
	}
	
	public void endOfArray(){
		sine.frequency.set(250);
		
		notificationEnvelopeOut.dataQueue.clear();
		notificationEnvelopeOut.dataQueue.queueOn( notificationAmpEnvelope );
		}
	
	public void outputMessage(double d, int i){}
}