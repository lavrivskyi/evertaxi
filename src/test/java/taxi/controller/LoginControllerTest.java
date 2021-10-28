package taxi.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import javax.servlet.http.HttpSession;
import taxi.dao.DriverDao;
import taxi.dao.impl.DriverDaoImpl;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/login.jsp";
    private static final String INDEX_PATH = "/index";
    private static final String INCORRECT_PASSWORD = "qwerty123";
    private static LoginController loginController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcher;
    private static HttpSession session;
    private static Driver admin;

    @BeforeClass
    public static void setBeforeAll(){
        DriverDao driverDao = new DriverDaoImpl();
        admin = new Driver("Admin", "ADM4395739");
        admin.setLogin("admin");
        admin.setPassword("qwerty");
        driverDao.create(admin);
        loginController = new LoginController();
    }

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        loginController.doGet(request, response);
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void processPostRequest_doPost_OK() throws IOException, ServletException {
        when(request.getParameter("login")).thenReturn(admin.getLogin());
        when(request.getParameter("password")).thenReturn(admin.getPassword());
        when(request.getSession()).thenReturn(session);
        loginController.doPost(request, response);
        verify(session).setAttribute("driver_id", admin.getId());
        verify(response).sendRedirect(INDEX_PATH);
    }

    @Test
    public void processPostRequest_doPost_Not_OK() throws ServletException, IOException {
        when(request.getParameter("login")).thenReturn(admin.getLogin());
        when(request.getParameter("password")).thenReturn(INCORRECT_PASSWORD);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        loginController.doPost(request, response);
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(request, never()).getSession();
        verify(dispatcher).forward(request, response);
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
