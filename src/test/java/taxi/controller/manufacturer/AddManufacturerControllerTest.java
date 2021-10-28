package taxi.controller.manufacturer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.util.ConnectionUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddManufacturerControllerTest {
    private static final String REQUEST_DISPATCHER_PATH = "/WEB-INF/views/manufacturers/add.jsp";
    private static final String ADD_MANUFACTURERS_PATH = "/manufacturers/add";
    private static RequestDispatcher dispatcher;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static AddManufacturerController addManufacturerController;

    @BeforeClass
    public static void setBeforeAll(){
        addManufacturerController = new AddManufacturerController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void processGetRequest_doGet_OK() throws ServletException, IOException {
        when(request.getRequestDispatcher(REQUEST_DISPATCHER_PATH)).thenReturn(dispatcher);
        addManufacturerController.doGet(request, response);
        verify(request, times(1)).getRequestDispatcher(REQUEST_DISPATCHER_PATH);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void processPostRequest_doPost_OK() throws IOException {
        when(request.getParameter("name")).thenReturn("BMW");
        when(request.getParameter("country")).thenReturn("Germany");
        addManufacturerController.doPost(request, response);
        verify(response).sendRedirect(ADD_MANUFACTURERS_PATH);
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
