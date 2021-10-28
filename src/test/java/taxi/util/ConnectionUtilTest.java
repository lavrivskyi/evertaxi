package taxi.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConnectionUtilTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/taxi";
    private static Connection connection;

    @Before
    public void setUp() {
        connection = ConnectionUtil.getConnection();
    }

    @Test
    public void getConnection_OK() throws SQLException {
        String actual = connection.getMetaData().getURL();
        Assert.assertEquals(DB_URL, actual);
    }
}