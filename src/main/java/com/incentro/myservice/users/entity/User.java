package com.incentro.myservice.users.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.incentro.myservice.application.exception.AppInputErrorException;
import com.incentro.myservice.users.dto.UserResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    private static final long serialVersionUID = 6773397465832675864L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id")
    private User createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by_user_id", referencedColumnName = "id")
    private User lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "account_deleted")
    private Boolean accountDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_deleted_by_user_id", referencedColumnName = "id")
    private User accountDeletedBy;

    @Column(name = "account_deleted_date")
    private LocalDateTime accountDeletedDate;

    @Basic
    @Column(name = "first_name", nullable = false, length = 200)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 200)
    private String lastName;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;

    @Basic
    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    @CollectionTable(name = "user_roles", schema = "public")
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> role;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        getRole().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    public void grantAuthority(Role authority) {
        if (role == null) role = new HashSet<>();
        role.add(authority);
    }

    public List<Role> getRole() {
        List<Role> roles = role.stream().collect(Collectors.toList());
        Collections.sort(roles, (a1, a2) -> Integer.compare(a1.getHierarchyLevel(), a2.getHierarchyLevel()));
        return roles;
    }

    public String getFullName() {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }

    public User() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public void addRole(Role role) {
        if (this.role == null) {
            this.role = new HashSet<>();
        }
        this.role.add(role);
        this.touch();
    }

    public void addRoles(Set<Role> roles) {
        if (this.role == null) {
            this.role = new HashSet<>();
        }
        if (roles != null) {
            roles.stream().forEach(role -> this.addRole(role));
        }
        this.touch();
    }

    public void updateRoles(Set<Role> roles) {
        if (this.role != null) {
            if (roles != null) {
                List<Role> deleteList = this.role.stream().filter(label -> {
                    for (Role role : roles) {
                        if (label.equals(role)) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
                this.role.removeAll(deleteList);
                roles.forEach(role -> {
                    addRole(role);
                });
            } else {
                this.role.clear();
            }
            this.touch();
        }
    }

    public void touch() {
        this.lastModifiedDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum Role implements GrantedAuthority {
        LIBRARIAN(Code.LIBRARIAN, 1),
        MEMBER(Code.MEMBER, 2);

        private String name;
        private int hierarchyLevel;

        Role(String name, int hierarchyLevel) {
            this.name = name;
            this.hierarchyLevel = hierarchyLevel;
        }

        public int getHierarchyLevel() {
            return hierarchyLevel;
        }

        @Override
        @JsonValue
        public String getAuthority() {
            return name;
        }

        @JsonCreator
        public static Role forValue(String value) {
            switch (value.toUpperCase()) {
                case Code.LIBRARIAN:
                case "LIBRARIAN":
                    return LIBRARIAN;
                case Code.MEMBER:
                case "MEMBER":
                    return MEMBER;
                default:
                    throw new AppInputErrorException("user.role.invalid", value);
            }
        }

        public static class Code {
            private static final String LIBRARIAN = "Librarian";
            private static final String MEMBER = "Member";
        }
    }

    public UserResponseDTO toUserResponse() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(this.id);
        userResponseDTO.setFirstName(this.firstName);
        userResponseDTO.setLastName(this.lastName);
        userResponseDTO.setPhoneNumber(this.phoneNumber);
        userResponseDTO.setEmail(this.email);
        userResponseDTO.setActive(this.enabled);
        userResponseDTO.setRole(this.getRole().get(0));

        return userResponseDTO;
    }
}
