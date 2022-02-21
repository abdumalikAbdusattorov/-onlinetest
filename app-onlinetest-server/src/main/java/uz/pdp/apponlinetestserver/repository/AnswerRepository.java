package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apponlinetestserver.entity.Answer;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {


}
