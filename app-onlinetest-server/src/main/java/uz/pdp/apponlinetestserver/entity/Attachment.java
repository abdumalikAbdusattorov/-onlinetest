package uz.pdp.apponlinetestserver.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.apponlinetestserver.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Attachment extends AbsEntity {
    private String name;
    private String contentType;
    private long size;


    public Attachment(String name, String contentType, long size) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
    }
}
