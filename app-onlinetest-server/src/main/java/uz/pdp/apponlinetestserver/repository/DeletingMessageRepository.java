package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apponlinetestserver.entity.DeletingMessage;

import java.util.List;
import java.util.UUID;

public interface DeletingMessageRepository extends JpaRepository<DeletingMessage, UUID> {
    List<DeletingMessage> findAllByChatId(Long chatId);
}
