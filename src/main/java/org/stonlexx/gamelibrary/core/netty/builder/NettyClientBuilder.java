package org.stonlexx.gamelibrary.core.netty.builder;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyClientBuilder<K> {

    private final InetSocketAddress inetSocketAddress;
    private final Class<K> packetKeyClass;

    private NettyPacketTyping<K> nettyPacketTyping;
    private ChannelFutureListener channelFutureListener;
    private ChannelInitializer<NioSocketChannel> channelInitializer;

    private Object[] bootstrapAttributes = new Object[0];


    private final NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
    private final NettyBootstrap nettyBootstrap = nettyManager.getNettyBootstrap();


// ================================================================================================================== //

    public static <K> NettyClientBuilder<K> newServerBuilder(@NonNull String serverHost,
                                                             @NonNull int serverPort,

                                                             @NonNull Class<K> packetKeyClass) {
        InetSocketAddress inetSocketAddress
                = InetSocketAddress.createUnresolved(serverHost, serverPort);

        return newServerBuilder(inetSocketAddress, packetKeyClass);
    }


    public static <K> NettyClientBuilder<K> newServerBuilder(@NonNull InetSocketAddress inetSocketAddress,
                                                             @NonNull Class<K> packetKeyClass) {

        NettyClientBuilder<K> nettyServerBuilder = new NettyClientBuilder<>(inetSocketAddress, packetKeyClass);
        nettyServerBuilder.nettyPacketTyping = NettyPacketTyping.getPacketTyping(packetKeyClass, RandomStringUtils.randomAlphabetic(16));

        return nettyServerBuilder;
    }

// ================================================================================================================== //


    public NettyClientBuilder<K> acceptPacketTyping(@NonNull Consumer<NettyPacketTyping<K>> packetTypingConsumer) {

        packetTypingConsumer.accept(nettyPacketTyping);
        return this;
    }

    public NettyClientBuilder<K> registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,

                                                @NonNull Class<? extends NettyPacket> nettyPacketClass,
                                                @NonNull K packetKeyId) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass, packetKeyId);
        return this;
    }

    public NettyClientBuilder<K> registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                                                @NonNull Class<? extends NettyPacket> nettyPacketClass) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass);
        return this;
    }

    public NettyClientBuilder<K> bootstrapAttributes(@NonNull Object... bootstrapAttributes) {

        this.bootstrapAttributes = bootstrapAttributes;
        return this;
    }

    public NettyClientBuilder<K> futureListener(@NonNull BiConsumer<ChannelFuture, Boolean> resultConsumer) {
        return futureListener(nettyBootstrap.createFutureListener(resultConsumer));
    }

    public NettyClientBuilder<K> futureListener(@NonNull ChannelFutureListener channelFutureListener) {

        this.channelFutureListener = channelFutureListener;
        return this;
    }

    public NettyClientBuilder<K> channelInitializer(Consumer<NioSocketChannel> channelConsumer) {
        return channelInitializer(nettyBootstrap.createChannelInitializer(channelConsumer));
    }

    public NettyClientBuilder<K> channelInitializer(@NonNull ChannelInitializer<NioSocketChannel> channelInitializer) {

        this.channelInitializer = channelInitializer;
        return this;
    }


    public void connectToServer() {
        nettyBootstrap.createClientBootstrap(inetSocketAddress, channelFutureListener, channelInitializer, bootstrapAttributes);
    }

}
