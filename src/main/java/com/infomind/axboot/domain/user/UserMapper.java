package com.infomind.axboot.domain.user;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserMapper extends MyBatisMapper {

    List<User> getUserList(HashMap map);

}
