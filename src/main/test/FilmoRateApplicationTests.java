import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.App;
import ru.yandex.model.Film;
import ru.yandex.model.User;
import ru.yandex.storage.FilmDbStorage;
import ru.yandex.storage.UserDbStorage;
import ru.yandex.storage.mapper.FilmMapper;
import ru.yandex.storage.mapper.UserMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({UserDbStorage.class, FilmDbStorage.class, UserMapper.class, FilmMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = App.class)
@DirtiesContext

class FilmoRateApplicationTests {

    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        Long userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM my_users", Long.class);
        System.out.println("Количество пользователей: " + userCount);
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .login("test_login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User createdUser = userDbStorage.createUser(user);

        assertNotNull(createdUser.getId());
        assertEquals("Test User", createdUser.getName());
    }

    @Test
    void testUpdateUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .login("test_login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User createdUser = userDbStorage.createUser(user);

        createdUser.setName("Updated Name");
        User updatedUser = userDbStorage.updateUser(createdUser);

        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .login("test_login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        User createdUser = userDbStorage.createUser(user);

        userDbStorage.deleteUser(createdUser);

        List<User> users = (List<User>) userDbStorage.allUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    void testCreateFilm() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .genre(1)
                .rating(1)
                .build();
        Film createdFilm = filmDbStorage.createFilm(film);

        assertNotNull(createdFilm.getId());
        assertEquals("Test Film", createdFilm.getName());
    }

    @Test
    void testUpdateFilm() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .genre(1)
                .rating(1)
                .build();
        Film createdFilm = filmDbStorage.createFilm(film);

        createdFilm.setName("Updated Film");
        Film updatedFilm = filmDbStorage.updateFilm(createdFilm);

        assertEquals("Updated Film", updatedFilm.getName());
    }

    @Test
    void testDeleteFilm() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .genre(1)
                .rating(1)
                .build();
        Film createdFilm = filmDbStorage.createFilm(film);

        filmDbStorage.deleteFilm(createdFilm);

        List<Film> films = (List<Film>) filmDbStorage.allFilms();
        assertTrue(films.isEmpty());
    }

    @Test
    void testGetUserId1() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .login("test_login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userDbStorage.createUser(user);
        User user1 = userDbStorage.getUserId(4L);

                assertEquals("Test User", user1.getName());
    }

    @Test
    void getAllUsers() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .login("test_login")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new HashSet<>())
                .build();
        userDbStorage.createUser(user);
        User user1 = User.builder()
                .name("Test User")
                .email("test@example.com")
                .login("test_login")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new HashSet<>())
                .build();
        userDbStorage.createUser(user1);
        List<User> users = (List<User>) userDbStorage.allUsers();
        List<User> testUsers = new ArrayList<>();
        testUsers.add(user);
        testUsers.add(user1);
        Assertions.assertArrayEquals(testUsers.toArray(), users.toArray());
    }
    
    @Test
    void getAllFilms() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .genre(1)
                .rating(1)
                .like(0L)
                .userLikes(new HashSet<>())
                .build();
        filmDbStorage.createFilm(film);
        Film film1 = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .genre(1)
                .rating(1)
                .like(0L)
                .userLikes(new HashSet<>())
                .build();
        filmDbStorage.createFilm(film1);
        List<Film> films = (List<Film>) filmDbStorage.allFilms();
        List<Film> testFilms = new ArrayList<>();
        testFilms.add(film);
        testFilms.add(film1);
        Assertions.assertArrayEquals(testFilms.toArray(), films.toArray());
    }
}