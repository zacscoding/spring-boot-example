package org.zerock.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author zacconding
 * @Date 2017-12-23
 * @GitHub : https://github.com/zacscoding
 */
public interface CustomWebBoard {
    public Page<Object[]> getCustomPage(String type, String keyword, Pageable pageable);

}
