package org.streams.compliance.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DumpServlet extends HttpServlet {
	public DumpServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>DumpServlet</h1><pre>");
		response.getWriter().println("requestURI=" + request.getRequestURI());
		response.getWriter().println("contextPath=" + request.getContextPath());
		response.getWriter().println("servletPath=" + request.getServletPath());
		response.getWriter().println("pathInfo=" + request.getPathInfo());
		response.getWriter().println("session=" + request.getSession(true).getId());

		String r = request.getParameter("resource");
		if (r != null)
			response.getWriter().println("resource(" + r + ")=" + getServletContext().getResource(r));

		response.getWriter().println("</pre>");
	}
}