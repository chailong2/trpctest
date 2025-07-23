package com.jackie.trpc.controller;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.nodes.NodeId;
import com.google.protobuf.util.JsonFormat;
import com.jackie.trpc.common.SnowflakeIdGenerator;
import com.jackie.trpc.service.Mydata;
import com.jackie.trpc.service.ProcessDataRequest;
import com.jackie.trpc.service.ProcessDataRequest.Builder;
import com.jackie.trpc.service.impl.ProxyService;
import com.tencent.trpc.core.rpc.RpcClientContext;
import com.tencent.trpc.core.rpc.RpcContext;

@RestController
@RequestMapping("/")
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Autowired
    private RedisTemplate<String, byte[]>  redisTemplate;

    @Autowired
    private SnowflakeIdGenerator idGenerator;

    private static final String MYDATA_KEY_PREFIX = "mydata:";


    @RequestMapping("/process")
    public Mydata sayHello(@RequestBody String jsonData) {
        Mydata mydata =null;
        try {
            Mydata.Builder builder = Mydata.newBuilder();
            JsonFormat.parser().merge(jsonData, builder);
             mydata = builder.build();
            // 转换为字节数组（Protobuf序列化）
            // 使用业务相关的Redis Key
            long snowflakeId = idGenerator.nextId();
            String key = MYDATA_KEY_PREFIX + snowflakeId;
            redisTemplate.opsForValue().set(key, mydata.toByteArray());
            RpcContext ctx = new RpcClientContext();
            ProcessDataRequest param = ProcessDataRequest.newBuilder().setRedisKey(key).build();
            proxyService.getData().processData(ctx, param);
        } catch (Exception e) {
            // 统一异常处理
            throw new RuntimeException("Redis operation failed", e);
        }
        return mydata;
    }
}
