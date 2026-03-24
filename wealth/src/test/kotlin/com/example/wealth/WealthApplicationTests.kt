package com.example.wealth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

import org.junit.jupiter.api.Assertions.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WealthApplicationTests {
    
    @LocalServerPort
    private lateinit var port: Integer
    
    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    
    @Test
    fun `context loads`() {
        // 验证应用程序上下文能够正常加载
        assertTrue(true) // 如果上下文加载失败，测试会自动失败
    }
    
    @Test
    fun `api root returns 200`() {
        // 验证API根端点是否正常工作
        val response = restTemplate.getForEntity("http://localhost:\$port/api", String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }
}