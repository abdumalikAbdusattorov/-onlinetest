package uz.pdp.apponlinetestserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.apponlinetestserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HistoryUser extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private TestBlock testBlock;

    private boolean fromBot;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "history_user_answer", joinColumns = {@JoinColumn(name = "history_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "answer_id")})
    private List<Answer> answers;

    private double totalScore;
    private double maxScore;

    private boolean finished;
}
