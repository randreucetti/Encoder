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
	private OutputStream output;
	private byte outputByte;
	private int numBits;

	private AdaptiveArithmeticEncoder() {
		L = 0;
		R = B1;
		bitsOutstanding = 0;
		t = 256;
		outputByte = 0;
		numBits = 0;
	}

	@Override
	public void encode(InputStream input, OutputStream output) throws IOException {
		this.output = output;
		while (input.available() > 0) {
			encode(input.read());
		}

	}

	@Override
	public void decode(InputStream input, OutputStream output) {
		// TODO Auto-generated method stub

	}

	private void encode(int s) throws IOException {
		rangeOf(s);
		T = (R * l) / t;
		L = L + T;
		R = ((R * h) / t) - T;
		if (R <= B2) {
			normalise();
		}
	}

	private void rangeOf(int s) {
		l = s;
		h = s + 1;
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

	private void bitPlusFollows(int b) throws IOException {
		outputByte = (byte) ((outputByte << 1 | b));
		 if(numBits==8)
	        {
	            output.write(outputByte);
	            numBits=0;
	        }
	        while(0 < bitsOutstanding)
	        {
	            outputByte = (byte) ((outputByte<<1)|(1-b));
	            numBits++;
	            if(numBits==8)
	            {
	                output.write(outputByte);
	                numBits=0;
	            }
	            bitsOutstanding--;
	        }
	}
}
