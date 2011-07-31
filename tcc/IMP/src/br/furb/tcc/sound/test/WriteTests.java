package br.furb.tcc.sound.test;

import static java.lang.Math.cos;
import br.furb.tcc.sound.format.WavFile;
import br.furb.tcc.sound.format.WavFileWriter;

public class WriteTests {

	private static final double	_360	= Math.toRadians(360);

	public static void main(String[] args) {
		try {
			int sampleRate = 44100; // Samples per second
			double duration = 1.0; // Seconds

			int channels = 1;

			// Calculate the number of frames required for specified duration
			int numFrames = (int) (duration * sampleRate);

			// Create a wav file with the name specified as the first argument
			WavFile wav = new WavFile(channels, numFrames, 16, sampleRate);

			// Initialise a local frame counter
			int frame = 0;

			// Loop until all frames written
			int dlay = 10;

			while (frame < numFrames - dlay) {
				// Determine how many frames to write, up to a maximum of the buffer size
				double amp = 1;
				double freq = 600;

				// Fill the buffer, one tone per channel
				wav.setSample(0, frame + dlay, powWave(sampleRate, frame));
				// buffer[1][s] = sinusoidal(frameCounter);
				// buffer[0][s] = triangular(frameCounter);

				frame++;
			}

			WavFileWriter.writeWavFile(wav, "./sound_output/teste3.wav");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private static double powWave(int sampleRate, long frameCounter) {
		return Math.sin((_360 * Math.pow(frameCounter, 2.0)) / sampleRate);
	}

	private static double sinusoidal(long x) {
		return cos(_360 * x / 50) - cos(_360 * x * 3 / 50) / 3 + cos(_360 * x * 5 / 50) / 5 - cos(_360 * x * 7 / 50) / 7 + cos(_360 * x * 9 / 50) / 9 - cos(_360 * x * 11 / 50) / 11
				+ cos(_360 * x * 13 / 50) / 13 - cos(_360 * x * 15 / 50) / 15 + cos(_360 * x * 17 / 50) / 17 - cos(_360 * x * 19 / 50) / 19;
	}

	private static double triangular(long x) {
		return cos(_360 * x / 50) + cos(_360 * x * 3 / 50) / 9 + cos(_360 * x * 5 / 50) / 25 + cos(_360 * x * 7 / 50) / 49 + cos(_360 * x * 9 / 50) / 81;
	}

}
