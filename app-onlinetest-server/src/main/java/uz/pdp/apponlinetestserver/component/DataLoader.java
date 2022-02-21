package uz.pdp.apponlinetestserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.entity.enums.RoleName;
import uz.pdp.apponlinetestserver.repository.RoleRepository;
import uz.pdp.apponlinetestserver.repository.UserRepository;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            userRepository.save(new User(

                    "123",
                    "Abdumalik@gmail.com",

                    passwordEncoder.encode("123"),
                    "Abdumalik",
                    "Abdusattorov",
                    roleRepository.findAllByNameIn(
                            Arrays.asList(RoleName.ROLE_ADMIN)
                    )));
        }

    }
}
