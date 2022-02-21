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
public class Answer extends AbsEntity {
    private String answer;

    private boolean correctAnswer;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

}
