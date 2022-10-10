package VSTU.ctQueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import VSTU.ctQueue.entity.EntrantHistory;

@Repository
public interface EntrantHistoryRepository extends JpaRepository<EntrantHistory, Long> {

}
