package com.randreucetti.encoder.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randreucetti.encoder.Encoder;

public class AdaptiveArithmeticEncoder implements Encoder {
	private static final int NUM_BITS = 16;
	private static final int B1 = (int) Math.pow(2, (NUM_BITS - 1));
	private static final int B2 = (int) Math.pow(2, (NUM_BITS - 2));
	private int L;
	private int R;
	private int bitsOutstanding;
	private int l, h, t, s, v;
	private OutputStream output;
	private InputStream input;
	private byte outputByte;
	private byte inputByte;
	private int numBits;
	private int[] ranges;
	private boolean dataAvailable = false;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public static Encoder getEncoder() {
		return new AdaptiveArithmeticEncoder();
	}

	private AdaptiveArithmeticEncoder() {

	}

	private void initEncoder() {
		logger.info("Encoder initialized with {} bits", NUM_BITS);
		L = 0;
		R = B1;
		bitsOutstanding = 0;
		t = 256;
		outputByte = 0;
		numBits = 0;
		ranges = new int[256];
		for (int i = 0; i < ranges.length; i++)
			ranges[i] = i;
	}

	private void initDecoder() {
		logger.info("Decoder initialized with {} bits", NUM_BITS);
		L = 0;
		R = B1;
		t = 256;
		outputByte = 0;
		numBits = 0;
		ranges = new int[256];
		for (int i = 0; i < ranges.length; i++)
			ranges[i] = i;
	}

	@Override
	public void encode(InputStream input, OutputStream output) throws IOException {
		initEncoder();
		this.output = output;
		while (input.available() > 0) {
			encode(input.read());
		}

	}

	@Override
	public void decode(InputStream input, OutputStream output) throws IOException {
		initDecoder();
		this.output = output;
		this.input = input;
		if (this.input.available() > 0) {
			v = input.read();
			dataAvailable = true;
		}
		while (dataAvailable) {
			decode();
		}
	}

	private void encode(int s) throws IOException {
		rangeOf(s);
		L = (L + R * l / t) * 2; // algorithms from notes, had to be adjusted slgihtly by adding *2
		R = R * (h - l) / t;
		if (R <= B2) {
			normalise();
		}
	}

	private void rangeOf(int s) {
		if (t > B2) {
			t = 0;
			for (int i = 0; i < ranges.length; i++) {
				ranges[i] = ranges[i] / 2 + 1;
				t = ranges[i] + t;
			}
		}
		l = ranges[s];
		if (s == 255)
			h = t;
		else
			h = ranges[s + 1];
		for (int i = s + 1; i < ranges.length; i++)
			ranges[i]++;
		t++;
	}

	private void normalise() throws IOException {
		while (R <= B2) {
			if (L + R <= B1) {
				bitPlusFollows(0);
			} else if (B1 <= L) {
				bitPlusFollows(1);
				L = L - B1;
			} else {
				bitsOutstanding++;
				L = L - B2;
			}
			L = L * 2;
			R = R * 2;
		}
	}

	private void normalizeDecoder() throws IOException // normalize method
	{
		while (R <= B2) {
			if (L + R <= B1) {

			} else if (B1 <= L) {
				L = L - B1;
				// V = V - b1;
			} else {
				L = L - B2;
				// V = V - b2;
			}
			L = L * 2;
			R = R * 2;
			v = v * 2;
			v = v % 256;
			getNextBit(); // reads a single bit from file and adds to V
		}
	}

	private void bitPlusFollows(int b) throws IOException {
		outputByte = (byte) ((outputByte << 1 | b));
		numBits++;
		if (numBits == 8) {
			output.write(outputByte);
			numBits = 0;
		}
		while (0 < bitsOutstanding) {
			outputByte = (byte) ((outputByte << 1) | (1 - b));
			numBits++;
			if (numBits == 8) {
				output.write(outputByte);
				numBits = 0;
			}
			bitsOutstanding--;
		}
	}

	private void getNextBit() throws IOException {
		if (numBits == 0 && input.available() > 0) {
			inputByte = (byte) input.read();
			numBits = 8;
		}

		if (input.available() <= 0) {
			dataAvailable = false;
		}
		if (inputByte < 0) {
			v++;
		}

		inputByte = (byte) ((inputByte << 1) | 0);
		numBits--;

	}

	private void decode() throws IOException {
		symbolOf(v);
		L = (L + R * l / t) * 2;
		R = R * (h - l) / t;

		if (R <= B2)
			normalizeDecoder();
		output.write(s);
	}

	private void symbolOf(int v) {
		if (t > B2) {
			t = 0;
			for (int i = 0; i < ranges.length; i++) {
				ranges[i] = ranges[i] / 2 + 1;
				t = ranges[i] + t;
			}
		}
		for (int i = 0; i < ranges.length; i++) {
			if (ranges[i] >= v) {
				s = i;
				l = ranges[i];
				if (i == 255)
					h = t;
				else
					h = ranges[i + 1];
				break;
			}
		}
		for (int i = h; i < ranges.length; i++)
			ranges[i]++;
		t++;
	}
}
