package com.server.quant_bot.quant.trend_following.mapper;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrendFollowMapper {

    TrendFollowMapper INSTANCE = Mappers.getMapper(TrendFollowMapper.class);
    TrendFollow DtoToEntity(TrendFollowDto dto);
    TrendFollowDto EntityToDto(TrendFollow entity);
}
