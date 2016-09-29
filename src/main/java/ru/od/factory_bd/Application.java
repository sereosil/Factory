package ru.od.factory_bd;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.od.factory_bd.entity.*;
import ru.od.factory_bd.repository.*;

import java.util.Date;

/**
 * Created by sereo_000 on 21.07.2016.
 */
@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository repository,
                                      UserRoleRepository roleRepository,
                                      RequestRepository requestRepository,
                                      PersonRepository personRepository,
                                      CarRepository carRepository,
                                      CompanyRepository companyRepository) {
        return (args) -> {
            if (args.length > 0) {
                log.info("Init database");
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

                String pass = "1";
                pass = DigestUtils.md5Hex(pass);
                repository.save(ivan = new User("Иван", "Иванов", "88005553535", role, "dfdf", pass));
                ivan.setNeedToChangePassword(false);
                repository.save(new User("Иван", "Васильевич", "88001478268", role, "test@mail.ru", pass));
                repository.save(new User("Василий", "Петров", "12345678900", role1, "1", pass));//SUPER
                repository.save(new User("Петр", "Сидоров", "12", role2, "user1@mail.ru", pass));
                repository.save(new User("Сидор", "Васильев", "22", role3, "user2@mail.ru", pass));
                // выборка всех кастомеров
                log.info("Кастомеры найденые через findAll(): ");
                log.info("____________________________________");
                for (User user : repository.findAll()) {
                    log.info(user.toString());
                }
                log.info("");

                Company itmo = new Company("Итмо", "Кронверкский пр. 49 ", "371-37-84");
                Company gti = new Company("Технологический Институт", "Московский проспект д. 22", " 316-46-56");
                Company politech = new Company("Политехнический университет ", "Политехническая, 29", "552-60-80");

                companyRepository.save(itmo);
                companyRepository.save(gti);
                companyRepository.save(politech);



            /*Person testPerson = new Person("Yarik", "Schehvatow", companyRepository.getOne(1), "1234");
            Person testPerson1 = new Person("qwer", "qwer", "SPbGTI", "1234");*/

                //companyRepository.save(new Company("qwer", testPerson1));
                //сохраним персон

                personRepository.save(new Person("Ярослав", "Шехватов", gti, "4001123456"));
                personRepository.save(new Person("Валерий", "Коваль", gti, "4002369859"));
                personRepository.save(new Person("Лада", "Соколова", gti, "4002789658"));

                personRepository.save(new Person("Георгий", "Майстер", itmo, "4002369781"));
                personRepository.save(new Person("Петр", "Федосков", itmo, "4008879643"));
                personRepository.save(new Person("Святослав", "Косовских", itmo, "4009879741"));

                personRepository.save(new Person("Дарья", "Климова", politech, "4007914848"));
                personRepository.save(new Person("Никола", "Вейештрассов", politech, "4002917848"));
                personRepository.save(new Person("Анатолий", "Занемов", politech, "4001994848"));
                personRepository.save(new Person("Николай", "Смирнов", politech, "3009947789"));

                //сохраним автомобили
                carRepository.save(new Car("Красный", "В555ПР", "Toyota", itmo));
                carRepository.save(new Car("Желтый", "Е584МИ", "Nissan", itmo));
                carRepository.save(new Car("Черный", "Я963УК", "Mercedes", gti));
                carRepository.save(new Car("Синий", "Е777КХ", "Bentley", politech));

             /*
          * Поиск в carRepository
          * */
            /*log.info("Find all cars");
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

          *//*
          * Поиск в companyRepository
          * *//*
            log.info("Find all company");
            log.info("---------------------------------");
            for (Company company : companyRepository.findAll()) {
                log.info(company.toString());
            }
            log.info("");

*/
          /*
          * Поиск в personRepository
          * */

            } else {
                log.info("Do not init database");
            }
        };
    }
}
