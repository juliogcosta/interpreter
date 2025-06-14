package com.yc.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yc.persistence.api.UnsecuredServiceController;
import com.yc.persistence.api.UnsecuredTransactionController;
import com.yc.pr.sec.AccessTokenFilter;
import com.yc.pr.sec.JwtAccessDeniedHandler;
import com.yc.pr.sec.JwtAuthenticationEntryPoint;
import com.yc.pr.sec.JwtUtils;
import com.yc.utils.ContextManagerControl;

import feign.Contract;
import jakarta.annotation.PreDestroy;

@EnableRabbit
@EnableWebSecurity
@EnableFeignClients
@ComponentScan(basePackages = { "com.yc.persistence", "com.loco3.persistence", "com.yc.utils.bean", "com.yc.error", "com.yc.collector" })
@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class PersistenceApplication implements CommandLineRunner
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${yc.api.management.server.log.control.display}")
    public Boolean logDisplay;

    @Value("${yc.api.management.server.log.control.data.persistence.ttl}")
    public Long ttl;

    @Value("${yc.app.name}")
    private String appName;

    @Value("${yc.eh.exchange.schema-interruption.name}")
    private String schemaInterruptionExchangeName;

    @Value("${yc.eh.exchange.schema-interruption.isDurable}")
    private Boolean isDurableSchemaInterruptionExchange = false;

    @Value("${yc.eh.exchange.schema-interruption.isAutodelete}")
    private Boolean isAutodeleteSchemaInterruptionExchange = false;

    private AbstractMessageListenerContainer container;

    public static Boolean nosql = false;

    /*@Autowired
    private LocalCacheImpl schemaCache;*/

    @Autowired
    private ContextManagerControl contextManagerControl;

    /*private ErrorHandlerImpl errorHandler = new ErrorHandlerImpl();

    @Autowired
    private GenericWebApplicationContext ctx;*/

    @Value("${yc.security.keys.accessToken.secret}") 
    private String accessTokenSecret;

    @Value("${yc.security.keys.accessToken.expirationMinutes}") 
    private int accessTokenExpirationMinutes;

    @Value("${yc.security.keys.refreshToken.secret}") 
    private String refreshTokenSecret;

    @Value("${yc.security.keys.refreshToken.expirationDays}") 
    private int refreshTokenExpirationDays;

    @Bean
    Contract feignContract()
    {
        return new feign.Contract.Default();
    }

    public static void main(String[] args)
    {
        SpringApplication.run(PersistenceApplication.class, args);
    }

    @Bean
    JwtUtils jwtUtils() 
    {
        return new JwtUtils(
                this.accessTokenSecret, 
                this.accessTokenExpirationMinutes, 
                this.refreshTokenSecret, 
                this.refreshTokenExpirationDays);
    }

    @Bean
    AccessTokenFilter accessTokenFilter() 
    {
        return new AccessTokenFilter();
    }

    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() 
    {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    JwtAccessDeniedHandler jwtAccessDeniedHandler() 
    {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(this.jwtAuthenticationEntryPoint())
                        .accessDeniedHandler(this.jwtAccessDeniedHandler()))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        /*
                         * Sobre os endpoints de monitoramento
                         * 
                         */
                        .requestMatchers("/actuator/**").hasRole("YC_API_ACTUATOR")
                        .requestMatchers(UnsecuredServiceController.urlPrefix.concat("/**")).permitAll()

                        .requestMatchers(UnsecuredTransactionController.urlPrefix.concat("/s/no-ac")).permitAll()

                        .requestMatchers("/open/**").permitAll()
                        .requestMatchers("/s/no-ac/**").permitAll()
                        .requestMatchers("/m/no-ac/**").permitAll()

                        /*
                         * Authenticated endpoints
                         * 
                         */
                        .requestMatchers("/s/ac/**").authenticated()
                        .requestMatchers("/m/ac/**").authenticated()
                        .requestMatchers(UnsecuredTransactionController.urlPrefix.concat("/s/ac")).authenticated()

                        /*
                         * Sobre Endpoints sem Segurança
                         * 
                         */
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        .anyRequest().authenticated());

        http.addFilterBefore(this.accessTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    WebMvcConfigurer corsConfigurer() 
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) 
            {
                registry.addMapping("/**")
                    .allowCredentials(true)
                    .allowedOriginPatterns("*")
                    .allowedHeaders(
                            HttpHeaders.ORIGIN,
                            HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                            HttpHeaders.AUTHORIZATION,
                            HttpHeaders.CONTENT_TYPE,
                            HttpHeaders.ACCEPT)
                    .allowedMethods(
                            HttpMethod.POST.name(), 
                            HttpMethod.GET.name(), 
                            HttpMethod.PUT.name(), 
                            HttpMethod.DELETE.name(), 
                            HttpMethod.OPTIONS.name());
            }
        };
    }

    @Override
    public void run(String... args) throws Exception
    {
        try
        {
            /*
            LocalSchemaServiceCache.getInstance().setSQLPoolConnRESTClient(this.sqlPoolConnRESTClient);
            LocalSchemaServiceCache.getInstance().setNoSQLPoolConnRESTClient(this.noSQLDriverRESTClient);
            LocalSchemaServiceCache.getInstance().setModelerRESTClient(this.forgerRESTClient);
             */

            /*
             * Aqui se espera que o microservice forger tenha criado a exchange
             * para essa essa nova fila ser associada.
             * 
             * A notificação que chega a esse consumidor implicará a remoção do 
             * schema de um tenant para o serviço de persistência deixe de funcionar
             * temporariamente para tal tenant.
             * 
             */
            /*this.container = new QueueReceiver().declareReceiver(this.ctx,
                this.appName, 
                this.schemaInterruptionExchangeName, 
                this.isDurableSchemaInterruptionExchange, 
                this.isAutodeleteSchemaInterruptionExchange, message ->
                {
                    JSONObject jsObject = null;
                    try
                    {
                        jsObject = new JSONObject(new String(message.getBody()));
                        logger.info(" >>> message.body: "+jsObject.toString(2));

                        logger.info(" >>> !contextManagerControl.containsKey("+jsObject.getString("tenantId")+"): "+(!this.errorControl.containsKey(jsObject.getString("tenantId"))));
                        if (!PersistenceApplication.this.errorControl.containsKey(jsObject.getString("tenantId")))
                        {
                            PersistenceApplication.this.errorControl.initKey(jsObject.getString("tenantId"), PersistenceApplication.this.ttl, PersistenceApplication.this.logDisplay);
                            PersistenceApplication.this.errorControl.setLogConsole(jsObject.getString("tenantId"), PersistenceApplication.this.logDisplay);
                        }

                        logger.info(" >>> contextManagerControl.getLogConsole("+jsObject.getString("tenantId")+"): "+this.errorControl.containsKey(jsObject.getString("tenantId")));
                        if (PersistenceApplication.this.errorControl.getLogConsole(jsObject.getString("tenantId"))) 
                        {
                            logger.info("to drop schema "+jsObject.toString()+" from cache.");
                        }

                        logger.info(" >>> schemaCache.containsKey("+jsObject.getString("tenantId")+"): "+this.schemaCache.containsKey(jsObject.getString("tenantId")));
                        if (PersistenceApplication.this.schemaCache.containsKey(jsObject.getString("tenantId")))
                        {
                            PersistenceApplication.this.schemaCache.remove(jsObject.getString("tenantId"));
                            logger.info(" >>> >>> schemaCache for "+jsObject.getString("tenantId")+" was removed!");
                        }
                        else
                        {
                            logger.info(" >>> schemaCache no contains tenantId '".concat(jsObject.getString("tenantId")));
                            //throw new Exception("510:schemaCache no contains tenantId '".concat(jsObject.getString("tenantId")));
                        }
                    }
                    catch (Exception e)
                    {
                        JSONObject jsError = new JSONObject(this.errorHandler.buildResponseEntityError(e, 
                                jsObject.getString("tenantId"), 
                                null, 
                                null, 
                                null, 
                                null, 
                                null, 
                                null, 
                                this.appName, 
                                "/queue-up-error", 
                                "QUEUE", 
                                null, 
                                null, 
                                false, 
                                false));
                        logger.info(" >>> Exception: "+jsError.toString(2));
                    }
                });*/

            Boolean saveLog = false;

            this.contextManagerControl.initKey("yc.persistence-c", this.ttl, false);
            this.contextManagerControl.setLogConsole("yc.persistence-c", this.logDisplay);
            this.contextManagerControl.setSaveLog("yc.persistence-c", saveLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            System.exit(-1);
        }
    }

    @PreDestroy
    public void onExit()
    {
        this.container.stop();
        this.container.destroy();;
    }
}
