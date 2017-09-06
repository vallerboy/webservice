package pl.oskarpolak.webservice.models.configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecure extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity web) throws Exception {
        web
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{

//        ShaPasswordEncoder encoder  = new ShaPasswordEncoder();
//        String passwordHashed = encoder.encodePassword("jakiesHasloDoZakodowania", null);
//
//        auth.userDetailsService(userAuth).passwordEncoder(encoder);
          auth.inMemoryAuthentication().withUser("oskar").password("oskar1").roles("USER");
    }
}
