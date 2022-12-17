package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getId(int id) {
        final String sqlQuery = "select * from mpa where mpa_id=?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            throw new NotFoundException("Рейтинг не найден.");
        }
        return jdbcTemplate.queryForObject("select * from mpa where mpa_id=?", this::makeMpa, id);
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("select * from mpa", this::makeMpa);
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name"));
    }
}
