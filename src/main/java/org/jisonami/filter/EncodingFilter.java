package org.jisonami.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodingFilter implements Filter {

	private static String encoding;
	private static final String DEFAULT_CHARSET = "utf-8";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			if ("GET".equals(request.getMethod())) {
				EncodingHttpServletRequest encodingRequest = new EncodingHttpServletRequest(request);
				chain.doFilter(encodingRequest, resp);
			} else {
				request.setCharacterEncoding(encoding);
				chain.doFilter(request, resp);
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
		if (encoding == null || "".equals(encoding)) {
			encoding = DEFAULT_CHARSET;
		}
	}

	private class EncodingHttpServletRequest extends HttpServletRequestWrapper {
		private HttpServletRequest request;

		public EncodingHttpServletRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			String value = request.getParameter(name);
			if(value!=null){
				try {
					value = new String(value.getBytes("iso8859-1"), encoding);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return value;
		}
	}
}
