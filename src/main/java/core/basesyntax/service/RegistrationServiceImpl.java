package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_AGE = 18;

    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User must not be null");
        }

        if (user.getLogin() == null) {
            throw new RegistrationException("Login must not be null");
        }
        if (user.getLogin().length() < MIN_LOGIN_LENGTH) {
            throw new RegistrationException("Login must be at least "
                    + MIN_LOGIN_LENGTH
                    + " characters long");
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("User with login '"
                    + user.getLogin()
                    + "' already exists");
        }

        if (user.getPassword() == null) {
            throw new RegistrationException("Password must not be null");
        }
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new RegistrationException("Password must be at least "
                    + MIN_PASSWORD_LENGTH
                    + " characters long");
        }

        if (user.getAge() == null) {
            throw new RegistrationException("Age must not be null");
        }
        if (user.getAge() < MIN_AGE) {
            throw new RegistrationException("User must be at least "
                    + MIN_AGE
                    + " years old, got: "
                    + user.getAge());
        }

        return storageDao.add(user);
    }
}

