package br.furb.tcc.sound.utils;
import java.io.IOException;
import java.util.Scanner;

import br.furb.tcc.sound.format.WavFile;
import br.furb.tcc.sound.format.WavFileException;
import br.furb.tcc.sound.format.WavFileReader;
import br.furb.tcc.sound.format.WavFileWriter;


public class SplitWavFiles {
	static WavFile	ch1;
	static WavFile	ch2;
	static WavFile	ch3;

	public static void main(String[] args) throws IOException, WavFileException {
		ch1 = WavFileReader.openWavFile("./sound_project/Canal 01.wav");
		ch2 = WavFileReader.openWavFile("./sound_project/Canal 02.wav");
		ch3 = WavFileReader.openWavFile("./sound_project/Canal 03.wav");

		Scanner in = new Scanner(System.in);

		System.out.println("Digite os tempos em segundos");
		int count = 0;

		while (!in.hasNext("exit")) {
			double d1 = in.nextDouble();
			double d2 = in.nextDouble();
			splitWav(count, d1, d2);
			count++;
		}

	}

	private static void splitWav(int count, double from, double to) throws WavFileException, IOException {
		String name = "./sound_input/";
		WavFile wav1 = subWav(ch1, from, to);
		WavFile wav2 = subWav(ch2, from, to);
		WavFile wav3 = subWav(ch3, from, to);

		WavFileWriter.writeWavFile(wav1, name + "ch1_" + count + ".wav");
		WavFileWriter.writeWavFile(wav2, name + "ch2_" + count + ".wav");
		WavFileWriter.writeWavFile(wav3, name + "ch3_" + count + ".wav");

	}

	private static WavFile subWav(WavFile wav, double from, double to) {

		int fromFrame = (int) (from * wav.getSampleRate());
		int toFrame = (int) (to * wav.getSampleRate());

		WavFile newWav = new WavFile(1, toFrame - fromFrame, wav.getValidBits(), wav.getSampleRate());

		for (int i = fromFrame; i < toFrame; i++) {
			newWav.setSample(0, i - fromFrame, wav.getSample(0, i));
		}

		return newWav;

	}

}
