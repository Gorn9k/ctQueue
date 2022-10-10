package VSTU.ctQueue.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@MappedSuperclass
@Data
@ApiModel
public class AbstractEntity {

    /**
     * ���������� �������������
     */
    @Id
    @ApiModelProperty(notes = "Auto generated ID (strategy = increment)", hidden = true, example = "1")
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

}
