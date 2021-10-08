package studio.deervision;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class DeerVisionApplication {

	public static void main(String[] args) throws SQLException {
		Server.createTcpServer(
				"-ifNotExists",
				"-tcpAllowOthers",
				"-tcpPort", "9191")
				.start();

		SpringApplication.run(DeerVisionApplication.class, args);
	}

}
