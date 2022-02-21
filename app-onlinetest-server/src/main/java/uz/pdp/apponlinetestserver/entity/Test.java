package uz.pdp.apponlinetestserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Test extends AbsEntity {
    private String title;

    @Enumerated(EnumType.STRING)
    private Level level;


    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "test",cascade = CascadeType.ALL)
    private List<Question> questions;
}
