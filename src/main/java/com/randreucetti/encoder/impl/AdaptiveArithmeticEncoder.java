package com.randreucetti.encoder.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.randreucetti.encoder.Encoder;

public class AdaptiveArithmeticEncoder implements Encoder {
	private static final int NUM_BITS = 16;
	private static final int B1 = (int) Math.pow(2, (NUM_BITS - 1));
	private static final int B2 = (int) Math.pow(2, (NUM_BITS - 2));
	private int L;
	private int R;
	private int T;
	private int bitsOutstanding;
	private int l, h, t;

	private AdaptiveArithmeticEncoder() {
		L = 0;
		R = B1;
		bitsOutstanding = 0;
		t = 256;
	}

	@Override
	public void encode(InputStream input, OutputStream output) throws IOException {
		while (input.available() > 0) {
			encode(input.read(), output);
		}

	}

	@Override
	public void decode(InputStream input, OutputStream output) {
		// TODO Auto-generated method stub

	}

	private void encode(int s, OutputStream output) {
		rangeOf(s);
		T = (R * l) / t;
	}

	private void rangeOf(int s) {
		l = s;
		h = s + 1;
	}

}
