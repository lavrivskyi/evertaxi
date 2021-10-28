package taxi.controller.manufacturer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.dao.ManufacturerDao;
import taxi.dao.impl.ManufacturerDaoImpl;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeleteManufacturerControllerTest {
    private static final String ALL_DRIVERS_PATH = "/manufacturers/all";
    private static DeleteManufacturerController deleteManufacturerController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static String manufacturerId;

    @BeforeClass
    public static void setBeforeAll(){
        Manufacturer bmw = new Manufacturer("BMW", "Germany");
        ManufacturerDao manufacturerDao = new ManufacturerDaoImpl();
        bmw = manufacturerDao.create(bmw);
        manufacturerId = bmw.getId().toString();
        deleteManufacturerController = new DeleteManufacturerController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws IOException {
        when(request.getParameter("id")).thenReturn(manufacturerId);
        deleteManufacturerController.doGet(request, response);
        verify(response).sendRedirect(ALL_DRIVERS_PATH);
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        clearManufacturersTable();
    }

    private static void clearManufacturersTable() throws SQLException {
        String query = "DELETE FROM manufacturers;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
