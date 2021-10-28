package taxi.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import taxi.exception.DataProcessingException;
import taxi.lib.Injector;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.service.CarService;
import taxi.service.DriverService;
import taxi.service.ManufacturerService;
import taxi.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CarServiceImplTest {
    private static final Logger logger = LogManager.getLogger(DriverServiceImplTest.class);
    private static final Injector injector = Injector.getInstance("taxi");
    private static final long DRIVER_INCORRECT_ID = 0L;
    private static final long CAR_INCORRECT_ID = 0L;
    private static DriverService driverService;
    private static ManufacturerService manufacturerService;
    private static CarService carService;
    private static Car bmwCar;
    private static Car audiCar;
    private static Driver kateryna;

    @BeforeClass
    public static void setBefore() {
        driverService = (DriverService) injector
                .getInstance(DriverService.class);
        manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);
        carService = (CarService) injector.getInstance(CarService.class);
    }

    @Before
    public void setUp() {
        Driver vasyl = new Driver("Vasyl", "VAS4395739");
        vasyl.setLogin("vasyl");
        vasyl.setPassword("qwerty");
        Driver olesya = new Driver("Olesya", "OLE9359794833");
        olesya.setLogin("olesya");
        olesya.setPassword("qwerty");
        Driver stepan = new Driver("Stepan", "STP729467264");
        stepan.setLogin("stepan");
        stepan.setPassword("qwerty");
        kateryna = new Driver("Kateryna", "KAT385683756");
        kateryna.setLogin("kateryna");
        kateryna.setPassword("qwerty");
        List<Driver> drivers = new ArrayList<>(List.of(vasyl, olesya, stepan, kateryna));
        drivers.forEach(driver -> driverService.create(driver));
        Manufacturer bmw = new Manufacturer("BMW", "Germany");
        Manufacturer audi = new Manufacturer("Audi", "Germany");
        List.of(bmw, audi).forEach(manufacturer -> manufacturerService.create(manufacturer));
        bmwCar = new Car("X5", bmw);
        bmwCar.setDrivers(new ArrayList<>(List.of(vasyl, olesya)));
        audiCar = new Car("RSQ7", audi);
        audiCar.setDrivers(new ArrayList<>(List.of(stepan, kateryna)));
        List.of(bmwCar, audiCar).forEach(car -> carService.create(car));
    }

    @Test
    public void createCar_create_Ok() {
        Car actual = carService.create(bmwCar);
        boolean isCarValid = actual.getId() != null
                && actual.getDrivers().equals(bmwCar.getDrivers())
                && actual.getManufacturer().equals(bmwCar.getManufacturer())
                && actual.getModel().equals(bmwCar.getModel());
        Assert.assertTrue(isCarValid);
    }

    @Test
    public void updateCar_update_Ok() {
        bmwCar.setModel("X7");
        Car actual = carService.update(bmwCar);
        Assert.assertEquals(bmwCar, actual);
    }

    @Test
    public void deleteCarById_delete_Ok() {
        carService.delete(audiCar.getId());
        Optional<Car> actual = getCarById(audiCar.getId());
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void getAllCarsByDriverId_getAllByDriver_Ok() {
        List<Car> expected = List.of(audiCar);
        List<Car> actual = carService.getAllByDriver(kateryna.getId());
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getAllCarsByDriverId_getAllByDriver_Not_Ok() {
        List<Car> expected = List.of();
        List<Car> actual = carService.getAllByDriver(DRIVER_INCORRECT_ID);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getCarById_get_Ok() {
        Car actual = carService.get(bmwCar.getId());
        Assert.assertEquals(bmwCar, actual);
    }

    @Test(expected = DataProcessingException.class)
    public void getManufacturerById_get_Not_Ok() {
        carService.get(CAR_INCORRECT_ID);
    }

    @Test
    public void getAllCars_getAll_Ok() {
        List<Car> expected = List.of(bmwCar, audiCar);
        List<Car> actual = carService.getAll();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllCars_getAll_NotOk() {
        List<Car> expected = List.of(bmwCar, audiCar);
        deleteCarById(bmwCar.getId());
        List<Car> actual = carService.getAll();
        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void addDriverToCar_addDriverToCar_Ok() {
        Driver petro = new Driver("Petro", "PTR353035354");
        petro.setLogin("petro");
        petro.setPassword("qwerty");
        driverService.create(petro);
        carService.addDriverToCar(petro, audiCar);
        Car actual = getCarById(audiCar.getId()).get();
        Assert.assertEquals(audiCar, actual);
    }

    @Test
    public void removeDriverFromCar_removeDriverFromCar_Ok() {
        carService.removeDriverFromCar(kateryna, audiCar);
        Car actualAudi = getCarById(audiCar.getId()).get();
        Assert.assertEquals(audiCar, actualAudi);

        carService.removeDriverFromCar(kateryna, bmwCar);
        Car actualBmw = getCarById(bmwCar.getId()).get();
        Assert.assertEquals(bmwCar, actualBmw);
    }

    @After
    public void tearDown() {
        clearCarsDriversTable();
        clearDriversTable();
        clearCarsTable();
        clearManufacturersTable();
    }

    private static void clearCarsDriversTable() {
        String query = "DELETE FROM cars_drivers;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            logger.error(throwables);
        }
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

    private static void clearCarsTable() {
        String query = "DELETE FROM cars;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            logger.error(throwables);
        }
    }

    private static void clearManufacturersTable() {
        String query = "DELETE FROM manufacturers;";
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            logger.error(throwables);
        }
    }

    private static Optional<Car> getCarById(Long id) {
        String selectQuery = "SELECT c.id as id, "
                + "model, "
                + "manufacturer_id, "
                + "m.name as manufacturer_name, "
                + "m.country as manufacturer_country "
                + "FROM cars c"
                + " JOIN manufacturers m on c.manufacturer_id = m.id"
                + " where c.id = ? AND c.is_deleted = false";
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                car = parseCarFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Can't get car by id: " + id, e);
            throw new DataProcessingException("Can't get car by id: " + id, e);
        }
        if (car != null) {
            car.setDrivers(getAllDriversByCarId(car.getId()));
        }
        return Optional.ofNullable(car);
    }

    private static List<Driver> getAllDriversByCarId(Long carId) {
        String selectQuery = "SELECT id, name, license_number, login, password "
                + "FROM cars_drivers cd "
                + "JOIN drivers d on cd.driver_id = d.id "
                + "where car_id = ? AND is_deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, carId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Driver> drivers = new ArrayList<>();
            while (resultSet.next()) {
                drivers.add(parseDriverFromResultSet(resultSet));
            }
            return drivers;
        } catch (SQLException e) {
            logger.error("Can't get all drivers by car id" + carId, e);
            throw new DataProcessingException("Can't get all drivers by car id" + carId, e);
        }
    }

    private static void deleteCarById(Long id) {
        String selectQuery = "UPDATE cars SET is_deleted = true WHERE id = ?"
                + " and is_deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete car by id " + id, e);
            throw new DataProcessingException("Can't delete car by id " + id, e);
        }
    }

    private static Driver parseDriverFromResultSet(ResultSet resultSet) throws SQLException {
        Long driverId = resultSet.getObject("id", Long.class);
        String name = resultSet.getNString("name");
        String licenseNumber = resultSet.getNString("license_number");
        String login = resultSet.getNString("login");
        String password = resultSet.getNString("password");
        Driver driver = new Driver(name, licenseNumber);
        driver.setId(driverId);
        driver.setLogin(login);
        driver.setPassword(password);
        return driver;
    }

    private static Car parseCarFromResultSet(ResultSet resultSet) throws SQLException {
        Long manufacturerId = resultSet.getObject("manufacturer_id", Long.class);
        String manufacturerName = resultSet.getNString("manufacturer_name");
        String manufacturerCountry = resultSet.getNString("manufacturer_country");
        Manufacturer manufacturer = new Manufacturer(manufacturerName, manufacturerCountry);
        manufacturer.setId(manufacturerId);
        Long carId = resultSet.getObject("id", Long.class);
        String model = resultSet.getNString("model");
        Car car = new Car(model, manufacturer);
        car.setId(carId);
        return car;
    }
}