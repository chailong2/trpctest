package com.jackie.trpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.tencent.trpc.spring.boot.starters.annotation.EnableTRpc;

@EnableTRpc
@SpringBootApplication
public class TrpcApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrpcApplication.class, args);
    }
}
