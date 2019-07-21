package com.urchin.release.mgt;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class UrchinReleaseMgtApplication {

	public static void main(String[] args) throws SQLException {
		Server.createTcpServer("-ifNotExists").start();

		SpringApplication.run(UrchinReleaseMgtApplication.class, args);
	}

}
