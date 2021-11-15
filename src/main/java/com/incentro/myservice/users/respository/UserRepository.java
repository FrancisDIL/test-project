package com.incentro.myservice.users.respository;

import com.incentro.myservice.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("select u from User u where UPPER(u.email) = UPPER(?1) and u.accountDeleted = false")
    Optional<User> findByEmail(String username);

    @Query("select u from User u where UPPER(u.email) = UPPER(?1) and u.accountDeleted = false")
    Optional<User> findByEmailAndAccountDeletedFalse(String email);

    @Query("select u from User u where u.id = ?1 and u.accountDeleted = false")
    Optional<User> findByIdAndAccountDeletedFalse(Long id);

    @Query("select u from User u where u.id in ?1 and u.accountDeleted = false")
    List<User> findByIdsAndAccountDeletedFalse(List<Long> ids);

    @Query("select u from User u where u.id = ?1")
    Optional<User> findById(Long id);

    @Query("select case when count(u) > 0 then true else false end from User u where UPPER(u.email) = UPPER(?1) and u.id <> ?2 and u.accountDeleted = false")
    boolean existsByEmailAndAccountDeletedFalse(String email, Long userId);

    @Query("select case when count(u) > 0 then true else false end from User u where UPPER(u.phoneNumber) = UPPER(?1) and u.id <> ?2 and u.accountDeleted = false")
    boolean existsByPhoneNumberAndAccountDeletedFalse(String phoneNumber, Long userId);

    @Query("select u from User u where u.accountDeleted = false order by u.id desc")
    List<User> getAllUsersAccountDeletedFalse();

    @Query("select u from User u where ?1 member of u.role and u.accountDeleted = false")
    List<User> getUsersByRole(User.Role role);
}
