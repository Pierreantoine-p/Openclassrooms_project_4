package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.sql.*;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        logger.info("Create DB connection");
/*
        Properties prop = new Properties();
        FileReader reader = new FileReader("database.properties");
        prop.load(reader);
        reader.close();

        String url = prop.getProperty("url");
        String login = prop.getProperty("login");
        String password = prop.getProperty("password");
*/
        Properties prop = new Properties();
       // InputStream input = new FileInputStream("database.properties");
        InputStream input = Files.newInputStream(Paths.get("database.properties"));
        prop.load(input);
        input.close();

        String driver = prop.getProperty("driver");
        String url = prop.getProperty("url");
        String login = prop.getProperty("login");
        String password = prop.getProperty("password");

        Class.forName(driver);
        return DriverManager.getConnection(url,login, password);



    }
     /*
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.get load(database.properties);
        config.getString("url");

        ResourceBundle res = ResourceBundle.getBundle("param");
        String url = res.getString("url");
        String login = res.getString("login");
        String password = res.getString("password");

                 */


    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
