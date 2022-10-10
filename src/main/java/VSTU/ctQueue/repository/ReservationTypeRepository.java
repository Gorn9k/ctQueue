package VSTU.ctQueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import VSTU.ctQueue.entity.ReservationType;

@Repository
public interface ReservationTypeRepository extends JpaRepository<ReservationType, Long> {

}
