package org.pan.domain;

import lombok.Data;

/**
 * Created by panmingzhi on 2017/5/3.
 */
@Data
public class SystemAccount extends DomainObject {
    private String username;
    private String password;

}
