package com.sstohnij.stacktraceqabackendv0.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =  @Info (
                description = "OpenApi documentation for Stack Trace api",
                title = "Stack Trace",
                version =  "0.0.1"
        ),
        servers =  {
                @Server(
                    description = "Local environment",
                    url = "http://localhost:8080"
                )
        }
)
@SecurityScheme(
        name = "Bearer Token",
        description = "JWT auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

