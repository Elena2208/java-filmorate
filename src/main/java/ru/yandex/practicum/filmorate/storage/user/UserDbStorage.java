package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        final String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User create(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        final String sqlQuery = "INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        log.info("Пользователь с id {} отправлен", user.getId());
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        final String checkQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, user.getId());

        if (!userRows.next()) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        final String sqlQuery = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь {} обновлен", user.getId());
        return user;
    }

    @Override
    public User getById(int id) {
        final String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!userRows.next()) {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь не найден");
        }
        final String checkQuery = "select * from users where user_id = ?";
        log.info("Пользователь с id {} отправлен", id);
        return jdbcTemplate.queryForObject(checkQuery, this::makeUser, id);
    }

    @Override
    public User deleteById(int id) {
        User user = getById(id);
        jdbcTemplate.update("delete from likes where user_id=?", id);
        jdbcTemplate.update("delete from  users where user_id=?", id);
        jdbcTemplate.update("delete from friends where user_id=? and friend_id=?", id, id);
        log.info("Пользователь удален");
        return user;
    }

    @Override
    public void addFriend(int firstId, int secondId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users where user_id=?", firstId);
        if (!rs.next()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        rs = jdbcTemplate.queryForRowSet("select * from users where user_id=?", secondId);
        if (!rs.next()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        jdbcTemplate.update("insert into friends(user_id,friend_id) values (?,?)", firstId, secondId);
    }

    @Override
    public void deleteFriend(int firstId, int secondId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users where user_id=?", firstId);
        if (!rs.next()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        rs = jdbcTemplate.queryForRowSet("select * from users where user_id=?", secondId);
        if (!rs.next()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        jdbcTemplate.update("delete from friends where user_id=? and friend_id=?", firstId, secondId);
    }

    @Override
    public List<User> getFriendsListById(int id) {
        String sqlQuery = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::makeUser, id);
    }

    @Override
    public List<User> getCommonFriendsList(int firstId, int secondId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users where user_id=?", firstId);
        if (!rs.next()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        rs = jdbcTemplate.queryForRowSet("select * from users where user_id=?", secondId);
        if (!rs.next()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        String sqlQuery = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?) " +
                "AND USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID =?)";
        return jdbcTemplate.query(sqlQuery, this::makeUser, firstId, secondId);
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("user_id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}



