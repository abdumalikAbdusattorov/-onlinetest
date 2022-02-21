package uz.pdp.apponlinetestserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Question extends AbsEntity {
    private String question;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Test test;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "question",cascade = CascadeType.ALL)
    private List<Answer> answers;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "question",cascade = CascadeType.ALL)
    private List<Comment> comments;
}
