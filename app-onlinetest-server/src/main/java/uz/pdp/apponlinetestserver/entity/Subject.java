package uz.pdp.apponlinetestserver.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.apponlinetestserver.entity.template.AbsNameEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Subject extends AbsNameEntity {
}
