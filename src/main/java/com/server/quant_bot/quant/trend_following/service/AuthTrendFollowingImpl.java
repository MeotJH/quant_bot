package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.comm.exception.ResourceCommException;
import com.server.quant_bot.comm.security.entity.UserEntity;
import com.server.quant_bot.comm.security.service.UserService;
import com.server.quant_bot.stock.entity.Stock;
import com.server.quant_bot.stock.service.StockService;
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
    public Optional<TrendFollowDto> save(TrendFollowDto requestDto) {
        
        isDuplicate(requestDto);

        Optional<TrendFollowEntityLikeDto> dto = transformRequestDtoToEntityLikeDto(requestDto);

        AtomicReference<TrendFollow> reference = new AtomicReference<>();
        dto.ifPresentOrElse(
                present -> reference.set(new TrendFollow().update(present))
                ,() ->{throw new ResourceCommException(" 데이터에 문제가 발생했습니다. ");}
        );

        TrendFollow save = trendFollowRepository.save(reference.get());
        return Optional.ofNullable(
                EntityLikeToResponseMapper.INSTANCE.EntityLikeToDto(save.toDto())
        );
    }

    private void isDuplicate(TrendFollowDto requestDto) {
        Boolean isExist = Boolean.FALSE;
        Optional<UserEntity> userByLoginId = userService.findUserByLoginId();
        Optional<Stock> stockByStockCode = stockService.findStockByStockCode(requestDto.getStock().getStockCode());

        if(userByLoginId.isPresent() && stockByStockCode.isPresent()){
            isExist = trendFollowRepository.existsByStockAndIsBuyAndUser(stockByStockCode.get(), requestDto.getIsBuy(), userByLoginId.get());
        }

        if(isExist == true){
            throw new ResourceCommException("중복된 데이터가 있습니다.");
        }
    }

    @Override
    public List<TrendFollowDto> findTrendDtoByUserId() {
        List<TrendFollow> trendFollows = trendFollowRepository.findAllByUser(findUserBySecurity().get());

        List<TrendFollowDto> dtos = new ArrayList<>();
        for (TrendFollow each : trendFollows) {

            TrendFollowEntityLikeDto dto = each.toDto();
            TrendFollowDto trendFollowDto = EntityLikeToResponseMapper.INSTANCE.EntityLikeToDto(dto);
            dtos.add(trendFollowDto);

        }

        return dtos;
    }

    @Override
    public Optional<TrendFollow> findByStock(Stock stock) {
        return trendFollowRepository.findByStockAndUser(stock,findUserBySecurity().get());
    }

    private Optional<TrendFollowEntityLikeDto> transformRequestDtoToEntityLikeDto(TrendFollowDto requestDto) {
        return Optional.ofNullable(
                new TrendFollowEntityLikeDto(
                        findUserBySecurity().get()
                        ,findStockByStockCode(requestDto.getStock().getStockCode()).get()
                        ,requestDto.getTrendFollowPrice()
                        ,requestDto.getIsBuy()
                        ,requestDto.getBaseDateClosePrice()
                        ,null
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
