package hiyen.onboarding.common;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseCleaner(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void clear() {
        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'";
        List<String> tableNames = jdbcTemplate.queryForList(query, String.class);

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String tableName : tableNames) {
            if (tableName.equalsIgnoreCase("authority")) continue;
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY ");
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
