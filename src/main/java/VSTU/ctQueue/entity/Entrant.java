package VSTU.ctQueue.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Table(name = "entrants")
@Entity
public class Entrant extends AbstractEntrant {

    /**
     * ����� ����������� �����
     */
    @Column(nullable = true, unique = true)
    @ApiModelProperty(notes = "Entrant email (have uniq)", required = false, example = "email@email.com")
    @Email
    private String email;

    /**
     * ���������� �����
     */
    @Column(nullable = false, unique = true)
    @ApiModelProperty(notes = "Entrant mobile phone (have uniq), max length = 20", required = true, example = "375291111111")
    private Long phone;

    public EntrantHistory castToEntrantHistory() {
        EntrantHistory entrantHistory = new EntrantHistory();
        entrantHistory.setFirstname(this.getFirstname());
        entrantHistory.setPatronymic(this.getPatronymic());
        entrantHistory.setSurname(this.getSurname());
        entrantHistory.setEmail(this.getEmail());
        entrantHistory.setPhone(this.getPhone());
        entrantHistory.setRegDate(this.getRegDate());
        entrantHistory.setReservation(this.getReservation());
        entrantHistory.setType(this.getType());
        return entrantHistory;
    };

}
