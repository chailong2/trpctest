package com.jackie.trpc.service.impl;

import org.springframework.stereotype.Service;

import com.jackie.trpc.service.DataServiceAPI;
import com.tencent.trpc.spring.annotation.TRpcClient;

@Service
public class ProxyService {
    public static final String SERVICE_NAME = "com.jackie.trpc.DataService";

    @TRpcClient(id=SERVICE_NAME)
    private DataServiceAPI dataServiceAPI;

    public DataServiceAPI getData(){
        return dataServiceAPI;
    }
}
