package hotelreservationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HotelreservationmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelreservationmanagementApplication.class, args);
	}

}
