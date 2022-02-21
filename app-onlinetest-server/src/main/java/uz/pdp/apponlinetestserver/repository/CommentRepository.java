package uz.pdp.apponlinetestserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.apponlinetestserver.entity.Comment;
import uz.pdp.apponlinetestserver.entity.User;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findAllByViewed( Pageable pageable,boolean viewed);

    @Query(value = "select count (*)  from comment  where viewed=:viewed",nativeQuery = true)
    int countComment (@Param(value = "viewed")boolean viewed);

    Page<Comment> findAllByUser(User user,Pageable pageable);
}
