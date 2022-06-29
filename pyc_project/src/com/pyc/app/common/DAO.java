package com.pyc.app.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DAO {

	private String driver;
	private String url;
	private String id;
	private String pwd;
	
	protected Connection conn;
	protected PreparedStatement pstmt;
	protected Statement stmt;
	protected ResultSet rs;

	protected DAO() {
		dbconfig();
	}
	
	private void dbconfig() {		
		/*
		String file = "config/db.properties";
		Properties properties = new Properties();
		
		try {
			String filePath = ClassLoader.getSystemClassLoader().getResource(file).getPath();			
			properties.load(new FileInputStream(filePath));
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("DB config Fail!");
		}
		
		driver = properties.getProperty("driver");
		url = properties.getProperty("url");
		id = properties.getProperty("id");
		pwd = properties.getProperty("password");
		*/
		driver = "oracle.jdbc.driver.OracleDriver";
		url = "jdbc:oracle:thin:@localhost:1521:xe";
		id ="hr";
		pwd = "hr";
	}

	protected void connect() {		
		try {			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pwd);
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("DB 연결 실패!");
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("DB 드라이버 로딩 실패!");			
		}		
	}
	
	protected void disconnect() {
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();			
		}catch(SQLException e) {
			e.printStackTrace();
		}		
	}
}
