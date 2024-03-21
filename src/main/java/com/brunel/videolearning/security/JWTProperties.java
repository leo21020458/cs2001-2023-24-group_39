package com.brunel.videolearning.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

// This annotation is from Spring Boot, indicating that when the project starts, it will be instanced and  this object will be managed by spring boot.
@Component
//The meaning of this annotation is: to read the content of the configuration file.
@PropertySource("classpath:application.properties")
// This annotation means: combine the name of each property inside this class, use the prefix "jwt.token" to concatenate and then look for the corresponding property value in the configuration file.
@ConfigurationProperties(prefix = "jwt.token")
public class JWTProperties {
    Logger logger = LoggerFactory.getLogger(JWTProperties.class);
    public static long expirationtime;
    public static String signingkey;

    public long getExpirationtime() {
        return expirationtime;
    }

    public void setExpirationtime(long expirationtime) {
        logger.info("Jwt token Expirationtime is set to "+expirationtime);
        JWTProperties.expirationtime = expirationtime;
    }

    public String getSigningkey() {
        return signingkey;
    }

    public void setSigningkey(String signingkey) {
        logger.info("Jwt token Signingkey is set to "+signingkey);
        JWTProperties.signingkey = signingkey;
    }
}
