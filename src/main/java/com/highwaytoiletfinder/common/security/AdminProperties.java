package com.highwaytoiletfinder.common.security;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "app.admin")
public class AdminProperties {
    private String emails;

    public List<String> getEmails() {
        return Arrays.asList(emails.split(","));
    }

}
