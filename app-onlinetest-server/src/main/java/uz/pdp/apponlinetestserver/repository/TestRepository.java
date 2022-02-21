package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apponlinetestserver.entity.Test;
import uz.pdp.apponlinetestserver.entity.enums.Level;

import java.util.List;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
    List<Test> findAllBySubjectIdAndLevel(Integer subject_id, Level level);

}
