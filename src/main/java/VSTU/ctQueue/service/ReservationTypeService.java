package VSTU.ctQueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.ReservationType;
import VSTU.ctQueue.repository.ReservationTypeRepository;

@Service
public class ReservationTypeService extends CrudImpl<ReservationType> {

    @Autowired
    public ReservationTypeService(ReservationTypeRepository repository) {
        super(repository);
    }

}
