package com.randreucetti.encoder.impl;

import static org.junit.Assert.assertNotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.randreucetti.encoder.Encoder;

public class AdaptiveArithmeticEncoderTest {

	@Test
	public void testEncode() throws IOException {
		Encoder encoder = AdaptiveArithmeticEncoder.getEncoder();
		assertNotNull("Test file missing", getClass().getResource("/input.txt"));
		OutputStream output = new FileOutputStream("output.dat");
		encoder.encode(getClass().getResourceAsStream("/input.txt"), output);
		output.flush();
		output.close();

	}

	@Test
	public void testDecode() throws IOException {
		Encoder decoder = AdaptiveArithmeticEncoder.getEncoder();
		assertNotNull("Test file missing", getClass().getResource("/input.dat"));
		OutputStream output = new FileOutputStream("output.txt");
		decoder.decode(getClass().getResourceAsStream("/input.dat"), output);
		output.flush();
		output.close();
	}
}
