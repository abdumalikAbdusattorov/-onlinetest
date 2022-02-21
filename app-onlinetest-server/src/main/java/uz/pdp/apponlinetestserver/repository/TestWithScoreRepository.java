package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.apponlinetestserver.entity.TestWithScore;

import java.util.UUID;

public interface TestWithScoreRepository extends JpaRepository<TestWithScore, UUID> {
    @Query(value = "select tws.score from test_with_score tws join test_block tb on tws.test_block_id = tb.id and tb.id = :testBlockId join test t on tws.test_id = t.id join question q on t.id = q.test_id join answer a on q.id = a.question_id and a.id = :answerId", nativeQuery = true)
    double getScoreByTestBlockAndAnswer(@Param(value = "testBlockId") UUID testBlockId,@Param(value = "answerId") UUID answerId);

}
