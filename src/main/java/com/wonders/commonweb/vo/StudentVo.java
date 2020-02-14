package com.wonders.commonweb.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Author : wuzhiheng
 * @Description :
 * @Date Created in 09:05 2020-02-14
 */
@Data
@Accessors(chain = true)
public class StudentVo implements Serializable {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @Range(min = 0,max = 100,message = "年龄区间应在0-100")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @Pattern(regexp = "^\\d{11}$",message = "手机号码不正确")
    private String phone;


}
