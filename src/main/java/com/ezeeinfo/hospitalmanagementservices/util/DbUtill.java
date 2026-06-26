
package com.ezeeinfo.hospitalmanagementservices.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.Data;

@Data
public class DbUtill {
	private static DataSource dataSource;

	private DbUtill() {

	}

	public static DataSource getDataSource() {
		if (dataSource == null) {
			synchronized (DbUtill.class) {
				if (dataSource == null) {

					DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
					driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
					driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/hospital_management_services_db");
					driverManagerDataSource.setUsername("root");
					driverManagerDataSource.setPassword("root");
					dataSource = driverManagerDataSource;
				}
			}
		}
		return dataSource;

	}

	public static Connection getConnection() throws SQLException {

		return getDataSource().getConnection();
	}
}
