package com.natura.web.server;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_23;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlTests {

    String url = "jdbc:mysql://localhost:3060/db_natura?useSSL=false&serverTimezone=UTC";

    String user = "naturauser";

    String password = "natura";

    @Test
    void testSqlConnection() throws Exception {

        MysqldConfig config = aMysqldConfig(v5_6_23)
                .withCharset(UTF8)
                .withPort(3060)
                .withUser(user, password)
                .withTimeZone("Europe/Amsterdam")
                .build();

        EmbeddedMysql mysqld = anEmbeddedMysql(config)
                .addSchema("db_natura")
                .start();

        // Class.forName( "com.mysql.jdbc.Driver" ); // do this in init
        // // edit the jdbc url
        Connection conn = DriverManager.getConnection(url + "&user=" + user + "&password=" + password);
        // Statement st = conn.createStatement();
        // ResultSet rs = st.executeQuery( "select * from table" );

        assertNotNull(conn);
        System.out.println("Connected");
    }
}