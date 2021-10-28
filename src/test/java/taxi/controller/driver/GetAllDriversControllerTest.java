package taxi.controller.driver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.dao.DriverDao;
import taxi.dao.impl.DriverDaoImpl;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GetAllDriversControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/drivers/all.jsp";
    private static GetAllDriversController getAllDriversController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcher;
    private static DriverDao driverDao;

    @BeforeClass
    public static void setBeforeAll() {
        driverDao = new DriverDaoImpl();
        Driver petro = new Driver("Petro", "PTR39459356");
        petro.setLogin("petro");
        petro.setPassword("qwerty");
        Driver kateryna = new Driver("Kateryna", "KAT9374935");
        kateryna.setLogin("kateryna");
        kateryna.setPassword("qwerty");
        List<Driver> drivers = new ArrayList<>(List.of(petro, kateryna));
        drivers.forEach(driver -> driverDao.create(driver));
        getAllDriversController = new GetAllDriversController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        getAllDriversController.doGet(request, response);
        verify(request).setAttribute("drivers", driverDao.getAll());
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
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
