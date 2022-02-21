package uz.pdp.apponlinetestserver.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.pdp.apponlinetestserver.entity.Role;

@Projection(name = "customRole", types = {Role.class})
public interface CustomRole {
    Integer getId();
    String getName();

}
