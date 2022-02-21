package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apponlinetestserver.entity.HistoryUser;
import uz.pdp.apponlinetestserver.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryUserRepository extends JpaRepository<HistoryUser, UUID> {
    Page<HistoryUser> findAllByUser(User user, Pageable pageable);
    List<HistoryUser> findAllByUser(User user);

    Optional<HistoryUser> findByUserAndTestBlockIdAndFinished(User user, UUID testBlock_id, boolean finished);

}
