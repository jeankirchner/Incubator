package br.furb.tcc.maths;

import java.math.BigDecimal;

import br.furb.tcc.sound.structure.SoundLocalizationEntry;
import br.furb.tcc.sound.utils.Utils;


public class Localization2D {

	public static Location localize(GeneralTrilaterationModel model, SoundLocalizationEntry entry) throws Localization2DInputError {

		double correlation12 = SlowCrossCorrelation.run(entry.getCh2().getChannel(0), entry.getCh1().getChannel(0));
		double d12 = Utils.distance(model.getReceptor1(), model.getReceptor2());
		double maxTime12 = d12 / Utils.speedOfSoundCmBySeconds();
		double seconds12 = (correlation12 / entry.getCh1().getSampleRate());

		if (maxTime12 < Math.abs(seconds12)) {
			// throw new Localization2DInputError("There are problems with receiver positions or sound input, max:" + maxTime12 + " milis: " + seconds12);
		}
		System.out.println("MaxTime: " + new BigDecimal(maxTime12));
		System.out.println("Time: " + new BigDecimal(seconds12));
		System.out.println("MaxFrames: " + (int) (maxTime12 * entry.getCh1().getSampleRate()));
		System.out.println("Frames: " + correlation12);

		double correlation13 = SlowCrossCorrelation.run(entry.getCh3().getChannel(0), entry.getCh1().getChannel(0));
		double d13 = Utils.distance(model.getReceptor1(), model.getReceptor3());
		double maxTime13 = d13 / Utils.speedOfSoundCmBySeconds();
		double seconds13 = (correlation13 / entry.getCh3().getSampleRate());

		if (maxTime13 < Math.abs(seconds13)) {
			// throw new Localization2DInputError("There are problems with receiver positions or sound input, max:" + maxTime13 + " milis: " + seconds13);
		}
		System.out.println("Max: " + new BigDecimal(maxTime13));
		System.out.println("Time: " + new BigDecimal(seconds13));
		System.out.println("MaxFrames: " + (int) (maxTime13 * entry.getCh1().getSampleRate()));
		System.out.println("Frames: " + correlation13);

		double correlation23 = SlowCrossCorrelation.run(entry.getCh2().getChannel(0), entry.getCh3().getChannel(0));
		double d23 = Utils.distance(model.getReceptor2(), model.getReceptor3());
		double maxTime23 = d23 / Utils.speedOfSoundCmBySeconds();
		double seconds23 = (correlation23 / entry.getCh3().getSampleRate());

		if (maxTime23 < Math.abs(seconds23)) {
			// throw new Localization2DInputError("There are problems with receiver positions or sound input, max:" + maxTime23 + " milis: " + seconds23);
		}
		System.out.println("Max: " + new BigDecimal(maxTime23));
		System.out.println("Time: " + new BigDecimal(seconds23));
		System.out.println("MaxFrames: " + (int) (maxTime23 * entry.getCh2().getSampleRate()));
		System.out.println("Frames: " + correlation23);

		System.out.println();
		// return new Trilateration(model).run(seconds12, seconds23, seconds13);
		return null;
	}
}
