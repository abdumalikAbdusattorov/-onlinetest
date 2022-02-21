package uz.pdp.apponlinetestserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Block extends AbsNameEntity {

    @Enumerated(EnumType.STRING)
    private Level level;
}
