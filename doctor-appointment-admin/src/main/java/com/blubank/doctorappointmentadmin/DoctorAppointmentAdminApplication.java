package com.blubank.doctorappointmentadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class DoctorAppointmentAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorAppointmentAdminApplication.class, args);
	}

}
