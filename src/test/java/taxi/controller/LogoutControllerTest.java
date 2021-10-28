package taxi.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogoutControllerTest {
    private static final String INDEX_PATH = "/";
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static HttpSession session;
    private static LogoutController logoutController;

    @BeforeClass
    public static void setUp() {
        logoutController = new LogoutController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        logoutController.doGet(request, response);
        verify(response).sendRedirect(INDEX_PATH);
    }
}
