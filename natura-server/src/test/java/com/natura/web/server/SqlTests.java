package com.natura.web.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlTests {

    //@Value("spring.datasource.url")
    String url = "jdbc:mysql://localhost:3306/db_natura?useSSL=false&serverTimezone=UTC";

    //@Value("spring.datasource.username")
    String user = "naturauser";

    //@Value("spring.datasource.password")
    String password = "natura";

    @Test
    void testSqlConnection() throws Exception {
        // Class.forName( "com.mysql.jdbc.Driver" ); // do this in init
        // // edit the jdbc url
        Connection conn = DriverManager.getConnection(url + "&user=" + user + "&password=" + password);
        // Statement st = conn.createStatement();
        // ResultSet rs = st.executeQuery( "select * from table" );

        assertNotNull(conn);
        System.out.println("Connected");
    }
}