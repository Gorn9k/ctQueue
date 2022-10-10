package VSTU.ctQueue.model;

import lombok.Data;

@Data
public class ReservationGenerateModel {

    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private Integer interval;

}
