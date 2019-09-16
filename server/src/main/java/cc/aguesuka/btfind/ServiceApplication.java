package cc.aguesuka.btfind;

import cc.aguesuka.btfind.connection.Bootstrap;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * @author yangmingyuxing
 */
@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(ServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext run = app.run(args);
        Bootstrap bootstrap = run.getBeanFactory().getBean(Bootstrap.class);
        bootstrap.start();
    }

}

