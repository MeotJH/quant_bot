package com.server.quant_bot.quant.trend_following.service;

import com.server.quant_bot.comm.entity.BaseEntity;
import com.server.quant_bot.comm.security.util.SecurityUtil;
import com.server.quant_bot.korea.service.StockService;
import com.server.quant_bot.quant.trend_following.dto.TrendFollowDto;
import com.server.quant_bot.quant.trend_following.entity.TrendFollow;
import com.server.quant_bot.quant.trend_following.repository.TrendFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthTrendFollowingImpl implements AuthTrendFollowing{

    private final TrendFollowRepository trendFollowRepository;

    @Override
    public Optional<TrendFollow> save(TrendFollowDto dto) {
        TrendFollow entity = new TrendFollow().update(dto);

        return Optional.ofNullable(
                trendFollowRepository.save(entity)
        );
    }

    @Override
    public List<TrendFollowDto> findTrendDtoByUserId() {
        List<TrendFollow> trendFollows = trendFollowRepository.findAllByUserId(SecurityUtil.getUser().getUsername());

        List<TrendFollowDto> dtos = new ArrayList<>();
        for (TrendFollow each : trendFollows) {
            dtos.add(each.toDto());
        }
        return dtos;
    }
}
