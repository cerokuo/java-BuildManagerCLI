package com.knowroaming.store;

import com.knowroaming.store.Exceptions.DataBaseConnectionException;
import com.knowroaming.model.UserDTO;
import com.knowroaming.model.UserDataHistoryDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cerokuo on 02/04/2017.
 */
public class UserStoreManagerImpl implements UserStoreManager {

    private static final Logger LOGGER = Logger.getLogger( UserStoreManagerImpl.class.getName() );
    private static final String COUNT_ROW = "count(*)";

    private String ddbb_url = "jdbc:sqlite:knowRoamingDDBB.db";

    public String addUser(UserDTO user) {
        Connection conn = null;
        Statement stmt = null;
        try {
            String userID = generateRandomID();
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery = String.format("INSERT INTO USERS (ID, NAME, EMAIL, PHONE, COUNTRY) VALUES ('%s', '%s', '%s', '%s', '%s');",
                    userID,
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getCountry());
            stmt.executeUpdate(sqlQuery);
            user.setId(userID);
            return userID;
        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at AddUser");
            throw new DataBaseConnectionException("Error trying to connect to DDBB", e);
        } finally {
            closeConnection(conn, stmt);
        }
    }

    public void addService(UserDataHistoryDTO userDataHistory) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery = String.format("INSERT INTO USER_HISTORIES (USERID, USAGEID, DATE) VALUES ('%s', '%s', '%s');",
                    userDataHistory.getUserID(),
                    getUsageID(userDataHistory.getDataTypeID()),
                    userDataHistory.getStarDate());
            stmt.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at addService");
            throw new DataBaseConnectionException("Error trying to connect to DDBB", e);

        } finally {
            closeConnection(conn, stmt);
        }
    }

    public List<UserDataHistoryDTO> checkHistory(UserDataHistoryDTO userDataHistory) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<UserDataHistoryDTO> userList = new ArrayList<UserDataHistoryDTO>();
        try {
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery;
            if (userDataHistory.getDataTypeID().equals("ALL")) {

                sqlQuery = String.format("SELECT * FROM USER_HISTORIES WHERE " +
                                "USERID = '%s' AND " +
                                "DATE BETWEEN strftime('%s') AND strftime('%s')",
                        userDataHistory.getUserID(),
                        userDataHistory.getStarDate(),
                        userDataHistory.getEndDate());
            } else {

                sqlQuery = String.format("SELECT * FROM USER_HISTORIES WHERE " +
                                "USERID = '%s' AND " +
                                "USAGEID = '%s' AND "+
                                "DATE BETWEEN strftime('%s') AND strftime('%s')",
                        userDataHistory.getUserID(),
                        getUsageID(userDataHistory.getDataTypeID()),
                        userDataHistory.getStarDate(),
                        userDataHistory.getEndDate());
            }
            rs = stmt.executeQuery(sqlQuery);
            while(rs.next()) {
                UserDataHistoryDTO udh = new UserDataHistoryDTO();
                udh.setUserID(rs.getString("USERID"));
                udh.setDataTypeID(rs.getString("USAGEID"));
                udh.setStarDate(rs.getString("DATE"));
                userList.add(udh);
            }
            return userList;
        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at checkHistory");
            throw new DataBaseConnectionException("Error trying to connect to DDBB",e);

        } finally {
            closeConnection(conn, stmt, rs);
        }
    }

    private String generateRandomID() {
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
    }

    private String getUsageID(String dataTypeID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery = String.format("SELECT ID FROM SERVICE_USAGES WHERE NAME = '%s'", dataTypeID);
            rs = stmt.executeQuery(sqlQuery);
            return rs.getString("ID");

        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at getUsageID");
            throw new DataBaseConnectionException("Error trying to connect to DDBB", e);

        } finally {
            closeConnection(conn, stmt, rs);
        }
    }

    public boolean searchUser(String userID) {
        Statement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery = String.format("SELECT count(*) FROM USERS WHERE ID = '%s'", userID);
            rs = stmt.executeQuery(sqlQuery);
            if (rs.getInt(COUNT_ROW) == 0) {
                System.out.println("No user found with this ID in DDBB");
                return false;
            }
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at searchUser");
            throw new DataBaseConnectionException("Error trying to connect to DDBB", e);

        } finally {
            closeConnection(conn, stmt, rs);
        }

    }

    public boolean checkType(String typeOfUsage) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery = String.format("SELECT count(*) FROM SERVICE_USAGES WHERE NAME = '%s'", typeOfUsage);
            rs = stmt.executeQuery(sqlQuery);
            if (rs.getInt(COUNT_ROW) == 0) {
                System.out.println("No service with this name in DDBB");
                return false;
            }
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at checkType");
            throw new DataBaseConnectionException("Error trying to connect to DDBB", e);

        } finally {
            closeConnection(conn, stmt, rs);
        }
    }

    public boolean searchEmail(String email) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(ddbb_url);
            stmt = conn.createStatement();
            LOGGER.log(Level.ALL,"Connection to SQLite has been established.");
            String sqlQuery = String.format("SELECT count(*) FROM USERS WHERE EMAIL = '%s'", email);
            rs = stmt.executeQuery(sqlQuery);
            if (rs.getInt(COUNT_ROW) == 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.ALL,"Error trying to connect to DDBB at searchEmail");
            throw new DataBaseConnectionException("Error trying to connect to DDBB", e);

        } finally {
            closeConnection(conn, stmt, rs);
        }
    }

    private void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception e) {System.err.println(e.getMessage());};
        try { if (stmt != null) stmt.close(); } catch (Exception e) {System.err.println(e.getMessage());};
        try { if (conn != null) conn.close(); } catch (Exception e) {System.err.println(e.getMessage());};
    }

    private void closeConnection(Connection conn, Statement stmt) {
        try { if (stmt != null) stmt.close(); } catch (Exception e) {System.err.println(e.getMessage());};
        try { if (conn != null) conn.close(); } catch (Exception e) {System.err.println(e.getMessage());};
    }

    /**
     * Set to allow the test to check with another ddbb.
     * @param ddbb_url
     */
    public void setDdbb_url(String ddbb_url) {
        this.ddbb_url = ddbb_url;
    }
}
