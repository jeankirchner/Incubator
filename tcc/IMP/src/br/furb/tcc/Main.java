package br.furb.tcc;

import java.io.IOException;
import java.util.List;

import br.furb.tcc.maths.GeneralTrilaterationModel;
import br.furb.tcc.maths.Localization2D;
import br.furb.tcc.maths.Localization2DInputError;
import br.furb.tcc.maths.TrilaterationModelUtils;
import br.furb.tcc.sound.format.WavFileException;
import br.furb.tcc.sound.structure.SoundLocalizationEntry;
import br.furb.tcc.sound.structure.SoundLocalizationFileLoader;


public class Main {

	public static void main(String[] args) throws IOException, WavFileException {

		GeneralTrilaterationModel model = TrilaterationModelUtils.create2DModel(0, 0, 30);

		SoundLocalizationFileLoader loader = new SoundLocalizationFileLoader("./sound_input");
		List<SoundLocalizationEntry> entries = loader.getEntries();

		int count = 0;
		for (SoundLocalizationEntry soundLocalizationEntry : entries) {
			try {
				Localization2D.localize(model, soundLocalizationEntry);
			} catch (Localization2DInputError e) {
				e.printStackTrace();
			}
			count++;
		}

	}
}