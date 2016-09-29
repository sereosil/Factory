package ru.od.factory_bd.repository;

import ru.od.factory_bd.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
    List<UserRole> findByViewAndAddAndConfirm(boolean view, boolean add, boolean confirm);
    List<UserRole> findByAdmin(boolean admin);
}
