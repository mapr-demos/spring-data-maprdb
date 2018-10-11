package com.mapr.springframework.data.maprdb.unit;

import com.mapr.springframework.data.maprdb.core.MapRTemplate;
import com.mapr.springframework.data.maprdb.model.User;
import com.mapr.springframework.data.maprdb.model.UserWithCustomTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapRTemplateUnitTests {
    public final static String DB_NAME = "test";

    public Connection connection;

    public MapRTemplate operations;

    @Before
    public void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor c = MapRTemplate.class.getDeclaredConstructor(String.class, org.ojai.store.Connection.class,
                java.sql.Connection.class);
        c.setAccessible(true);

        connection = getConnectionMock();

        operations = (MapRTemplate) c.newInstance(DB_NAME, connection, null);
    }

    @Test
    public void getPathTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = operations.getClass().getDeclaredMethod("getPath", String.class);
        method.setAccessible(true);

        String name = method.invoke(operations, "/user").toString();
        String expectedName = "/test/user";

        Assert.assertEquals(expectedName, name);
    }

    @Test
    public void getTablePathTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = operations.getClass().getDeclaredMethod("getTablePath", Class.class);
        method.setAccessible(true);

        String name = method.invoke(operations, User.class).toString();
        String expectedName = "/user";

        Assert.assertEquals(expectedName, name);
    }

    @Test
    public void getTablePathWithCustomPath() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = operations.getClass().getDeclaredMethod("getTablePath", Class.class);
        method.setAccessible(true);

        String name = method.invoke(operations, UserWithCustomTable.class).toString();
        String expectedName = "/user2";

        Assert.assertEquals(expectedName, name);
    }

    @Test
    public void getStoreTest() {
        DocumentStore store = operations.getStore(User.class);

        Assert.assertNotNull(store);
    }

    public Connection getConnectionMock() {
        Connection connection = mock(Connection.class);

        when(connection.getStore(ArgumentMatchers.anyString())).thenReturn(getDocumentStoreMock());

        return connection;
    }

    public DocumentStore getDocumentStoreMock() {
        DocumentStore store = mock(DocumentStore.class);
        return store;
    }

}
