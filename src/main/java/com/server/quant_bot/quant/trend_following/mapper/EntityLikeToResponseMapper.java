package com.server.quant_bot.quant.trend_following.mapper;

import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowEntityLikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityLikeToResponseMapper {

    EntityLikeToResponseMapper INSTANCE = Mappers.getMapper(EntityLikeToResponseMapper.class);

    @Mapping(source="stock", target="stock.stockName", ignore = true)
    @Mapping(source="approval", target="notification.approval", ignore = true)
    TrendFollowEntityLikeDto DtoToEntityLike(TrendFollowDto dto);

    @Mapping(source="stock", target="stock")
    @Mapping(source="stock.stockName", target="stockName")
    @Mapping(source = "stock.market", target="market")
    @Mapping(source = "notification.approval", target="approval")
    TrendFollowDto EntityLikeToDto(TrendFollowEntityLikeDto entityLikeDto);
}
