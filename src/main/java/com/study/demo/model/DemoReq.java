package com.study.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoReq {
     
    @NotBlank(message="code不能为空")
    String code;
     
    @Length(max=10,message="name长度不能超过10")
    String name;
 
}