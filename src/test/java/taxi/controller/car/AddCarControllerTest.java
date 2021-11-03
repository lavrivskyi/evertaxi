package taxi.controller.car;

import static org.mockito.Mockito.mock;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import taxi.dao.DriverDao;
import taxi.dao.ManufacturerDao;
import taxi.dao.impl.DriverDaoImpl;
import taxi.dao.impl.ManufacturerDaoImpl;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

public class AddCarControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/cars/add.jsp";
    private static AddCarController addCarController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcherGet;
    private static RequestDispatcher dispatcherPost;
    private static Long manufacturerId;
    private static String carModel;

    @BeforeClass
    public static void setBeforeAll() {
        Driver vasyl = new Driver("Vasyl", "VAS4395739");
        vasyl.setLogin("vasyl");
        vasyl.setPassword("qwerty");
        List<Driver> drivers = new ArrayList<>(List.of(vasyl));
        Manufacturer bmw = new Manufacturer("BMW", "Germany");
        Car bmwCar = new Car("X5", bmw);
        bmwCar.setDrivers(drivers);
        DriverDao driverDao = new DriverDaoImpl();
        ManufacturerDao manufacturerDao = new ManufacturerDaoImpl();
        driverDao.create(vasyl);
        manufacturerDao.create(bmw);
        manufacturerId = bmw.getId();
        carModel = bmwCar.getModel();
        addCarController = new AddCarController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcherGet = mock(RequestDispatcher.class);
        dispatcherPost = mock(RequestDispatcher.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcherGet);
        addCarController.doGet(request, response);
        verify(dispatcherGet).forward(request, response);
    }

    @Test
    public void processPostRequest_doPost_OK() throws IOException, ServletException {
        when(request.getParameter("model")).thenReturn(carModel);
        when(request.getParameter("manufacturer_id")).thenReturn(manufacturerId.toString());
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcherPost);
        addCarController.doPost(request, response);
        verify(dispatcherPost).forward(request, response);
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        clearCarsDriversTable();
        clearDriversTable();
        clearCarsTable();
        clearManufacturersTable();
    }

    private static void clearCarsDriversTable() throws SQLException {
        String query = "DELETE FROM cars_drivers;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
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

    private static void clearCarsTable() throws SQLException {
        String query = "DELETE FROM cars;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
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