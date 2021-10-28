package taxi.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import taxi.exception.AuthenticationException;
import taxi.exception.DataProcessingException;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.AuthenticationService;
import taxi.util.ConnectionUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuthenticationServiceImplTest {
    private static final Injector injector = Injector.getInstance("taxi");
    private static Driver admin;
    private static AuthenticationService authenticationService;

    @BeforeClass
    public static void setBeforeAll() {
        authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
        admin = new Driver("Admin", "ADM4395739");
        admin.setLogin("admin");
        admin.setPassword("qwerty");
    }

    @Before
    public void setUp() {
        createDriver(admin);
    }

    @Test
    public void processLogin_login_Ok() throws AuthenticationException {
        Driver actual = authenticationService
                .login(admin.getLogin(), admin.getPassword());
        Assert.assertEquals(admin, actual);
    }

    @Test(expected = AuthenticationException.class)
    public void processLogin_login_Not_Ok() throws AuthenticationException {
        authenticationService.login("wrong", "wrong");
    }

    @After
    public void tearDown() throws SQLException {
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
}
