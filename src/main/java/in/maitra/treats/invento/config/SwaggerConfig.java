package in.maitra.treats.invento.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(OpenAPI.class)
public class SwaggerConfig {

	@Bean
	public OpenAPI createSwaggerApi() {
		return new OpenAPI()
				          .info(new Info()
				          .title("Invento Application")
				          .version("1.0")
				          .description("Application for inventory management"));
	}
}