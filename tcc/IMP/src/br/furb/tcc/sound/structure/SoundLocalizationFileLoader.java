package br.furb.tcc.sound.structure;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.furb.tcc.sound.format.WavFileException;
import br.furb.tcc.sound.format.WavFileReader;


public class SoundLocalizationFileLoader {

	private final List<SoundLocalizationEntry>	entries	= new ArrayList<SoundLocalizationEntry>();

	public SoundLocalizationFileLoader(String path) throws IOException, WavFileException {
		File dir = new File(path);

		if (!dir.isDirectory()) {
			throw new RuntimeException("path must be an directory");
		}

		File[] ch1Files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("ch1_");
			}
		});
		File[] ch2Files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("ch2_");
			}
		});
		File[] ch3Files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("ch3_");
			}
		});

		Comparator<File> sortFunciton = new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				int a = Integer.parseInt(o1.getName().substring(4).replace(".wav", ""));
				int b = Integer.parseInt(o2.getName().substring(4).replace(".wav", ""));

				return a - b;
			}
		};

		Arrays.sort(ch1Files, sortFunciton);
		Arrays.sort(ch2Files, sortFunciton);
		Arrays.sort(ch3Files, sortFunciton);

		int count = Math.min(ch1Files.length, Math.min(ch2Files.length, ch3Files.length));

		for (int i = 0; i < count; i++) {
			entries.add(new SoundLocalizationEntry(WavFileReader.openWavFile(ch1Files[i]), WavFileReader.openWavFile(ch2Files[i]), WavFileReader.openWavFile(ch3Files[i])));
		}
	}

	public List<SoundLocalizationEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

}
