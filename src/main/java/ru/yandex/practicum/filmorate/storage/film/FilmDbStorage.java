package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select films.FILM_ID,films.NAME,films.DESCRIPTION, " +
                " films.RELEASE_DATE, films.DURATION, films.mpa_id, mpa.mpa_name from films " +
                " join MPA ON films.mpa_id=mpa.mpa_id order by film_id";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }


    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films(name,description,release_date,duration,mpa_id) values (?, ?, ?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey().longValue()));
        final String genresSqlQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(genresSqlQuery, film.getId(), g.getId());
            }
        }
        film.setMpa(findMpa((int) film.getId()));
        film.setGenres(findGenres((int) film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("Select * from films where film_id=?", film.getId());
        if (!rs.next()) {
            throw new NotFoundException("Фильм не найден.");
        }

        final String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ? , mpa_id=?  WHERE film_id = ?";

        if (film.getGenres() != null) {
            jdbcTemplate.update("delete from film_genres where film_id=?", film.getId());
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), g.getId());
            }
        }
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        film.setMpa(findMpa((int) film.getId()));
        film.setGenres(findGenres((int) film.getId()));
        return film;

    }

    @Override
    public Film getById(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("Select * from films where film_id=?", id);
        if (!rs.next()) {
            throw new NotFoundException("Фильм не найден.");
        }
        return jdbcTemplate.queryForObject("select films.FILM_ID,films.NAME,films.DESCRIPTION,films.RELEASE_DATE,"
                        + "films.DURATION, films.mpa_id, mpa.mpa_name from films join MPA ON films.mpa_id=mpa.mpa_id " +
                        "where films.film_id=?",
                this::makeFilm, id);
    }

    @Override
    public void deleteById(int id) {

        jdbcTemplate.update("delete from FILM_GENRES where film_id=?", id);
        jdbcTemplate.update("delete from likes where film_id=?", id);
        jdbcTemplate.update("delete from films where film_id=?", id);

    }

    @Override
    public void addLike(int filmId, int userId) {
        containsId(filmId, userId);
        jdbcTemplate.update("insert into likes(user_id,film_id) values (?,?)", userId, filmId);

    }

    @Override
    public void deleteLike(int filmId, int userId) {
        containsId(filmId, userId);
        jdbcTemplate.update("delete from likes where user_id=? and film_id=?", userId, filmId);

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "select f.FILM_ID, f.RELEASE_DATE,  f.DESCRIPTION, f.DURATION, f.NAME, MPA.MPA_ID," +
                " MPA.MPA_NAME " +
                "from FILMs as f " +
                "left join LIKES L on F.FILM_ID = L.FILM_ID " +
                "left join MPA on f.MPA_ID = MPA.MPA_ID " +
                "group by f.FILM_ID " +
                "order by count(l.USER_ID) desc " +
                "limit ?";


        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);

    }

    private void containsId(int filmId, int userId) {
        SqlRowSet sqlFilm = jdbcTemplate.queryForRowSet("SELECT * FROM films where film_id=?", filmId);
        SqlRowSet sqlUser = jdbcTemplate.queryForRowSet("SELECT * FROM users where user_id=?", userId);
        if (!sqlFilm.next() || !sqlUser.next()) {
            throw new NotFoundException("Объекты не найдены.");
        }
    }


    private Set<Genre> findGenres(int filmId) {
        Set<Genre> genres = new LinkedHashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select  * from FILM_GENRES G" +
                " join GENRE G2 on G2.GENRE_ID = G.GENRE_ID " +
                "where film_id = ?  order by Genre_id asc  ", filmId);
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }

        return genres;
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name"));
    }

    private Mpa findMpa(int filmId) {
        return jdbcTemplate.queryForObject("SELECT mpa_id, mpa_name " +
                "FROM mpa " +
                "WHERE mpa_id in (select mpa_id from films where film_id=?)", this::makeMpa, filmId);
    }

    private Film makeFilm(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("film_id");
        return new Film(id, resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getLong("duration"),
                new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")),
                findGenres(id));
    }

}
