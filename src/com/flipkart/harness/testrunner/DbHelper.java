package com.flipkart.harness.testrunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

import com.mysql.jdbc.PreparedStatement;

public class DbHelper {

    Connection conn;

    public DbHelper() {


    }

    public void addResults(Test test, UUID batchId, Boolean persist) {

        if(persist){

            Config config = new Config();
            config.loadConfigFile();

            String dburl = "jdbc:mysql://" + config.configProperties.getProperty("db.hostname") + ":" + config.configProperties.getProperty("db.port") + "/" + config.configProperties.getProperty("db.dbname") + "?autoReconnect=true";

            String username = config.configProperties.getProperty("db.user");
            String password = config.configProperties.getProperty("db.password");

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(dburl, username, password);


            } catch (Exception e) {
                System.out.println("Could not connect to database.");
                e.printStackTrace();

            }
            try {

                String sql = "insert into harness (testName,passed,failed,exceptions,batchId,type,createdTime,module,feature,subfeature) values ('" + test.getName() + "','" + test.getPassed() + "','" + test.getFailed() + "','" + test.getException() + "','" + batchId + "','" + test.getType() + "','" + getCurrentTime() + "','" + test.getModule() + "','" + test.getFeature() + "','" + test.getSubfeature() + "')";
                PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
                ps.execute(sql);
                ps.close();

            } catch (Exception e) {
            e.printStackTrace();
        }

        try {

        } catch (Exception exp) {
            exp.printStackTrace();

        }
       }
    }

    public String getCurrentTime() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        return currentTime;
    }


}
