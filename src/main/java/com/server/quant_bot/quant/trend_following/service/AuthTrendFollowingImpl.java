package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.korea.entity.Stock;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowEntityLikeDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.mapper.EntityLikeToResponseMapper;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Service
@RequiredArgsConstructor
public class AuthTrendFollowingImpl implements AuthTrendFollowing{

    private final TrendFollowRepository trendFollowRepository;
    private final UserService userService;
    private final StockService stockService;

    @Override
    public Optional<TrendFollow> save(TrendFollowDto requestDto) {

        Optional<TrendFollowEntityLikeDto> dto = transformRequestDtoToEntityLikeDto(requestDto);

        AtomicReference<TrendFollow> entity = new AtomicReference<TrendFollow>();
        dto.ifPresentOrElse(
                present -> entity.set(new TrendFollow().update(present))
                ,() ->{throw new ResourceCommException(" 데이터에 문제가 발생했습니다. ");}
        );

        return Optional.ofNullable(
                trendFollowRepository.save(entity.get())
        );
    }

    @Override
    public List<TrendFollowDto> findTrendDtoByUserId() {
        List<TrendFollow> trendFollows = trendFollowRepository.findAllByUser(findUserBySecurity().get());

        List<TrendFollowDto> dtos = new ArrayList<>();
        for (TrendFollow each : trendFollows) {

            TrendFollowEntityLikeDto dto = each.toDto();

            //TODO 이부분 캡슐화 하기
            TrendFollowDto trendFollowDto = EntityLikeToResponseMapper.INSTANCE.EntityLikeToDto(dto);
            dtos.add(trendFollowDto);

        }

        return dtos;
    }

    private Optional<TrendFollowEntityLikeDto> transformRequestDtoToEntityLikeDto(TrendFollowDto requestDto) {
        return Optional.ofNullable(
                new TrendFollowEntityLikeDto(
                        findUserBySecurity().get()
                        ,findStockByStockCode(requestDto.getStock()).get()
                        ,requestDto.getTrendFollowPrice()
                        ,requestDto.getIsBuy()
                        ,requestDto.getBaseDateClosePrice()
                )
        );
    }

    private Optional<Stock> findStockByStockCode(String stockCode){
        if(stockCode == null){
            throw new ResourceCommException("stock이 존재하지 않습니다");
        }
        return stockService.findStockByStockCode(stockCode);
    }

    private Optional<UserEntity> findUserBySecurity() {
        return userService.findUserByLoginId();
    }
}
