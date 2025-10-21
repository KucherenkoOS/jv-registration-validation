package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationException;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationTest {
    private RegistrationServiceImpl registrationService;
    private StorageDaoImpl storageDao;

    @BeforeEach
    void setUp() {
        storageDao = new StorageDaoImpl();
        registrationService = new RegistrationServiceImpl(storageDao);
    }

    @Test
    void register_validUser_ok() {
        User user = new User();
        user.setLogin("validUser");
        user.setPassword("securePass");
        user.setAge(25);

        User result = registrationService.register(user);

        assertEquals(user, result);
        assertEquals(user, storageDao.get("validUser"));
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User();
        user.setLogin(null);
        user.setPassword("abcdef");
        user.setAge(25);

        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_emptyLogin_notOk() {
        User user = new User();
        user.setLogin("");
        user.setPassword("abcdef");
        user.setAge(25);

        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_fiveCharLogin_notOk() {
        User user = new User();
        user.setLogin("abcde");
        user.setPassword("abcdef");
        user.setAge(25);

        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_edgeLoginLength_ok() {
        User user = new User();
        user.setLogin("abcdef");
        user.setPassword("abcdef");
        user.setAge(25);

        User result = registrationService.register(user);

        assertEquals(user, result);
        assertEquals(user, storageDao.get("abcdef"));
    }

    @Test
    void register_duplicateLogin_notOk() {
        User existing = new User();
        existing.setLogin("existingUser");
        existing.setPassword("strongPass");
        existing.setAge(25);
        storageDao.add(existing);

        User newUser = new User();
        newUser.setLogin("existingUser");
        newUser.setPassword("newPass");
        newUser.setAge(30);

        assertThrows(RegistrationException.class, () -> registrationService.register(newUser));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = new User();
        user.setLogin("validUser");
        user.setPassword(null);
        user.setAge(25);

        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("");
        user1.setAge(25);

        User user2 = new User();
        user2.setLogin("login2");
        user2.setPassword("abc");
        user2.setAge(25);

        User user3 = new User();
        user3.setLogin("login3");
        user3.setPassword("abcde");
        user3.setAge(25);

        assertThrows(RegistrationException.class, () -> registrationService.register(user1));
        assertThrows(RegistrationException.class, () -> registrationService.register(user2));
        assertThrows(RegistrationException.class, () -> registrationService.register(user3));
    }

    @Test
    void register_edgePasswordLength_ok() {
        User user = new User();
        user.setLogin("login4");
        user.setPassword("abcdef");
        user.setAge(25);

        User result = registrationService.register(user);

        assertEquals(user, result);
        assertEquals(user, storageDao.get("login4"));
    }

    @Test
    void register_underage_notOk() {
        User user1 = new User();
        user1.setLogin("teenUser");
        user1.setPassword("abcdef");
        user1.setAge(17);

        User user2 = new User();
        user2.setLogin("babyUser");
        user2.setPassword("abcdef");
        user2.setAge(-1);

        assertThrows(RegistrationException.class, () -> registrationService.register(user1));
        assertThrows(RegistrationException.class, () -> registrationService.register(user2));
    }

    @Test
    void register_ageExactly18_ok() {
        User user = new User();
        user.setLogin("adultUser");
        user.setPassword("abcdef");
        user.setAge(18);

        User result = registrationService.register(user);

        assertEquals(user, result);
        assertEquals(user, storageDao.get("adultUser"));
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User();
        user.setLogin("noAgeUser");
        user.setPassword("abcdef");
        user.setAge(null);

        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }
}
