package com.lithium.streams.compliance.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplianceServerFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(ComplianceServerFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		log.info(">>> Request from Servlet filter. Request Params: " + request.getParameterMap() + " Chain: " + chain.toString());
		//Call the filter to carry-on
		chain.doFilter(request, response);
		//Play with response now.
		log.info(">>> Response from Servlet filter: " + response.toString());
		/*
		response.setContentType("gzip");
		//Call the filter to carry-on.
		chain.doFilter(request, wrappedResponse);
		*/
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {
	}
}