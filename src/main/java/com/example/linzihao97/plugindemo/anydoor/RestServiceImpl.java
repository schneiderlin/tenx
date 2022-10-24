//package com.example.linzihao97.plugindemo.anydoor;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.QueryStringDecoder;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.ide.RestService;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//
//public class RestServiceImpl extends RestService {
//
//    @Override
//    public boolean isSupported(@NotNull FullHttpRequest request) {
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public String execute(@NotNull QueryStringDecoder queryStringDecoder, @NotNull FullHttpRequest fullHttpRequest, @NotNull ChannelHandlerContext channelHandlerContext) throws IOException {
//        System.out.println("lgp xxxxxxx");
//        return null;
//    }
//
//    @NotNull
//    @Override
//    protected String getServiceName() {
//        return "RestServiceLgp";
//    }
//
//    @Override
//    protected boolean isHostTrusted(@NotNull FullHttpRequest request, @NotNull QueryStringDecoder urlDecoder) throws InterruptedException, InvocationTargetException {
//        return true;
//    }
//}
