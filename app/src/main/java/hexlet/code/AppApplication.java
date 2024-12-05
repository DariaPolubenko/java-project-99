package hexlet.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import io.sentry.spring.jakarta.EnableSentry;

//@EnableSentry(dsn = "https://9b8f2f5dbcbc45075cb07dd618f7024a@o4508415448383488.ingest.de.sentry.io/4508415461163088")
@SpringBootApplication
@EnableJpaAuditing
public class AppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}
