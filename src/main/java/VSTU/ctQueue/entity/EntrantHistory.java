package VSTU.ctQueue.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.Data;

@Table(name = "entrants_history")
@Data
@Entity
public class EntrantHistory extends AbstractEntrant {

    /**
     * ����� ����������� �����
     */
    @Column(nullable = true, unique = false)
    @Email
    private String email;

    /**
     * ���������� �����
     */
    @Column(nullable = false, unique = false)
    private Long phone;

}
