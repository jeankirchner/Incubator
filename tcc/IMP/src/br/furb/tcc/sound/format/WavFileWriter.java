package br.furb.tcc.sound.format;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WavFileWriter {

	private final static int	FMT_CHUNK_ID	= 0x20746D66;
	private final static int	DATA_CHUNK_ID	= 0x61746164;
	private final static int	RIFF_CHUNK_ID	= 0x46464952;
	private final static int	RIFF_TYPE_ID	= 0x45564157;

	public static void writeWavFile(WavFile wav, String file) throws WavFileException, IOException {
		writeWavFile(wav, new BufferedOutputStream(new FileOutputStream(file)));
	}

	public static void writeWavFile(WavFile wav, OutputStream out) throws WavFileException, IOException {

		// Sanity check arguments
		validateWavFormat(wav);

		int blockAlign = wav.getBytesPerSample() * wav.getNumChannels();

		// Calculate the chunk sizes
		long dataChunkSize = blockAlign * wav.getFramesCount();

		long mainChunkSize = 4 + // Riff Type
				8 + // Format ID and size
				16 + // Format data
				8 + // Data ID and size
				dataChunkSize;

		boolean wordAlignAdjust;

		// Chunks must be word aligned, so if odd number of audio data bytes
		// adjust the main chunk size
		if (dataChunkSize % 2 == 1) {
			mainChunkSize += 1;
			wordAlignAdjust = true;
		} else {
			wordAlignAdjust = false;
		}

		byte[] buffer = new byte[1024];

		// Set the main chunk size
		putLittleEndian(RIFF_CHUNK_ID, buffer, 0, 4);
		putLittleEndian(mainChunkSize, buffer, 4, 4);
		putLittleEndian(RIFF_TYPE_ID, buffer, 8, 4);

		// Write out the header
		out.write(buffer, 0, 12);

		// Put format data in buffer
		long averageBytesPerSecond = wav.getSampleRate() * blockAlign;

		putLittleEndian(FMT_CHUNK_ID, buffer, 0, 4); // Chunk ID
		putLittleEndian(16, buffer, 4, 4); // Chunk Data Size
		putLittleEndian(1, buffer, 8, 2); // Compression Code (Uncompressed)
		putLittleEndian(wav.getNumChannels(), buffer, 10, 2); // Number of channels
		putLittleEndian(wav.getSampleRate(), buffer, 12, 4); // Sample Rate
		putLittleEndian(averageBytesPerSecond, buffer, 16, 4); // Average Bytes Per Second
		putLittleEndian(blockAlign, buffer, 20, 2); // Block Align
		putLittleEndian(wav.getValidBits(), buffer, 22, 2); // Valid Bits

		// Write Format Chunk
		out.write(buffer, 0, 24);

		// Start Data Chunk
		putLittleEndian(DATA_CHUNK_ID, buffer, 0, 4); // Chunk ID
		putLittleEndian(dataChunkSize, buffer, 4, 4); // Chunk Data Size

		// Write Format Chunk
		out.write(buffer, 0, 8);

		int floatOffset;
		double floatScale;

		// Calculate the scaling factor for converting to a normalised double
		if (wav.getValidBits() > 8) {
			// If more than 8 validBits, data is signed
			// Conversion required multiplying by magnitude of max positive value
			floatOffset = 0;
			floatScale = Long.MAX_VALUE >> (64 - wav.getValidBits());
		} else {
			// Else if 8 or less validBits, data is unsigned
			// Conversion required dividing by max positive value
			floatOffset = 1;
			floatScale = 0.5 * ((1 << wav.getValidBits()) - 1);
		}

		int bufferPointer = 0;
		for (int f = 0; f < wav.getFramesCount(); f++) {
			for (int c = 0; c < wav.getNumChannels(); c++) {

				long value = (long) (floatScale * (floatOffset + wav.getSample(c, f)));

				for (int b = 0; b < wav.getBytesPerSample(); b++) {
					if (bufferPointer == buffer.length) {
						out.write(buffer, 0, buffer.length);
						bufferPointer = 0;
					}

					buffer[bufferPointer] = (byte) (value & 0xFF);
					value >>= 8;
					bufferPointer++;
				}

			}
		}
		out.write(buffer, 0, bufferPointer);

		out.close();
	}

	private static void validateWavFormat(WavFile wav) throws WavFileException {
		if (wav.getNumChannels() < 1 || wav.getNumChannels() > 65535) {
			throw new WavFileException("Illegal number of channels, valid range 1 to 65536");
		}
		if (wav.getFramesCount() < 0) {
			throw new WavFileException("Number of frames must be positive");
		}
		if (wav.getValidBits() < 2 || wav.getValidBits() > 65535) {
			throw new WavFileException("Illegal number of valid bits, valid range 2 to 65536");
		}
		if (wav.getSampleRate() < 0) {
			throw new WavFileException("Sample rate must be positive");
		}
	}

	private static void putLittleEndian(long val, byte[] buffer, int pos, int numBytes) {
		for (int b = 0; b < numBytes; b++) {
			buffer[pos] = (byte) (val & 0xFF);
			val >>= 8;
			pos++;
		}
	}

}
