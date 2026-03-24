package com.example.wealth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class WealthApplication

fun main(args: Array<String>) {
    SpringApplication.run(WealthApplication::class.java, *args)
}