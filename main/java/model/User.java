package model;

import lombok.Getter;
import lombok.Setter;
import util.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(columnDefinition = "varchar(25) not null unique")
    private String username;

    private String password;

    private String securityAnswer;

    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }
}
