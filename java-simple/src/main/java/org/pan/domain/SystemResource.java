package org.pan.domain;

import lombok.Data;

/**
 * Created by panmingzhi on 2017/5/13.
 */
@Data
public class SystemResource extends DomainObject {
    private String resourceName;
    private String resourceUrl;
    private String resourceIcon;
    private Integer resourceIndex;
    private Long resourceParent;
}
