package com.server.quant_bot.quant.trend_following.mapper;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowEntityLikeDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrendFollowEnntityLikeMapper {

    TrendFollowEnntityLikeMapper INSTANCE = Mappers.getMapper(TrendFollowEnntityLikeMapper.class);
    TrendFollow DtoToEntity(TrendFollowEntityLikeDto dto);
    TrendFollowEntityLikeDto EntityToDto(TrendFollow entity);
}
