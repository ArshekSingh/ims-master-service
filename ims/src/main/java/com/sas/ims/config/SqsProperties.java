package com.sas.ims.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class SqsProperties {
    @Value("${aws.sqs.requirement.url}")
    private String requirementUrl;

    @Value("${aws.sqs.requirementUpdate.url}")
    private String requirementUpdateUrl;
}
