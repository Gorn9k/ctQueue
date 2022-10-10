package VSTU.ctQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class Runner {
    /**
     * ����� ����� � ����������.
     * 
     * @param args ��������� ��������� ������
     */
    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    /**
     * ����������� {@link BCryptPasswordEncoder} ������ {@link ApplicationContext}
     * 
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
