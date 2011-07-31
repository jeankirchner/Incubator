package br.furb.tcc.sound.structure;

import br.furb.tcc.sound.format.WavFile;
import br.furb.tcc.sound.utils.WavOptimizer;

public class SoundLocalizationEntry {

	private final WavFile	ch1;
	private final WavFile	ch2;
	private final WavFile	ch3;

	public SoundLocalizationEntry(WavFile ch1, WavFile ch2, WavFile ch3) {
		int min = Math.min(ch1.getSampleRate(), Math.min(ch2.getSampleRate(), ch3.getSampleRate()));
		min = Math.min(min, 20000);

		this.ch1 = WavOptimizer.optimizeFrameRate(ch1, min);
		this.ch2 = WavOptimizer.optimizeFrameRate(ch2, min);
		this.ch3 = WavOptimizer.optimizeFrameRate(ch3, min);
	}

	public WavFile getCh1() {
		return ch1;
	}

	public WavFile getCh2() {
		return ch2;
	}

	public WavFile getCh3() {
		return ch3;
	}

}
