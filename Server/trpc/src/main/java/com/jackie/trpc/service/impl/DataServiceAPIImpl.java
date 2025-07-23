package com.jackie.trpc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jackie.trpc.service.DataServiceAPI;
import com.jackie.trpc.service.Mydata;
import com.jackie.trpc.service.ProcessDataRequest;
import com.tencent.trpc.core.rpc.RpcContext;
import com.tencent.trpc.core.rpc.anno.TRpcMethod;
import com.tencent.trpc.core.rpc.anno.TRpcService;

@TRpcService(name="equipment.DataService")
@Service
public class DataServiceAPIImpl implements DataServiceAPI {

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @Override
    @TRpcMethod(name="processData")
    public Mydata processData(RpcContext context,ProcessDataRequest request) {
       byte[] dataByte=redisTemplate.opsForValue().get(request.getRedisKey());
       Mydata mydata=null;
       try {
         mydata=Mydata.parseFrom(dataByte);
         /**
          * 数据处理模拟操作
          */
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return mydata;
    }
}
