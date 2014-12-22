package se.vgregion.portal.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * This filter is placed in
 * @author Patrik Bergström
 */
public class Iso88591EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("ISO-8859-1");
        request.getParameterMap();
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
