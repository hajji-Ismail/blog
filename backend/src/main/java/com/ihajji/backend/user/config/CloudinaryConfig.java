package com.ihajji.backend.user.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfig {
    
    // Spring automatically maps the YAML keys (e.g., 'cloud-name') to the @Value properties.
    @Value("dusotzx98")
    private String cloudName;

    @Value("185134715417857")
    private String apiKey;

    @Value("bZ6bKFHehcWGLXGUh88O3tU3EJg")
    private String apiSecret;

    /**
     * Creates and configures the Cloudinary object as a Spring Bean.
     * This bean can then be injected into any service class.
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true // Recommended: Forces HTTPS URLs
        ));
    }
}