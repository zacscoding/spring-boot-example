package org.zerock.persistence;

import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Profile;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public interface ProfileRepository extends CrudRepository<Profile,Long>{

}
