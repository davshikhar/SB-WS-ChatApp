package org.chat.wschat.Repository;

import org.chat.wschat.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteRepository extends JpaRepository<Invite, String> {
    Optional<Invite> findByIdAndToUsername(String id, String toUsername);
}
