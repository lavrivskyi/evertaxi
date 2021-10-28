package taxi.web.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationFilterTest {
    private static final String SERVLET_PATH = "/login";
    private static final String INDEX_PATH = "/index";
    private static final String ATTRIBUTE = "driver_id";
    private static final long DRIVER_ID = 1L;
    private static AuthenticationFilter authenticationFilter;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static HttpSession session;
    private static FilterChain filterChain;
    private static FilterConfig filterConfig;

    @Before
    public void setUp() {
        authenticationFilter = new AuthenticationFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        filterChain = mock(FilterChain.class);
        filterConfig = mock(FilterConfig.class);
        authenticationFilter.init(filterConfig);
    }

    @Test
    public void processFilter_doFilter_OK() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(ATTRIBUTE)).thenReturn(DRIVER_ID);
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        authenticationFilter.doFilter(request, response, filterChain);
        verify(response, never()).sendRedirect(SERVLET_PATH);
    }

    @Test
    public void processFilter_doFilter_Not_OK() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(ATTRIBUTE)).thenReturn(null);
        when(request.getServletPath()).thenReturn(INDEX_PATH);
        authenticationFilter.doFilter(request, response, filterChain);
        verify(response).sendRedirect(SERVLET_PATH);
    }
}
