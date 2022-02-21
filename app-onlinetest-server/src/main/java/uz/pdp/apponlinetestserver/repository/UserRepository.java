package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apponlinetestserver.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumberOrEmail(String phoneNumber, String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByTelegramChatId(Integer telegramChatId);

    void deleteById(UUID uuid);
}
