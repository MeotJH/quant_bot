package com.server.quant_bot.batch.trend_following;

public interface TrendFollowBatch {

    default void checkNotificationStatus(){}

    default void sendNotification(){}
}
