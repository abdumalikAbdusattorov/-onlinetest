package uz.pdp.apponlinetestserver.security;

import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.entity.enums.RoleName;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ReqSignUp;
import uz.pdp.apponlinetestserver.repository.RoleRepository;
import uz.pdp.apponlinetestserver.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    private final MessageSource messageSource;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, MessageSource messageSource, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumberOrEmail) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumberOrEmail(phoneNumberOrEmail,phoneNumberOrEmail).orElseThrow(() -> new UsernameNotFoundException(phoneNumberOrEmail));
    }


    public UserDetails loadUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User id not found: " + userId));
    }


    public ApiResponse register(ReqSignUp reqSignUp) {
        Optional<User> optionalUser = userRepository.findByPhoneNumberOrEmail(reqSignUp.getPhoneNumber(),reqSignUp.getEmail());
        if (optionalUser.isPresent()) {
            return new ApiResponse("Error", false);
        } else {
            User user = new User();
               user.setPhoneNumber(reqSignUp.getPhoneNumber());
               user.setEmail(reqSignUp.getEmail());
               user.setPassword(passwordEncoder.encode(reqSignUp.getPassword()));
               user.setFirstName(reqSignUp.getFirstName());
               user.setLastName(reqSignUp.getLastName());
               user.setRoles(roleRepository.findAllByNameIn(Collections.singletonList(RoleName.ROLE_USER)));
               userRepository.save(user);
            return new ApiResponse("Saved", true);
        }
    }


}
