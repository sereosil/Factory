package factory_bd;

import factory_bd.entity.*;
import factory_bd.repository.*;
import factory_bd.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Date;

/**
 * Created by sereo_000 on 21.07.2016.
 */
@SpringBootApplication
public class Application {
    private static final Logger log= LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
    @Bean
    public CommandLineRunner loadData(UserRepository repository,
                                      UserRoleRepository roleRepository,
                                      RequestRepository requestRepository/**/,
                                      PersonRepository personRepository,
                                      CarRepository carRepository,
                                      CompanyRepository companyRepository) {
        return (args) -> {
            //добавить немного  кастомеров
            UserRole role;
            UserRole role1;
            UserRole role2;
            UserRole role3;
            User ivan;
            Date date = new Date();
            long delta = date.getTime();
            roleRepository.save(new UserRole(false, false, true, false));
            roleRepository.save(role = new UserRole(true, false, true, false));
            roleRepository.save(role1 = new UserRole(true, true, true, true));
            roleRepository.save(role2 = new UserRole(true, true, true, false));
            roleRepository.save(role3 = new UserRole(false, true, false, true));

            String pass="5555";
            pass= DigestUtils.md5Hex(pass);
            repository.save(ivan=new User("Иван", "Иванов","88005553535",role,"dfdf",pass));

             repository.save(new User("Иван", "Васильевич","88001478268",role,"test@mail.ru",pass));
             repository.save(new User("Василий", "Петров","12345678900",role1,"superuser@mail.ru",pass));
             repository.save(new User("Петр", "Сидоров","12",role2,"user1@mail.ru",pass));
             repository.save(new User("Сидор", "Васильев","22",role3,"user2@mail.ru",pass));
            // выборка всех кастомеров
            log.info("Кастомеры найденые через findAll(): ");
            log.info("____________________________________");
            for (User user : repository.findAll()) {
                log.info(user.toString());
            }
            log.info("");

            Company itmo = new Company("ITMO", "6565", "5454");
            Company gti = new Company("GTI", "2323", "4545");

            companyRepository.save(itmo);
            companyRepository.save(gti);


            /*Person testPerson = new Person("Yarik", "Schehvatow", companyRepository.getOne(1), "1234");
            Person testPerson1 = new Person("qwer", "qwer", "SPbGTI", "1234");*/

            //companyRepository.save(new Company("qwer", testPerson1));
            //сохраним персон

            personRepository.save(new Person("Yarik", "Schehvatow", gti, "1234"));
            personRepository.save(new Person("Valerii", "Koval", gti, "2345"));
            personRepository.save(new Person("Gohsa", "Mayster", itmo, "sdasd"));
            personRepository.save(new Person("sfsdf", "Mayster", itmo, "sdasd"));
            personRepository.save(new Person("sdfsdf", "Mayster", itmo, "sdasd"));
            /*personRepository.save(new Person("Dasha", "Lathisheva", "SPbGTI", "8966"));
            personRepository.save(new Person("Gorge", "Mayster", "ITMO", "8898"));*/
            //сохраним автомобили
            carRepository.save(new Car("Red", "889", "Toyota", itmo));
            carRepository.save(new Car("Pink", "336", "Nissan", itmo));
            carRepository.save(new Car("Black", "986", "Mercedes", gti));

             /*
          * Поиск в carRepository
          * */
            log.info("Find all cars");
            log.info("---------------------------------");
            for (Car car : carRepository.findAll()) {
                log.info(car.toString());
            }
            log.info("");

            log.info("Find cars by color");
            log.info("---------------------------------");
            for (Car car : carRepository.findByCarColorStartsWithIgnoreCase("Red")) {
                log.info(car.toString());
            }
            log.info("");

            log.info("Find cars by registrationNumber");
            log.info("---------------------------------");
            for (Car car : carRepository.findByCarRegistrationNumberStartsWithIgnoreCase("889")) {
                log.info(car.toString());
            }
            log.info("");

            log.info("Find cars by carModel");
            log.info("---------------------------------");
            for (Car car : carRepository.findByCarModelStartsWithIgnoreCase("Nissan")) {
                log.info(car.toString());
            }
            log.info("");

          /*
          * Поиск в companyRepository
          * */
            log.info("Find all company");
            log.info("---------------------------------");
            for (Company company : companyRepository.findAll()) {
                log.info(company.toString());
            }
            log.info("");


          /*
          * Поиск в personRepository
          * */


        };
    }
}
