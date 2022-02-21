package uz.pdp.apponlinetestserver.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.apponlinetestserver.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private String commentText;

    private boolean viewed;

}
