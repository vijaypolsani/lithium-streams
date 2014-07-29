package com.lithium.streams.compliance.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Compress
public class GzipWriterInterceptor implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		final OutputStream outputStream = context.getOutputStream();
		context.getHeaders().putSingle("Content-Encoding", "gzip");
		context.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
		context.getHeaders().putSingle("Access-Control-Allow-Methods", "GET");
		//context.getHeaders().putSingle("Content-Type", "application/json");
		context.setOutputStream(new GZIPOutputStream(outputStream));
		context.proceed();
		return;
	}

}
