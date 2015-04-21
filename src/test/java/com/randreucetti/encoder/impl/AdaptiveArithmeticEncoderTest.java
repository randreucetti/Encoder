package com.randreucetti.encoder.impl;

import static org.junit.Assert.assertNotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.randreucetti.encoder.Encoder;

public class AdaptiveArithmeticEncoderTest {

	@Test
	public void testEncode() throws IOException{
		Encoder encoder = AdaptiveArithmeticEncoder.getEncoder();
		assertNotNull("Test file missing", getClass().getResource("/merged_document.pdf"));
		OutputStream output = new FileOutputStream("output.dat");
		encoder.encode(getClass().getResourceAsStream("/merged_document.pdf"), output);
		output.flush();
		output.close();
	}
	
	
}
