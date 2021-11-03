package taxi.controller.driver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddDriverControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/drivers/add.jsp";
    private static final String REQUEST_DISPATCHER_LOGIN_PATH = "/WEB-INF/views/login.jsp";
    private static RequestDispatcher dispatcherGet;
    private static RequestDispatcher dispatcherPost;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static AddDriverController addDriverController;

    @BeforeClass
    public static void setBeforeAll(){
        addDriverController = new AddDriverController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcherGet = mock(RequestDispatcher.class);
        dispatcherPost = mock(RequestDispatcher.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcherGet);
        addDriverController.doGet(request, response);
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(dispatcherGet).forward(request, response);
    }

    @Test
    public void processPostRequest_doPost_OK() throws IOException, ServletException {
        when(request.getParameter("name")).thenReturn("Kateryna");
        when(request.getParameter("license_number")).thenReturn("KAT3745673");
        when(request.getParameter("login")).thenReturn("kateryna");
        when(request.getParameter("password")).thenReturn("qwerty");
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_LOGIN_PATH)).thenReturn(dispatcherPost);
        addDriverController.doPost(request, response);
        verify(request).setAttribute("success_message",  "kateryna registered successfully!");
        verify(dispatcherPost).forward(request, response);
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        clearDriversTable();
    }

    private static void clearDriversTable() throws SQLException {
        String query = "DELETE FROM drivers;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
