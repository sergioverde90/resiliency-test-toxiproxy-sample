package ms1.com.sergio.sample;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

@Controller("/hello")
public class RestController {

    @Inject
    SaluteClient saluteClient;

    @Inject
    DataSource dataSource;

    @Get
    public String getSalute() throws SQLException {
        // simple operative:
        // 1º obtain resource from a third party system
        // 2º insert data into database
        // 3º publish to an event bus
        dataSource.getConnection();
        return saluteClient.salute();
    }

}
