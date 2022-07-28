package store;

import model.Cat;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class JdbcStore {

    private static final JdbcStore INST = new JdbcStore();

    private final BasicDataSource pool = new BasicDataSource();

    private JdbcStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        JdbcStore.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    public static JdbcStore instOf() {
        return INST;
    }

    public void save(Cat cat) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO cats (name, age) VALUES (?, ?)")
        ) {
            ps.setString(1, cat.getName());
            ps.setInt(2, cat.getAge());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<Cat> getAll() {
        List<Cat> cats = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM cats")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cats.add(
                            new Cat(
                                    it.getString("name"),
                                    it.getInt("age")
                            ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cats;
    }
}
