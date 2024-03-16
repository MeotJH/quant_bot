package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowListDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowUserPageDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;

import java.util.List;
import java.util.Optional;

public interface TrendFollowing {

    default TrendFollowDto getOneday(String ticker, String baseDt){return null;}
    default TrendFollowDto getOneday(Stock stock){return null;}

    default TrendFollowListDto getDays(String ticker, String baseDt){return null;}

    default List<String> getStocksByKeyword(String keyword){return null;} // 이건 논리적으로 역할이 안맞는 메소드 같은데..
    //TODO entity return 하면 안됨 수정하자
    default Optional<TrendFollowDto> save(TrendFollowDto dto) {return Optional.ofNullable(null);}

    default List<TrendFollowUserPageDto> findTrendDtoByUserId(){return null;}

    default Optional<TrendFollow> findByStock(Stock stock){return null;}
}
