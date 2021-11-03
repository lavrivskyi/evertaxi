package taxi.controller;

import static org.mockito.Mockito.mock;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import taxi.dao.DriverDao;
import taxi.dao.impl.DriverDaoImpl;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;

public class IndexControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/index.jsp";
    private static final String DRIVER_ID = "driver_id";
    private static IndexController indexController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcher;
    private static HttpSession session;
    private static Driver driver;

    @BeforeClass
    public static void setUp() {
        indexController = new IndexController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);
        DriverDao driverDao = new DriverDaoImpl();
        driver = new Driver("Admin", "ADM4395739");
        driver.setLogin("admin");
        driver.setPassword("qwerty");
        driverDao.create(driver);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(DRIVER_ID)).thenReturn(driver.getId());
        indexController.doGet(request, response);
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
