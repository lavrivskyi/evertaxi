package taxi.controller.manufacturer;

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
import taxi.dao.ManufacturerDao;
import taxi.dao.impl.ManufacturerDaoImpl;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GetAllManufacturersControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/manufacturers/all.jsp";
    private static GetAllManufacturersController getAllManufacturersController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcher;
    private static ManufacturerDao manufacturerDao;

    @BeforeClass
    public static void setBeforeAll(){
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturerDao = new ManufacturerDaoImpl();
        manufacturers.add(new Manufacturer("BMW", "Germany"));
        manufacturers.add(new Manufacturer("Audi", "Germany"));
        manufacturers.add(new Manufacturer("Cadillac", "USA"));
        manufacturers.add(new Manufacturer("VolksWagen", "Germany"));
        getAllManufacturersController = new GetAllManufacturersController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        manufacturers.forEach(manufacturer -> manufacturerDao.create(manufacturer));
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        getAllManufacturersController.doGet(request, response);
        verify(request).setAttribute("manufacturers", manufacturerDao.getAll());
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(dispatcher).forward(request, response);
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
