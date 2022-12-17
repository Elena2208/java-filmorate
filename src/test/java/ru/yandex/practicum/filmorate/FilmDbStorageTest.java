package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private   final UserDbStorage userDbStorage;


    private Film film = Film.builder()
            .name("new film")
            .description("....")
            .releaseDate(LocalDate.now())
            .duration(120L)
            .mpa(new Mpa(1, "G"))
            .genres(null)
            .build();

    private Film film2 = Film.builder()
            .name("new film2")
            .description("....")
            .releaseDate(LocalDate.now())
            .duration(126L)
            .mpa(new Mpa(3, "PG-13"))
            .genres(null)
            .build();

    final User user1 = User.builder()
            .email("elena@yandex.ru")
            .login("elena111")
            .name("E")
            .birthday(LocalDate.of(1990, 2, 22))
            .build();
    final User user2 = User.builder()
            .email("max@yandex.ru")
            .login("max111")
            .name("M")
            .birthday(LocalDate.of(1985, 5, 11))
            .build();
    @Test
    void addFilmTest() {
        filmDbStorage.create(film);
        Film newFilm = filmDbStorage.getById((int) film.getId());
        assertThat(film).isEqualToComparingFieldByFieldRecursively(newFilm);

    }

    @Test
    void getByIdTest() {
        filmDbStorage.create(film);
        Film newFilm = filmDbStorage.getById((int) film.getId());
        assertThat(film).isEqualToComparingFieldByFieldRecursively(newFilm);
    }

    @Test
    void findByAll() {
        filmDbStorage.create(film);
        filmDbStorage.create(film2);
        List<Film> films = (List<Film>) filmDbStorage.findAll();
        assertThat(films).isNotEmpty();
    }


    @Test
    void updateTest() {
        filmDbStorage.create(film);
        film.setName("test");
        film.setDescription("test");
        filmDbStorage.update(film);
        AssertionsForClassTypes.assertThat(filmDbStorage.getById((int) film.getId()))
                .hasFieldOrPropertyWithValue("name", "test")
                .hasFieldOrPropertyWithValue("description", "test");
    }

    @Test
    void deleteById() {
        filmDbStorage.create(film);
        filmDbStorage.deleteById((int) film.getId());
        Assertions.assertThatThrownBy(() -> filmDbStorage.getById((int) film.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addLikeTest(){
        filmDbStorage.create(film);
        userDbStorage.create(user1);
        filmDbStorage.create(film2);
        userDbStorage.create(user2);
        filmDbStorage.addLike((int) film.getId(), (int) user1.getId());
        filmDbStorage.addLike((int) film.getId(), (int) user2.getId());
        filmDbStorage.addLike((int) film2.getId(), (int) user1.getId());
        filmDbStorage.getPopularFilms(2);
        AssertionsForClassTypes.assertThat(filmDbStorage.getPopularFilms(2).size()==2);

    }

    @Test
    void deleteLikeTest(){
        filmDbStorage.create(film);
        userDbStorage.create(user1);
        filmDbStorage.create(film2);
        userDbStorage.create(user2);
        filmDbStorage.addLike((int) film.getId(), (int) user1.getId());
        filmDbStorage.addLike((int) film.getId(), (int) user2.getId());
        filmDbStorage.addLike((int) film2.getId(), (int) user1.getId());
        filmDbStorage.getPopularFilms(2);
        AssertionsForClassTypes.assertThat(filmDbStorage.getPopularFilms(2).size()==2);
        filmDbStorage.deleteLike((int) film2.getId(), (int) user1.getId());
        filmDbStorage.getPopularFilms(2);
        AssertionsForClassTypes.assertThat(filmDbStorage.getPopularFilms(2).size()==1);
    }

    @Test
    void getPopulatFilmTest(){
        filmDbStorage.create(film);
        userDbStorage.create(user1);
        filmDbStorage.create(film2);
        userDbStorage.create(user2);
        filmDbStorage.addLike((int) film.getId(), (int) user1.getId());
        filmDbStorage.addLike((int) film.getId(), (int) user2.getId());
        filmDbStorage.addLike((int) film2.getId(), (int) user1.getId());
        filmDbStorage.getPopularFilms(2);
        AssertionsForClassTypes.assertThat(filmDbStorage.getPopularFilms(2).size()==2);
    }

}
