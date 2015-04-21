package com.randreucetti.encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Encoder {
	public void encode(InputStream input, OutputStream output) throws IOException;

	public void decode(InputStream input, OutputStream output);
}
