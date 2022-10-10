package VSTU.ctQueue.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@MappedSuperclass
@ApiModel
public class Person extends AbstractEntity {

    /**
     * ���
     */
    @Column(nullable = false, length = 50)
    @ApiModelProperty(notes = "max length = 50", required = true, example = "Ivan")
    private String firstname;

    /**
     * �������
     */
    @Column(nullable = false, length = 50)
    @ApiModelProperty(notes = "max length = 50", required = true, example = "Ivanovich")
    private String surname;

    /**
     * ��������
     */
    @Column(nullable = false, length = 50)
    @ApiModelProperty(notes = "max length = 50", required = true, example = "Ivanov")
    private String patronymic;

}
