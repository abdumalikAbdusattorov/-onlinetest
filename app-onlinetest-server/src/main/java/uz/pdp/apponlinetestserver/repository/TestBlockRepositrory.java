package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.apponlinetestserver.entity.TestBlock;
import uz.pdp.apponlinetestserver.entity.enums.Level;

import java.lang.annotation.Native;
import java.util.List;
import java.util.UUID;

public interface TestBlockRepositrory extends JpaRepository<TestBlock, UUID> {
    List<TestBlock> findAllByBlockIdAndBlockLevel(Integer block_id, Level level);

    @Modifying
    @Query(value = "delete from  testWithScores where test_block_id=:testBlockId", nativeQuery = true)
    void deleteTestWithScoreByTestBlockId(@Param(value = "testBlockId")UUID testBlockId);

}
