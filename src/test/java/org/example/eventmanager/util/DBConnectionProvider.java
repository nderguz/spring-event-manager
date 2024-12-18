package org.example.eventmanager.util;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;

@AllArgsConstructor
public class DBConnectionProvider {

    private final String url;
    private final String username;
    private final String password;

    Connection getConnection(){
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
