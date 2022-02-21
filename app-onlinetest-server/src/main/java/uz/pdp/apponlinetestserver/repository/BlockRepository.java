package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apponlinetestserver.entity.Block;
import uz.pdp.apponlinetestserver.entity.enums.Level;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockRepository extends JpaRepository<Block, Integer> {
    List<Block> findAllByLevel(Level level);
}
