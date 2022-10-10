package VSTU.ctQueue.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "reservation_types")
@Data
public class ReservationType extends AbstractEntity {

    /**
     * ��� ����
     */
    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "type")
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Entrant> entrants;

}
