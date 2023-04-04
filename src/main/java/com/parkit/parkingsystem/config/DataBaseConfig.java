package com.parkit.parkingsystem.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.configuration.PropertiesConfiguration;
import java.io.IOException;
import java.sql.*;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() throws ClassNotFoundException, SQLException, IOException, ConfigurationException {
        logger.info("Create DB connection");

        PropertiesConfiguration prop = new PropertiesConfiguration();
        prop.load("database.properties");

        String driver = prop.getString("driver");
        String url = prop.getString("url");
        String login = prop.getString("login");
        String password = prop.getString("password");

        Class.forName(driver);
        return DriverManager.getConnection(url,login, password);



    }


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
