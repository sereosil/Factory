package factory_bd.service;

import com.vaadin.spring.annotation.SpringComponent;
import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Manages all users from factory in system
 * Created by sereo_000 on 22.07.2016.
 *
 */
@SpringComponent
public class UserService {
    private final UserRepository repository;
    @Autowired
    public UserService(UserRepository repository){
        this.repository=repository;
    }
    public boolean haveUser(String email){
        for(User user:repository.findByEmail(email)){
            return true;
        }
        return false;
    }
    public User getValidUser(String email, String password){
        String passwordChecker;
        for (User user:repository.findByEmail(email)){
            passwordChecker=user.getPasswordHash();
            if(passwordChecker.equals(password)) {
                passwordChecker = hashPassword(password);
                user.setPasswordHash(passwordChecker);
                repository.save(user);
                return user;
            }
        }
        return null;
    }
    private String hashPassword(String password){
        String passwordChecker;
        passwordChecker = DigestUtils.md5Hex(password);
        return passwordChecker;
    }
    public boolean ifDefaultPassword(String email,String password){
        boolean b = checkPassword(email, password);
        if(b) {
            for (User user : repository.findByEmail(email)) {
                if (password.equals("55555")) {
                    return true;
                }
            }
        }
            return false;
    }

    /**
     * Update all users with email obtained from parameters
     * @throws RuntimeException if no user with such email
     * @param email - user email
     * @param oldPassword - old password
     * @param newPassword - new password
     */
    public void changePassword(String email, String oldPassword, String newPassword) {
        String passwordChecker;
        for (User user:repository.findByEmail(email)){
            passwordChecker=user.getPasswordHash();
            if(passwordChecker.equals(oldPassword)) {
                passwordChecker = hashPassword(newPassword);
                user.setPasswordHash(passwordChecker);
                repository.save(user);
            }
        }
    }
    public boolean checkViewPermission(User user){
        if(user.getUserRole().isView()) return true;
        return false;
    }
    public boolean checkAddPermission(User user){
        if(user.getUserRole().isAdd()) return true;
        return false;
    }
    public boolean checkConfirmPermission(User user){
        if(user.getUserRole().isConfirm()) return true;
        return false;
    }
    public boolean checkAdminPermission(User user){
        if(user.getUserRole().isAdmin()) return true;
        return false;
    }
    public void addUser(String firstName, String lastName, String contact, UserRole role, String email){
        User user = new User(firstName, lastName, contact, role, email);
        repository.save(user);
    }
    public void changeUserRole(User user,UserRole role){
        user.setUserRole(role);
       repository.save(user);
    }

    public void deleteUser(User user){
        repository.delete(user);
    }
    public void changeUserEmail(User user, String email){
        user.setEmail(email);
        repository.save(user);
    }
    public void changeUserFirstName(User user, String firstName){
        user.setFirstName(firstName);
        repository.save(user);
    }
    public void changeUserLastName(User user, String lastName){
        user.setFirstName(lastName);
        repository.save(user);
    }
    public void changeUserPhone(User user, String contact){
        user.setContact(contact);
        repository.save(user);
    }
    public boolean checkPassword(String email, String password){
        String passwordChecker;
        for (User user:repository.findByEmail(email)){
            passwordChecker=user.getPasswordHash();
            if(passwordChecker.equals(password)) {
                return true;
            }
        }
        return false;
    }
}
