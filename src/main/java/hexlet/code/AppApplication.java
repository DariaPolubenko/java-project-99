package hexlet.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import io.sentry.spring.jakarta.EnableSentry;

//@EnableSentry(dsn = "https://9b8f2f5dbcbc45075cb07dd618f7024a@o4508415448383488.ingest.de.sentry.io/4508415461163088")
@SpringBootApplication
@EnableJpaAuditing
public class AppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	/*
	 Дорогой наставник, мне не описать всю свою боль. Этот проект выпотрошил меня и завставил сомневаться во всей своей жизни!
	 Спасибо большое тебе за помощь и проверку проверку. Уверена, что тут будут ошибки, готова их услышать и исправить!

	 /иногда мне нужно поныть, прости пожалуйста, что тебе приходится это читать/
	 */
}
