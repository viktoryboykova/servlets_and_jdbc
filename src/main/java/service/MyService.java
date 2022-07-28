package service;

import model.Cat;
import store.JdbcStore;

import java.util.Collection;

public class MyService {

    private JdbcStore jdbcStore = JdbcStore.instOf();

    public void save(Cat cat) {
        jdbcStore.save(cat);
    }

    public Collection<Cat> getAll() {
        return jdbcStore.getAll();
    }
}
