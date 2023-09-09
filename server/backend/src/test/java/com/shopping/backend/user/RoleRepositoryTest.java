package com.shopping.backend.user;

import com.shopping.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateRoleAdmin() {
        Role admin = new Role(0L, "Admin", "관리자");

        Role savedRole = repo.save(admin);

        Assertions.assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRoles() {
        Role salesperson = new Role(0L, "SalesPerson", "판매자");
        Role editor = new Role(0L, "Editor", "편집자");
        Role shipper = new Role(0L, "Shipper", "배송자");
        Role assistant = new Role(0L, "Assistant", "보조");

        repo.saveAll(List.of(salesperson, editor, shipper, assistant));


    }
}
