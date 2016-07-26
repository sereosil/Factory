package factory_bd.service;

import factory_bd.entity.*;
import factory_bd.repository.*;
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
            User ivan;
            Date date =new Date();
            long delta=date.getTime();
            roleRepository.save(new UserRole(false,false,true,false));
            roleRepository.save(role=new UserRole(true,false,true,false));
            roleRepository.save(new UserRole(false,false,false,false));
            roleRepository.save(new UserRole(true,true,true,false));
            roleRepository.save(new UserRole(false,true,false,true));

            repository.save(ivan=new User("Иван", "Иванов","88005553535",role,"qwe@mail.ru"));
            repository.save(new User("Иван1", "Иванов2","88005553535",role,"dread@mail.ru"));
            repository.save(new User("Иван3", "Иванов4","88005553535",role,"vasily@mail.ru"));
            //requestRepository.save(new Request(new Date(), new Date(delta+5000000000000000L),ivan));
            // repository.save(new User("Иван", "Васильевич","88001488228",new UserRole(true,false,true,false)));
            // repository.save(new User("Василий", "Петров","12345678900",new UserRole(false,false,false,false)));
            // repository.save(new User("Петр", "Сидоров","12",new UserRole(true,true,true,false)));
            // repository.save(new User("Сидор", "Васильев","22",new UserRole(false,true,false,true)));
            // выборка всех кастомеров
            log.info("Кастомеры найденые через findAll(): ");
            log.info("____________________________________");
            for (User user : repository.findAll()) {
                log.info(user.toString());
            }
            log.info("");
            //выборка индивидуального кастомера по ID
            User user = repository.findOne(1);
            log.info("Кастомеры найденые через findOne(1): ");
            log.info("____________________________________");
            log.info(user.toString());
            log.info("");
            //выборка кастомеров по фамилии
            log.info("Кастомеры, найденные по findByLastNameStartsWithIgnoreCase('Петров'):");
            log.info("____________________________________");
            for (User lastName : repository.findByLastNameStartsWithIgnoreCase("Петров")) {
                log.info(lastName.toString());
            }
            log.info("");
            log.info("Кастомеры, найденные по findByContact:");
            log.info("____________________________________");
            for (User contact : repository.findByContact("88005553535")) {
                log.info(contact.toString());
            }
            log.info("");
            log.info("Кастомеры, найденные по findByUserRole:");
            log.info("____________________________________");
            for (User userRole : repository.findByUserRole(role)) {
                log.info(userRole.toString());
            }
            log.info("");
            log.info("Кастомеры, найденные по findByCreatedBy:");
            log.info("____________________________________");
            for (Request request : requestRepository.findByCreatedBy(ivan)) {
                log.info(request.toString());
            }
            log.info("");
            log.info("Кастомеры найденые через findAll(): ");
            log.info("____________________________________");
            for (UserRole userRole: roleRepository.findAll()) {
                log.info(userRole.toString());
            }
            log.info("");
            log.info("Кастомеры найденые через findAll(): ");
            log.info("____________________________________");
            for (Request request : requestRepository.findAll()) {
                log.info(request.toString());
            }
            log.info("");
            log.info("Кастомеры, найденные по findByAdmin:");
            log.info("____________________________________");
            for (UserRole userRole : roleRepository.findByAdmin(true)) {
                log.info(userRole.toString());
            }
            log.info("");
            log.info("Кастомеры, найденные по findByViewAndAddAndConfirm:");
            log.info("____________________________________");
            for (UserRole userRole : roleRepository.findByViewAndAddAndConfirm(false,true,false)) {
                log.info(userRole.toString());
            }
            log.info("");

            /*
            * Из другого проекта
            * */
            //компании
            Company itmo = new Company("ITMO");

            companyRepository.save(itmo);


            Person testPerson = new Person("Yarik", "Schehvatow", companyRepository.getOne(1), "1234");
            //Person testPerson1 = new Person("qwer", "qwer", "SPbGTI", "1234");
           
            //companyRepository.save(new Company("qwer", testPerson1));
            //сохраним персон

            personRepository.save(new Person("Yarik", "Schehvatow", itmo, "1234"));
            /*personRepository.save(new Person("Valerii", "Koval", "SPbGTI", "2345"));
            personRepository.save(new Person("Dasha", "Lathisheva", "SPbGTI", "8966"));
            personRepository.save(new Person("Gorge", "Mayster", "ITMO", "8898"));*/
            //сохраним автомобили
            carRepository.save(new Car("Red", "889", "Toyota"));
            carRepository.save(new Car("Pink", "336", "Nissan"));
            carRepository.save(new Car("Black", "986", "Mercedes"));

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

            log.info("Find persons by companyName");
            log.info("---------------------------------");
            for (Person person : personRepository.findByCompanyName(itmo)) {
                log.info(person.toString());
            }
            log.info("");

            log.info("Find all persons");
            log.info("---------------------------------");
            for (Person person : personRepository.findAll()) {
                log.info(person.toString());
            }
            log.info("");

        };
    }
}
