import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import util.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.times;


public class DAOTest {

    @Mock
    private DAO dao;
    @Mock private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTableTest() throws SQLException {
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        dao.createTable();

        Mockito.verify(mockConnection, times(1));
    }

    @Test
    public void insertEventTest() {

    }

    @Test
    public void selectAllTest() {

    }

    @Test
    public void closeTest() {

    }
}
