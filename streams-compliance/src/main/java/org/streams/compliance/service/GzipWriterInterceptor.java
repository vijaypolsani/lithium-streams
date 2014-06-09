package org.streams.compliance.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

public class GzipWriterInterceptor implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		final OutputStream outputStream = context.getOutputStream();
		context.setOutputStream(new GZIPOutputStream(outputStream));
		context.proceed();
	}

}
