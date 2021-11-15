package com.incentro.myservice.users.entity;

import com.incentro.myservice.application.entity.AbstractAuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user_password_reset", schema = "public", uniqueConstraints = {@UniqueConstraint(columnNames = {"token"})})
public class UserPasswordReset extends AbstractAuditableEntity {

    private static final long serialVersionUID = -488865007311336642L;

    @Basic
    @NotBlank
    @Column(name = "token", nullable = false)
    private String token;

    @Basic
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    @ManyToOne(optional = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPasswordReset that = (UserPasswordReset) o;

        if (id != that.id) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (expiryDate != null ? !expiryDate.equals(that.expiryDate) : that.expiryDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
        return result;
    }
}
