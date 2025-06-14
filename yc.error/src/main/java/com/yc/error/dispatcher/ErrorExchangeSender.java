package com.yc.error.dispatcher;

public interface ErrorExchangeSender
{
    public void registerError(String queueName, String message);

    public void registerPlatformError(String message);

    public void registerApplicationError(String message);

    public void notifyQueryModel(String exchange, String keyRouting, String message);
}
