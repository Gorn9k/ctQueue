package VSTU.ctQueue.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role extends AbstractEntity {

    /**
     * ��� ����
     */
    @Column(unique = true)
    private String name;

    /**
     * ������ �������������, � ������� ��������� ������ ����
     */
    @ManyToMany(mappedBy = "roleList", fetch = FetchType.EAGER)
    private List<User> user;

}
