package org.chat.wschat.Repository;

import org.chat.wschat.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteRepository extends JpaRepository<Invite, String> {
    @Query("select i from Invite i where i.id = :id and i.toUser = :toUser")
    Optional<Invite> findByIdAndToUser(@Param("id") String id, @Param("toUser") String toUser);
}
