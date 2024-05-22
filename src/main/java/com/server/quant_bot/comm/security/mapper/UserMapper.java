package com.server.quant_bot.comm.security.mapper;

import com.server.quant_bot.comm.security.dto.UserDto;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.view.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserEntity userDTOToEntity(UserDto userDto);
    UserDto userEntityToDto(UserEntity userEntity);
    UserView userEntityToView(UserEntity userEntity);
}
