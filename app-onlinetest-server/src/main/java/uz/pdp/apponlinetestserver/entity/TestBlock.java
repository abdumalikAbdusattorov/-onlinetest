package uz.pdp.apponlinetestserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class TestBlock extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Block block;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "testBlock",cascade = CascadeType.ALL)
    private List<TestWithScore> testWithScores;
}
