package VSTU.ctQueue.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "reservations")
@Data
public class Reservation extends AbstractEntity {

    /**
     * ������ ������������, ������������������ �� ������ {@link Reservation}
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reservation")
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Entrant> entrants = new ArrayList<>();

    /**
     * ���� ���������� (yyyy-MM-dd HH:mm:ss)
     */
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(hidden = true)
    @Column(unique = true)
    private Date reservationDate;

    public Reservation() {
        super();
    }

    public Reservation(Date reservationDate) {
        super();
        this.reservationDate = reservationDate;
    }

}
