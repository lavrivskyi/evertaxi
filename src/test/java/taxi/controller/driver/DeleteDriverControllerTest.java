package taxi.controller.driver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.dao.DriverDao;
import taxi.dao.impl.DriverDaoImpl;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeleteDriverControllerTest {
    private static final String ALL_DRIVERS_PATH = "/drivers";
    private static DeleteDriverController deleteDriverController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static String driverId;

    @BeforeClass
    public static void setBeforeAll(){
        Driver driver = new Driver("Petro", "PTR39459356");
        driver.setLogin("petro");
        driver.setPassword("qwerty");
        DriverDao driverDao = new DriverDaoImpl();
        driver = driverDao.create(driver);
        driverId = driver.getId().toString();
        deleteDriverController = new DeleteDriverController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws IOException {
        when(request.getParameter("id")).thenReturn(driverId);
        deleteDriverController.doGet(request, response);
        verify(response).sendRedirect(ALL_DRIVERS_PATH);
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