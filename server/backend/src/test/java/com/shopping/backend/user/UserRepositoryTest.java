package com.shopping.backend.user;

import com.shopping.entity.Role;
import com.shopping.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void createUserWithOneRoleTest() {
        Role roleAdmin = entityManager.find(Role.class, 1);

        User newUser = new User("test@gmail.com", "1234", "테스트유저");
        newUser.addRole(roleAdmin);

        User savedUser = repo.save(newUser);

        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void createUserWithManyRolesTest() {
        Role roleShipper = entityManager.find(Role.class, 4);
        Role roleEditor = entityManager.find(Role.class, 3);

        User newUser = new User("aaaa@gmail.com", "1234", "역할테스트유저");
        newUser.addRole(roleShipper);
        newUser.addRole(roleEditor);

        User savedUser = repo.save(newUser);

        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }
}
