package goodspace.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absoluteImagesPath = Paths.get("images")
                .toAbsolutePath()
                .toUri()
                .toString();

        registry.addResourceHandler("/images/**")
                .addResourceLocations(absoluteImagesPath);

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
