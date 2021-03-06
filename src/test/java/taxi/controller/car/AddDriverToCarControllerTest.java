package taxi.controller.car;

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
import taxi.dao.CarDao;
import taxi.dao.DriverDao;
import taxi.dao.ManufacturerDao;
import taxi.dao.impl.CarDaoImpl;
import taxi.dao.impl.DriverDaoImpl;
import taxi.dao.impl.ManufacturerDaoImpl;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddDriverToCarControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/cars/drivers/add.jsp";
    private static AddDriverToCarController addDriverToCarController;
    private static DriverDao driverDao;
    private static ManufacturerDao manufacturerDao;
    private static CarDao carDao;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcherGet;
    private static RequestDispatcher dispatcherPost;
    private static Long driverId;
    private static Long carId;

    @BeforeClass
    public static void setBeforeAll() {
        Driver vasyl = new Driver("Vasyl", "VAS4395739");
        vasyl.setLogin("vasyl");
        vasyl.setPassword("qwerty");
        Driver olesya = new Driver("Olesya", "OLE9359794833");
        olesya.setLogin("olesya");
        olesya.setPassword("qwerty");
        Driver stepan = new Driver("Stepan", "STP729467264");
        stepan.setLogin("stepan");
        stepan.setPassword("qwerty");
        Driver kateryna = new Driver("Kateryna", "KAT385683756");
        kateryna.setLogin("kateryna");
        kateryna.setPassword("qwerty");
        List<Driver> drivers = new ArrayList<>(List.of(vasyl, olesya, stepan, kateryna));
        Manufacturer bmw = new Manufacturer("BMW", "Germany");
        Manufacturer audi = new Manufacturer("Audi", "Germany");
        Car bmwCar = new Car("X5", bmw);
        bmwCar.setDrivers(new ArrayList<>(List.of(vasyl, olesya)));
        Car audiCar = new Car("RSQ7", audi);
        audiCar.setDrivers(new ArrayList<>(List.of(stepan, kateryna)));
        driverDao = new DriverDaoImpl();
        manufacturerDao = new ManufacturerDaoImpl();
        carDao = new CarDaoImpl();
        drivers.forEach(driver -> driverDao.create(driver));
        List.of(bmw, audi).forEach(manufacturer -> manufacturerDao.create(manufacturer));
        List.of(bmwCar, audiCar).forEach(car -> carDao.create(car));
        addDriverToCarController = new AddDriverToCarController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcherGet = mock(RequestDispatcher.class);
        dispatcherPost = mock(RequestDispatcher.class);
        driverId = stepan.getId();
        carId = bmwCar.getId();
    }

    @Test
    public void processGetRequest_doGet_OK() throws IOException, ServletException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcherGet);
        addDriverToCarController.doGet(request, response);
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(dispatcherGet).forward(request, response);
    }

    @Test
    public void processPostRequest_doPost_OK() throws IOException, ServletException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcherPost);
        when(request.getParameter("driver_id")).thenReturn(driverId.toString());
        when(request.getParameter("car_id")).thenReturn(carId.toString());
        addDriverToCarController.doPost(request, response);
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
