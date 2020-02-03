package com.orpak.fho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan({"com.gvr.datahub.watch"})
public class FhoCsvConvertorApp {
    public static void main(String[] args) {
        SpringApplication.run(com.orpak.fho.FhoCsvConvertorApp.class, args);
    }
}
