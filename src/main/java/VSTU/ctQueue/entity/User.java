package VSTU.ctQueue.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User extends Person {

    /**
     * ��� ������������
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * ������
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * ������ ����� ������������
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "userID") }, inverseJoinColumns = {
            @JoinColumn(name = "roleID") }, uniqueConstraints = {
                    @UniqueConstraint(columnNames = { "userID", "roleID" }) })
    @JsonIgnore
    private List<Role> roleList;

    /**
     * ������ ������. ������ ���� ������������ ���� ��� �����������.
     */
    @Transient
    @JsonIgnore
    private String rPassword;

    /**
     * ��� �������������. ������ ���� ������������ ���� ��� �����������.
     */
    @Transient
    @JsonIgnore
    private String verificationCode;

}
