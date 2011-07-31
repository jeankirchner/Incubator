package br.furb.tcc.sound.format;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WavFileReader {

	private final static int	FMT_CHUNK_ID	= 0x20746D66;
	private final static int	DATA_CHUNK_ID	= 0x61746164;
	private final static int	RIFF_CHUNK_ID	= 0x46464952;
	private final static int	RIFF_TYPE_ID	= 0x45564157;

	public static WavFile openWavFile(String fileName) throws IOException, WavFileException {
		return openWavFile(new File(fileName));
	}

	public static WavFile openWavFile(File file) throws IOException, WavFileException {
		return readWavFile(new BufferedInputStream(new FileInputStream(file)));
	}

	public static WavFile readWavFile(InputStream in) throws IOException, WavFileException {
		byte[] buffer = new byte[1024];

		// Read the first 12 bytes of the file
		int bytesRead = in.read(buffer, 0, 12);
		if (bytesRead != 12) {
			throw new WavFileException("Not enough wav file bytes for header");
		}

		// Extract parts from the header
		long riffChunkID = getLittleEndian(buffer, 0, 4);
		long chunkSize = getLittleEndian(buffer, 4, 4);
		long riffTypeID = getLittleEndian(buffer, 8, 4);

		// Check the header bytes contains the correct signature
		if (riffChunkID != RIFF_CHUNK_ID) {
			throw new WavFileException("Invalid Wav Header data, incorrect riff chunk ID");
		}
		if (riffTypeID != RIFF_TYPE_ID) {
			throw new WavFileException("Invalid Wav Header data, incorrect riff type ID");
		}

		boolean foundFormat = false;
		boolean foundData = false;

		int numChannels = 0;
		long sampleRate = 0;
		int blockAlign = 0;
		int validBits = 0;
		int numFrames = 0;

		// Search for the Format and Data Chunks
		while (true) {
			// Read the first 8 bytes of the chunk (ID and chunk size)
			bytesRead = in.read(buffer, 0, 8);
			if (bytesRead == -1) {
				throw new WavFileException("Reached end of file without finding format chunk");
			}
			if (bytesRead != 8) {
				throw new WavFileException("Could not read chunk header");
			}

			// Extract the chunk ID and Size
			long chunkID = getLittleEndian(buffer, 0, 4);
			chunkSize = getLittleEndian(buffer, 4, 4);

			// Word align the chunk size
			// chunkSize specifies the number of bytes holding data. However,
			// the data should be word aligned (2 bytes) so we need to calculate
			// the actual number of bytes in the chunk
			long numChunkBytes = (chunkSize % 2 == 1) ? chunkSize + 1 : chunkSize;

			if (chunkID == FMT_CHUNK_ID) {
				// Flag that the format chunk has been found
				foundFormat = true;

				// Read in the header info
				bytesRead = in.read(buffer, 0, 16);

				// Check this is uncompressed data
				int compressionCode = (int) getLittleEndian(buffer, 0, 2);
				if (compressionCode != 1) {
					throw new WavFileException("Compression Code " + compressionCode + " not supported");
				}

				// Extract the format information
				numChannels = (int) getLittleEndian(buffer, 2, 2);
				sampleRate = getLittleEndian(buffer, 4, 4);
				blockAlign = (int) getLittleEndian(buffer, 12, 2);
				validBits = (int) getLittleEndian(buffer, 14, 2);

				if (numChannels == 0) {
					throw new WavFileException("Number of channels specified in header is equal to zero");
				}
				if (blockAlign == 0) {
					throw new WavFileException("Block Align specified in header is equal to zero");
				}
				if (validBits < 2) {
					throw new WavFileException("Valid Bits specified in header is less than 2");
				}
				if (validBits > 64) {
					throw new WavFileException("Valid Bits specified in header is greater than 64, this is greater than a long can hold");
				}

				// Calculate the number of bytes required to hold 1 sample
				int bytesPerSample = (validBits + 7) / 8;
				if (bytesPerSample * numChannels != blockAlign) {
					throw new WavFileException("Block Align does not agree with bytes required for validBits and number of channels");
				}

				// Account for number of format bytes and then skip over
				// any extra format bytes
				numChunkBytes -= 16;
				if (numChunkBytes > 0) {
					in.skip(numChunkBytes);
				}
			} else if (chunkID == DATA_CHUNK_ID) {
				// Check if we've found the format chunk,
				// If not, throw an exception as we need the format information
				// before we can read the data chunk
				if (foundFormat == false) {
					throw new WavFileException("Data chunk found before Format chunk");
				}

				// Check that the chunkSize (wav data length) is a multiple of the
				// block align (bytes per frame)
				// if (chunkSize % blockAlign != 0) {
				// throw new WavFileException("Data Chunk size is not multiple of Block Align");
				// }

				// Calculate the number of frames
				numFrames = (int) (chunkSize / blockAlign);

				// Flag that we've found the wave data chunk
				foundData = true;

				break;
			} else {
				// If an unknown chunk ID is found, just skip over the chunk data
				in.skip(numChunkBytes);
			}
		}

		// Throw an exception if no data chunk has been found
		if (foundData == false) {
			throw new WavFileException("Did not find a data chunk");
		}

		int floatOffset;
		double floatScale;

		// Calculate the scaling factor for converting to a normalised double
		if (validBits > 8) {
			// If more than 8 validBits, data is signed
			// Conversion required dividing by magnitude of max negative value
			floatOffset = 0;
			floatScale = 1 << (validBits - 1);
		} else {
			// Else if 8 or less validBits, data is unsigned
			// Conversion required dividing by max positive value
			floatOffset = -1;
			floatScale = 0.5 * ((1 << validBits) - 1);
		}

		WavFile wav = new WavFile(numChannels, numFrames, validBits, (int) sampleRate);

		readFrames(wav, floatOffset, floatScale, in);

		return wav;
	}

	private static long getLittleEndian(byte[] buffer, int pos, int numBytes) {
		numBytes--;
		pos += numBytes;

		long val = buffer[pos] & 0xFF;
		for (int b = 0; b < numBytes; b++) {
			val = (val << 8) + (buffer[--pos] & 0xFF);
		}

		return val;
	}

	private static void readFrames(WavFile wav, int floatOffset, double floatScale, InputStream in) throws IOException, WavFileException {
		byte[] buffer = new byte[8192];

		int bufferPointer = 0;
		int bytesRead = 0;

		for (int f = 0; f < wav.getFramesCount(); f++) {
			for (int c = 0; c < wav.getNumChannels(); c++) {

				long val = 0;

				for (int b = 0; b < wav.getBytesPerSample(); b++) {
					if (bufferPointer == bytesRead) {
						int read = in.read(buffer, 0, buffer.length);
						if (read == -1) {
							throw new WavFileException("Not enough data available");
						}
						bytesRead = read;
						bufferPointer = 0;
					}

					int v = buffer[bufferPointer];

					if (b < wav.getBytesPerSample() - 1 || wav.getBytesPerSample() == 1) {
						v &= 0xFF;
					}
					val += v << (b * 8);

					bufferPointer++;
				}

				double sample = floatOffset + val / floatScale;
				wav.setSample(c, f, sample);
			}
		}
	}

}
