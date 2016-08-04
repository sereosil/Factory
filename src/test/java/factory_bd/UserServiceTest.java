package factory_bd;

import factory_bd.entity.User;
import factory_bd.entity.UserRole;
import factory_bd.repository.UserRepository;
import factory_bd.repository.UserRoleRepository;
import factory_bd.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by sereo_000 on 25.07.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserRepository repository;
    @Mock
    UserRoleRepository roleRepository;
    String forHashTest="12345";
    String forHashTest2="54321";
    UserRole role=new UserRole(true,false,true,false);;
    User user=new User("Иван", "Иванов","88005553535",role,"qwe@mail.ru","5555",true);
    private User s;

    @Before
    public void setUp() throws Exception {
        s = new User("Иван1", "Иванов2", "88005553535", role, "dread@mail.ru","5555",true);
    }
    @Before
    public void prepareMock(){
        Mockito.when(repository.findByEmail("qwe@mail.ru")).thenReturn(Arrays.asList(new User [] {user}));
        Mockito.when(repository.findByEmailAndPasswordHash("qwe@mail.ru","55555")).thenReturn(Arrays.asList(new User [] {user}));
    }
    @Test
    public void haveUserTest() throws Exception {
        UserService userService = new UserService(repository);
        boolean b = userService.haveUser("qwe@mail.ru");
        assertTrue(b);
    }

    @Test
    public void getValidUserTest() throws Exception {
        UserService userService = new UserService(repository);
        User b = userService.getValidUser("qwe@mail.ru","55555");
        assertEquals(b,user);
    }

    /*@Test
    public void hashPasswordTest() throws Exception {
        UserService userService = new UserService(repository);
       forHashTest=userService.hashPassword(forHashTest);
        forHashTest2=userService.hashPassword(forHashTest2);
        assertEquals(forHashTest,forHashTest2);
    }*/

    @Test
    public void ifDefaultPasswordTest() throws Exception {
        UserService userService = new UserService(repository);
        boolean b = userService.ifNeedToChangePassword("qwe@mail.ru");
        assertTrue(b);
    }

    @Test
    public void changeUserFirstNameTest() throws Exception {
        String firstName = "VALEEEEEERA";
        String firstNameTest;
        UserService userService = new UserService(repository);
        userService.changeUserFirstName(user,firstName);
        firstNameTest=user.getFirstName();
        assertEquals(firstName,firstNameTest);
    }

}