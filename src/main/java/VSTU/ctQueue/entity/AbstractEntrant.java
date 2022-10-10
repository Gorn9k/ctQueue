package VSTU.ctQueue.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@MappedSuperclass
@ApiModel
public class AbstractEntrant extends Person {

    /**
     * 
     * TYPE_MAIN = 1l
     */
    @Transient
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    public static final Long TYPE_MAIN = 1l;

    /**
     * TYPE_RESERVE = 2l
     */
    @Transient
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    public static final Long TYPE_RESERVE = 2l;

    /**
     * ���� �����������
     */
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(notes = "Registered date", hidden = true)
    @Column(nullable = false)
    @JsonIgnore
    private Date regDate;

    /**
     * {@link Reservation}
     */
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /**
     * ��� ���������, ���������� �� ����� ����������� �����
     */
    @Column(unique = true)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String activationCode;

    /**
     * {@link ReservationType}
     */
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @JsonIgnore
    private ReservationType type = new ReservationType();

    public AbstractEntrant() {
        super();
        this.regDate = new Date();
        this.type.setId(TYPE_MAIN);
    }

    public Boolean isMain() {
        return this.getType().getId() == Entrant.TYPE_MAIN ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean isReserve() {
        return this.getType().getId() == Entrant.TYPE_RESERVE ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public String toString() {
        return "Entrant [regDate=" + regDate + ", reservation=" + reservation.getReservationDate() + ", activationCode="
                + activationCode + ", type=" + type.getId() + "]";
    }
}
