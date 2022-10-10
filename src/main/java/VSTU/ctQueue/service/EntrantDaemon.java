package VSTU.ctQueue.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EntrantDaemon {

    private static Logger log = LoggerFactory.getLogger(EntrantDaemon.class);

    @Autowired
    private EntrantService service;

    /**
     * �������� ������������ ��� ��������������� ������������ ������ ������ N
     * �������� ������.
     */
    @Scheduled(fixedDelayString = "${entrant.unaprove.delete.delay}000")
    public void deleteUnaproveEntrant() {
        service.deleteUnapproveExpired();
        log.info("Unapprove entrants are removed.");
    }

    /**
     * ����������� � ������� ���� ������������, ��� ����� ���������� ��� ������
     * ������ N �������� ������
     */
    @Scheduled(fixedDelayString = "${entrant.history.transfer.delay}000")
    public void entrantsTransferToHisotry() {
        service.entrantsTransferToHisotry();
        log.info("Visited entrants are transferred to history.");
    }
}
