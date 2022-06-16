package com.example.trains.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "registered_users")
public class RegisteredUser implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long registered_user_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    private String mail;
    private String password;
    private String recoveryCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "registered_users_roles",
            joinColumns = @JoinColumn(name = "registered_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString(){
        return registered_user_id.toString() + " " + mail + " " + password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegisteredUser that = (RegisteredUser) o;
        return registered_user_id != null && Objects.equals(registered_user_id, that.registered_user_id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


