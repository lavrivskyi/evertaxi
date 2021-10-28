package taxi.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import taxi.exception.DataProcessingException;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.DriverService;
import taxi.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DriverServiceImplTest {
    private static final Logger logger = LogManager.getLogger(DriverServiceImplTest.class);
    private static final Injector injector = Injector.getInstance("taxi");
    private static final long DRIVER_INCORRECT_ID = 0L;
    private static final String DRIVER_INCORRECT_LOGIN = "incorrectLogin";
    private static final int DRIVER_LIST_INDEX = 1;
    private static DriverService driverService;
    private static List<Driver> drivers;

    @BeforeClass
    public static void setBeforeAll(){
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
        drivers = List.of(vasyl, olesya, stepan, kateryna);
        driverService = (DriverService) injector
                .getInstance(DriverService.class);
    }

    @Before
    public void setUp() {
        drivers.forEach(DriverServiceImplTest::createDriver);
    }

    @Test
    public void createDriver_create_Ok() {
        Driver expected = new Driver("Polina", "POL74628462");
        expected.setLogin("polina");
        expected.setPassword("qwerty");
        Driver actual = driverService.create(expected);
        boolean isValidDriver = actual.getId() != null
                && actual.getLogin().equals("polina")
                && actual.getName().equals("Polina")
                && actual.getLicenseNumber().equals("POL74628462")
                && actual.getPassword().equals("qwerty");
        Assert.assertTrue(isValidDriver);
    }

    @Test
    public void updateDriver_update_Ok() {
        Driver expected = drivers.get(DRIVER_LIST_INDEX);
        expected.setLogin("newLogin");
        Driver actual = driverService.update(expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteDriverById_delete_Ok() {
        driverService.delete(drivers.get(DRIVER_LIST_INDEX).getId());
        Optional<Driver> actual = getDriverById(drivers.get(DRIVER_LIST_INDEX)
                .getId());
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void getDriverById_get_Ok() {
        Driver expected = drivers.get(DRIVER_LIST_INDEX);
        Driver actual = driverService
                .get(drivers.get(DRIVER_LIST_INDEX).getId());
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DataProcessingException.class)
    public void getDriverById_get_Not_Ok() {
        driverService.get(DRIVER_INCORRECT_ID);
    }

    @Test
    public void getDriverByLogin_get_Ok() {
        Driver expected = drivers.get(DRIVER_LIST_INDEX);
        Optional<Driver> actual = driverService
                .findByLogin(drivers.get(DRIVER_LIST_INDEX).getLogin());
        Assert.assertTrue(actual.isPresent());
        Assert.assertEquals(expected, actual.get());
    }

    @Test
    public void getDriverByLogin_get_Not_Ok() {
        Optional<Driver> actual = driverService
                .findByLogin(DRIVER_INCORRECT_LOGIN);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void getAllDrivers_getAll_Ok() {
        List<Driver> expected = drivers;
        List<Driver> actual = driverService.getAll();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllDrivers_getAll_NotOk() {
        List<Driver> expected = drivers;
        deleteDriverById(drivers.get(DRIVER_LIST_INDEX).getId());
        List<Driver> actual = driverService.getAll();
        Assert.assertNotEquals(expected, actual);
    }

    @After
    public void tearDown() {
        clearDriversTable();
    }

    private static void clearDriversTable() {
        String query = "DELETE FROM drivers;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            logger.error(throwables);
        }
    }

    private static void createDriver(Driver driver) {
        String query = "INSERT INTO drivers (name, license_number, login, password) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getLogin());
            statement.setString(4, driver.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create "
                    + driver + ". ", e);
        }
    }

    private static Optional<Driver> getDriverById(Long id) {
        String query = "SELECT * FROM drivers WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Driver driver = null;
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
            return Optional.ofNullable(driver);
        } catch (SQLException e) {
            logger.error("Couldn't get driver by id " + id, e);
            throw new DataProcessingException("Couldn't get driver by id " + id, e);
        }
    }

    private static void deleteDriverById(Long id) {
        String query = "UPDATE drivers SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Couldn't delete driver with id " + id, e);
            throw new DataProcessingException("Couldn't delete driver with id " + id, e);
        }
    }

    private static Driver getDriver(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        String licenseNumber = resultSet.getString("license_number");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        Driver driver = new Driver(name, licenseNumber);
        driver.setId(id);
        driver.setLogin(login);
        driver.setPassword(password);
        return driver;
    }
}
