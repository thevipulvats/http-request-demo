package com.engineeringdigest.http_request_demo.repository;

import com.engineeringdigest.http_request_demo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User saveUser(User user) {
        String sql = "INSERT INTO users (name, age) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            return ps;
        }), keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setAge(rs.getInt("age"));
        return user;
    };

    public List<User> findAll() {
        String sql = "Select id, name, age FROM users";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findById(int id) {
        String sql = "Select id, name, age from users where id = ?";
        List<User> users = jdbcTemplate.query(sql, rowMapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    public void update(User user) {
        String sql = "update users set name = ?, age = ? where id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getAge(), user.getId());

    }

    public void delete(int id) {
        String sql = "delete from users where id = ?";
        jdbcTemplate.update(sql, id);
    }

}
