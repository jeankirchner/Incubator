package br.furb.tcc.sound.utils;

import br.furb.tcc.sound.format.WavFile;

public class WavOptimizer {

	public static WavFile optimizeFrameRate(WavFile wav, int newFrameRate) {
		if (wav.getSampleRate() <= newFrameRate) {
			return wav;
		}
		double ratio = newFrameRate / (double) wav.getSampleRate();
		int newFramesCount = (int) (wav.getFramesCount() * ratio);
		WavFile newWav = new WavFile(wav.getNumChannels(), newFramesCount, wav.getValidBits(), newFrameRate);
		double increment = 1 / ratio;
		for (double frame = 0; frame < newFramesCount; frame += increment) {
			for (int i = 0; i < wav.getNumChannels(); i++) {
				newWav.setSample(i, (int) frame, wav.getSample(i, (int) frame));
			}
		}
		return wav;

	}

}
