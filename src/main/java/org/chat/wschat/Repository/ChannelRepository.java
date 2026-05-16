package org.chat.wschat.Repository;

import org.chat.wschat.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

    Optional<Channel> findByName(String name);
    List<Channel> findByType(Channel.ChannelType type);

    @Query("select c from Channel c where c.type = 'PUBLIC' or :username member of c.members")
    List<Channel> findAccessibleChannels(@Param("username") String username);
    boolean existsByName(String name);
}
