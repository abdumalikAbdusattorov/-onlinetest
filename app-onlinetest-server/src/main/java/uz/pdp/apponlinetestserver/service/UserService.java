package uz.pdp.apponlinetestserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apponlinetestserver.entity.Role;
import uz.pdp.apponlinetestserver.entity.User;
import uz.pdp.apponlinetestserver.payload.ApiResponse;
import uz.pdp.apponlinetestserver.payload.ReqPassword;
import uz.pdp.apponlinetestserver.payload.ResUser;
import uz.pdp.apponlinetestserver.repository.RoleRepository;
import uz.pdp.apponlinetestserver.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MessageSource messageSource;

   /* public ApiResponse addUser(ReqSignUp request) {
        if (request.getId()==null) {
            User user = new User(
                    request.getPhoneNumber(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getFirstName(),
                    request.getLastName(),
                    roleRepository.findAllByNameIn(
                            Collections.singletonList(RoleName.ROLE_MANAGER)
            ));
            userRepository.save(user);
            return new ApiResponse("Foydalanuvchi muvoffaqiyatli ro'yxatga olindi", true);
        }else {
            Optional<User> optionalUser = userRepository.findById(request.getId());
            if (optionalUser.isPresent()){
                User user = optionalUser.get();
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setPhoneNumber(request.getPhoneNumber());
                userRepository.save(user);
                return new ApiResponse("Foydalanuvchi malumotlari muvofaqqiyatli o'zgartirildi.", true);
            }else {
                return new ApiResponse("Bunday telefon raqamli foydalanuvchi mavjud", false);
            }
        }
    }*/


    public ResponseEntity changePassword(ReqPassword request, User user) {
        if (request.getPassword().equals(request.getPrePassword())) {
            if (checkPassword(request.getOldPassword(), user)) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("Parol o'zgartirildi", true));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Hozirgi parol xato", false));
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Yangi va tasdiqlovchi parol mos emas", false));
        }
    }


    private Boolean checkPassword(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


   /* public HttpEntity<?> editUser(ReqSignUp reqUser, User user) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        user.setFirstName(reqUser.getFirstName());
        user.setLastName(reqUser.getLastName());

        if (!user.getPhoneNumber().equals(reqUser.getPhoneNumber())) {
            if (!userRepository.findByPhoneNumber(reqUser.getPhoneNumber()).isPresent()) {
                user.setPhoneNumber(reqUser.getPhoneNumber());
            } else {
                response.setSuccess(false);
                response.setMessage("Phone number is already exist");
            }
        }
        if (response.isSuccess()) {
            response.setMessage(messageSource.getMessage("user.edited", null, LocaleContextHolder.getLocale()));
        } else {
            response.setMessage(messageSource.getMessage("error", null, LocaleContextHolder.getLocale()));
        }
        userRepository.save(user);
        return ResponseEntity.ok(response);
    }*/

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public ResUser getResUser(User user){
        ResUser resUser=new ResUser();
        resUser.setId(user.getId());
        resUser.setFirstName(user.getFirstName());
        resUser.setLastName(user.getLastName());
        resUser.setPhoneNumber(user.getPhoneNumber());
        resUser.setPassword(user.getPassword());
        List<String> stringList=new ArrayList<>();
        for (Role role : user.getRoles()) {
            stringList.add(role.getName().name());
        }
        resUser.setRoleNameList(stringList);
        return resUser;
    }
}
