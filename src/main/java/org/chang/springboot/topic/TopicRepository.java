package org.chang.springboot.topic;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Long> {
  @Query("select new org.chang.springboot.topic.TopicDto(t.id, t.name) "
      + "from Topic t where t.name like :subs% ")
  List<TopicDto> findByNameStartingWith(String subs);
}
