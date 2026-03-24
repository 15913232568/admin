package com.example.wealth.config

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.http.HttpMethod

/**
 * Spring Security 6.x 正确配置（修复 csrfTokenRepository 空指针问题）
 * 核心：仅保留 csrf.disable()，移除错误的 csrfTokenRepository(null)
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 1. 正确禁用CSRF（仅需这一行，无需额外配置）
            .csrf { it.disable() }

            // 2. 禁用Session（JWT无状态认证）
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // 3. 禁用默认认证方式（避免干扰）
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

            // 4. 授权规则（核心：放行规则必须在 anyRequest 之前！）
            .authorizeHttpRequests { auth ->
                // 第一步：明确放行 POST 方法的登录接口（优先级最高）
                auth.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // 可选：放行Swagger/Actuator等公开接口（按需添加）
                auth.requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/health"
                ).permitAll()
                // 第二步：其余所有请求必须认证（必须在放行规则之后！）
                auth.anyRequest().authenticated()
            }

            // 5. 异常处理（返回JSON格式响应）
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint { _, response, authException ->
                    logger.warn("未认证访问: ${authException.message}")
                    response.contentType = "application/json;charset=UTF-8"
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("""{"code":401,"message":"${authException.message ?: "请先登录"}"}""")
                }
                exception.accessDeniedHandler { _, response, accessDeniedException ->
                    logger.warn("权限不足: ${accessDeniedException.message}")
                    response.contentType = "application/json;charset=UTF-8"
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.writer.write("""{"code":403,"message":"${accessDeniedException.message ?: "权限不足"}"}""")
                }
            }

            // 6. 添加JWT过滤器（在用户名密码过滤器之前执行）
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        logger.info("Security配置生效：POST /api/auth/login 已放行，其余接口需Token认证")
        return http.build()
    }

    /**
     * 密码加密器（Spring Security 必须配置）
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * 认证管理器（登录接口手动认证用）
     */
    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager
}