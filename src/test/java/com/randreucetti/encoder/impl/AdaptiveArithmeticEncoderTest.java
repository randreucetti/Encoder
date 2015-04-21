package com.randreucetti.encoder.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.randreucetti.encoder.Encoder;

public class AdaptiveArithmeticEncoderTest {

	@Test
	public void testEncode() throws IOException{
		Encoder encoder = new AdaptiveArithmeticEncoder();
		assertNotNull("Test file missing", getClass().getResource("/merged_document.pdf"));
		encoder.encode(getClass().getResourceAsStream("/merged_document.pdf"), System.out);
	}
}
