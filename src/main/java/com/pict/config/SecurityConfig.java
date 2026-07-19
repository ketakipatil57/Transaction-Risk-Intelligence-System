package com.pict.config;

// When spring application starts spring reads the config file to get an idea about what
// fields are to me stored in the container, so that they can be easily used further
import com.pict.Security.CustomUserDetailsService;
import com.pict.Security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtFilter jwtFilter){
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    //Tells how to authenticate the user
    public AuthenticationProvider authenticationProvider(){

        // we make an object of
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // we retrieve the respective user from the DB
        authProvider.setUserDetailsService(customUserDetailsService);
        // passwordEncoder ko call lagake password match karva leta hain
        authProvider.setPasswordEncoder(passwordEncoder());

        // gives the authentication Provider object to spring and it stores it into the cotainer
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        // tells authentication manager to do the next part
         return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // this is used to disable the csrf sessions as we are using the JWT token for authentication
        http.csrf(csrf -> csrf.disable());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // this is used to allow user to login / register without any JWT token only these requests will be allowed(.anyRequest().authenticated() -> due to this all other requests require to authenticated)
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/register", "/auth/login").permitAll().anyRequest().authenticated());
        return http.build();
    }
}
