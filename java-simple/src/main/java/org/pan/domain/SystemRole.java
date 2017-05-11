package org.pan.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by panmingzhi on 2017/5/9.
 */
@Data
public class SystemRole extends DomainObject {

    private String roleName;
    private Date updateTime;

}
