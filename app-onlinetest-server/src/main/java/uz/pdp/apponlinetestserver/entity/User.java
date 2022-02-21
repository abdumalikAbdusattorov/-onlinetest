package uz.pdp.apponlinetestserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.apponlinetestserver.bot.BotState;
import uz.pdp.apponlinetestserver.entity.enums.Level;
import uz.pdp.apponlinetestserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Users")
public class User extends AbsEntity implements UserDetails {
    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;


    private String emailCode;

    private String password;

    private String choosenLevel;

    private Integer choosenBlokId; //

    private UUID choosenTestBlokId;

    private Integer testStep;

    private Integer questionStep;

    @Column()
    private String firstName;

    @Column()
    private String lastName;


    private Integer telegramChatId;


    private String botState;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;


    public User(String phoneNumber, String email, String password, String firstName, String lastName, List<Role> roles) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
