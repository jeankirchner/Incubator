package br.furb.tcc.sound.format;

/**
 * A.Greensted http://www.labbookpages.co.uk <br>
 * File format is based on the information from: <br>
 * http://www.sonicspot.com/guide/wavefiles.html <br>
 * http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/wave.htm
 * 
 * Version 1.0
 * 
 * @author Jean Kirchner
 */
public class WavFile {

	// Wav Header
	private final int			numChannels;
	private final int			sampleRate;
	private final int			validBits;

	// Frames data
	private final int			framesCount;
	private final double[][]	frameData;

	public WavFile(int numChannels, int framesCount, int validBits, int sampleRate) {
		this.numChannels = numChannels;
		this.validBits = validBits;
		this.sampleRate = sampleRate;
		this.framesCount = framesCount;
		frameData = new double[numChannels][framesCount];
	}

	public int getNumChannels() {
		return numChannels;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getValidBits() {
		return validBits;
	}

	public int getFramesCount() {
		return framesCount;
	}

	public double getSample(int channel, int sampleFrame) {
		return frameData[channel][sampleFrame];
	}

	public int getBytesPerSample() {
		return (validBits + 7) / 8;
	}

	public void setSample(int channel, int frame, double sample) {
		frameData[channel][frame] = sample;
	}

	public double[] getChannel(int chNum) {
		double[] ch = new double[frameData[chNum].length];
		System.arraycopy(frameData[chNum], 0, ch, 0, ch.length);
		return ch;
	}
}
