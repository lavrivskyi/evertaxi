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
import javax.servlet.http.HttpSession;
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

public class GetMyCurrentCarsControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/cars/all.jsp";
    private static DriverDao driverDao;
    private static ManufacturerDao manufacturerDao;
    private static CarDao carDao;
    private static Long driverId;
    private static GetMyCurrentCarsController getMyCurrentCarsController;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static RequestDispatcher dispatcher;
    private static HttpSession session;
    private static Car bmwCar;

    @BeforeClass
    public static void setUp() {
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
        bmwCar = new Car("X5", bmw);
        bmwCar.setDrivers(new ArrayList<>(List.of(vasyl, olesya)));
        Car audiCar = new Car("RSQ7", audi);
        audiCar.setDrivers(new ArrayList<>(List.of(stepan, kateryna)));
        driverDao = new DriverDaoImpl();
        manufacturerDao = new ManufacturerDaoImpl();
        carDao = new CarDaoImpl();
        drivers.forEach(driver -> driverDao.create(driver));
        List.of(bmw, audi).forEach(manufacturer -> manufacturerDao.create(manufacturer));
        List.of(bmwCar, audiCar).forEach(car -> carDao.create(car));
        driverId = olesya.getId();
        getMyCurrentCarsController = new GetMyCurrentCarsController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("driver_id")).thenReturn(driverId);
        getMyCurrentCarsController.doGet(request, response);
        verify(request).setAttribute("cars", List.of(bmwCar));
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(dispatcher).forward(request, response);
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
