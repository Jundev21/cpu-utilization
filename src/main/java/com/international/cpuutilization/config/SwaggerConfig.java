package com.international.cpuutilization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

@OpenAPIDefinition(
	servers = {
		@Server(url = "http://localhost:8080", description = "Local Server")
	},
	info = @Info(
		title = "cpu-utilization Service",
		description = "cpu-utilization API",
		contact = @Contact(name = "김준래", url = "https://github.com/Jundev21/cpu-utilization")
	)
)
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.components(new Components());
	}
}
