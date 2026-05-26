package co.istad.ite_spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/")
public class ApplicationController {

    @Value("${app.version}")
    private Integer appVersion;

    @Value("${app.message}")
    private  String message;

    @Value("${app.info}")
    private String information;

    @Value("{app.port}")
    private String port;


    @GetMapping("hello")
    public Map<String, Object> hello() {

        log.info("hello");
        log.debug("Message: {}", message);

        return Map.of(
                "status", "Successfully",
                "Version", appVersion,
                "message", message,
                "info", information,
                "Port", port
        );
    }

}
