package com.softserve.academy.event.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:email.properties")
@ComponentScan(basePackages = {"com.softserve.academy.event.service"})
public class EmailConfig {

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private Integer port;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${mail.transport.protocol}")
    private String mailTransportProtocol;

    @Value("${mail.smtp.auth}")
    private String maiSmtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Value("${mail.smtp.socketFactory.port}")
    private String mailSmtpSocketFactoryPort;

    @Value("${mail.smtp.socketFactory.class}")
    private String mailSmtpSocketFactoryClass;

    @Value("${mail.smtp.socketFactory.fallback}")
    private String mailSmtpSocketFactoryFallback;

    @Value("${mail.debug}")
    private String mailDebug;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations(
                new ClassPathResource("email.properties")
        );
        configurer.setIgnoreResourceNotFound(true);
        return configurer;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();

        properties.setProperty("mail.transport.protocol", mailTransportProtocol);
        properties.setProperty("mail.smtp.auth", maiSmtpAuth);
        properties.setProperty("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        properties.setProperty("mail.smtp.socketFactory.port", mailSmtpSocketFactoryPort);
        properties.setProperty("mail.smtp.socketFactory.class", mailSmtpSocketFactoryClass);
        properties.setProperty("mail.smtp.socketFactory.fallback", mailSmtpSocketFactoryFallback);
        properties.setProperty("mail.debug", mailDebug);

        return properties;
    }

    @Bean
    public SimpleMailMessage emailTemplate() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("somebody@gmail.com");
        message.setFrom("admin@gmail.com");
        message.setText("FATAL - Application crash. Save your job !!");
        return message;
    }


}
