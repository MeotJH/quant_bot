package com.server.quant_bot.stock.mapper;


import com.server.quant_bot.stock.dto.CoinDto;
import com.server.quant_bot.stock.entity.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoinMapper {

    CoinMapper INSTANCE = Mappers.getMapper(CoinMapper.class);
    Coin DtoToEntity(CoinDto.CoinDetail dto);
    CoinDto.CoinDetail EntityToDto(Coin entity);
}
