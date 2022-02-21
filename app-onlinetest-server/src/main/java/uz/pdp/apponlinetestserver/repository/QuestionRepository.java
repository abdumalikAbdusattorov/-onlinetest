package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.apponlinetestserver.entity.Question;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Query(value = "delete from answer WHERE question_id=:questionId",nativeQuery = true)
    void deleteAnswersByQuestionId(@Param(value = "questionId")UUID questionId);

    @Query(value = "delete from question where id not in (select question_id from answer )",nativeQuery = true)
    void deleteQuestionByQuestionId(@Param(value = "questionId")UUID questionId);


}
