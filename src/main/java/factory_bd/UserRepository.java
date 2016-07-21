package factory_bd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sereo_000 on 20.07.2016.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    List<User> findByLastNameStartsWithIgnoreCase(String lastName);
    List<User> findByContact(String contact);
    List<User> findByUserRole(UserRole userRole);
}
