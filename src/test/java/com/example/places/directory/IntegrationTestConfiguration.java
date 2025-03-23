package com.example.places.directory;

import com.example.places.directory.controller.PlacesController;
import com.example.places.directory.converter.AddressConverter;
import com.example.places.directory.exception.RestExceptionHandler;
import com.example.places.directory.persistence.repostitory.AddressRepository;
import com.example.places.directory.service.AddressService;
import com.example.places.directory.service.impl.AddressServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    AddressConverter.class,
    AddressService.class,
    AddressServiceImpl.class,
    AddressRepository.class,
    PlacesController.class,
    RestExceptionHandler.class})
public class IntegrationTestConfiguration {

}
