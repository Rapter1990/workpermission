package com.example.workpermission.auth.model.mapper;

import com.example.workpermission.auth.model.User;
import com.example.workpermission.auth.model.entity.UserEntity;
import com.example.workpermission.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityToUserMapper extends BaseMapper<UserEntity, User> {

    @Override
    User map(UserEntity source);

    static UserEntityToUserMapper initialize() {
        return Mappers.getMapper(UserEntityToUserMapper.class);
    }

}
