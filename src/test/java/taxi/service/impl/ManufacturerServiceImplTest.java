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
import taxi.model.Manufacturer;
import taxi.service.ManufacturerService;
import taxi.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManufacturerServiceImplTest {
    private static final Logger logger = LogManager.getLogger(ManufacturerServiceImplTest.class);
    private static final Injector injector = Injector.getInstance("taxi");
    private static final long MANUFACTURER_INCORRECT_ID = 0L;
    private static final int MANUFACTURER_LIST_INDEX = 1;
    private static ManufacturerService manufacturerService;
    private static List<Manufacturer> manufacturers;

    @BeforeClass
    public static void setBeforeAll(){
        manufacturers = new ArrayList<>();
        manufacturers.add(new Manufacturer("BMW", "Germany"));
        manufacturers.add(new Manufacturer("Audi", "Germany"));
        manufacturers.add(new Manufacturer("Cadillac", "USA"));
        manufacturers.add(new Manufacturer("VolksWagen", "Germany"));
        manufacturerService = (ManufacturerService) injector
                .getInstance(ManufacturerService.class);
    }

    @Before
    public void setUp() {
        manufacturers.forEach(ManufacturerServiceImplTest::addManufacturer);
    }

    @Test
    public void createManufacturer_create_Ok() {
        Manufacturer expected = new Manufacturer("Skoda", "Czech Republic");
        Manufacturer actual = manufacturerService.create(expected);
        boolean isValidManufacturer = actual.getId() != null
                && actual.getCountry().equals("Czech Republic")
                && actual.getName().equals("Skoda");
        Assert.assertTrue(isValidManufacturer);
    }

    @Test
    public void updateManufacturer_update_Ok() {
        Manufacturer expected = manufacturers.get(MANUFACTURER_LIST_INDEX);
        expected.setCountry("Ukraine");
        Manufacturer actual = manufacturerService.update(expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteManufacturerById_delete_Ok() {
        manufacturerService.delete(manufacturers.get(MANUFACTURER_LIST_INDEX).getId());
        Optional<Manufacturer> actual = getManufacturer(manufacturers.get(MANUFACTURER_LIST_INDEX)
                .getId());
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void getManufacturerById_get_Ok() {
        Manufacturer expected = manufacturers.get(MANUFACTURER_LIST_INDEX);
        Manufacturer actual = manufacturerService
                .get(manufacturers.get(MANUFACTURER_LIST_INDEX).getId());
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DataProcessingException.class)
    public void getManufacturerById_get_Not_Ok() {
        manufacturerService.get(MANUFACTURER_INCORRECT_ID);
    }

    @Test
    public void getAllManufacturers_getAll_Ok() {
        List<Manufacturer> expected = manufacturers;
        List<Manufacturer> actual = manufacturerService.getAll();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllManufacturers_getAll_NotOk() {
        List<Manufacturer> expected = manufacturers;
        deleteManufacturer(manufacturers.get(MANUFACTURER_LIST_INDEX).getId());
        List<Manufacturer> actual = manufacturerService.getAll();
        Assert.assertNotEquals(expected, actual);
    }

    @After
    public void tearDown() {
        clearManufacturersTable();
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

    private static void addManufacturer(Manufacturer manufacturer) {
        String query = "INSERT INTO manufacturers (name, country) VALUES (?,?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setUpdate(preparedStatement, manufacturer).executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                manufacturer.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            logger.error("Couldn't create manufacturer. " + manufacturer, e);
            throw new DataProcessingException("Couldn't create manufacturer. " + manufacturer, e);
        }
    }

    private static Optional<Manufacturer> getManufacturer(Long id) {
        String query = "SELECT * FROM manufacturers WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Manufacturer manufacturer = null;
            if (resultSet.next()) {
                manufacturer = setManufacturer(resultSet);
            }
            return Optional.ofNullable(manufacturer);
        } catch (SQLException e) {
            logger.error("Couldn't get manufacturer by id " + id, e);
            throw new DataProcessingException("Couldn't get manufacturer by id " + id, e);
        }
    }

    private static PreparedStatement setUpdate(PreparedStatement statement,
                                               Manufacturer manufacturer) throws SQLException {
        statement.setString(1, manufacturer.getName());
        statement.setString(2, manufacturer.getCountry());
        return statement;
    }

    private static Manufacturer setManufacturer(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        String country = resultSet.getString("country");
        Manufacturer manufacturer = new Manufacturer(name, country);
        manufacturer.setId(id);
        return manufacturer;
    }

    private static void deleteManufacturer(Long id) {
        String query = "UPDATE manufacturers SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Couldn't delete a manufacturer by id " + id, e);
            throw new DataProcessingException("Couldn't delete a manufacturer by id " + id, e);
        }
    }
}