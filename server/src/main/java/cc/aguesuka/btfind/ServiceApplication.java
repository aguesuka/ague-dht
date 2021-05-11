package cc.aguesuka.btfind;

import cc.aguesuka.btfind.connection.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author aguesuka
 */
@SpringBootApplication
public class ServiceApplication {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${dht.databasePath:#{\"database\"}}")
    public String databasePath;

    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(ServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext run = app.run(args);
        Bootstrap bootstrap = run.getBeanFactory().getBean(Bootstrap.class);
        bootstrap.start();
    }


    @Bean
    public DataSource dataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        logger.error("the database is `{}`", Path.of(databasePath).toAbsolutePath().toString());
        dataSource.setUrl("jdbc:sqlite:" + databasePath);
        return dataSource;
    }

}

