package org.stonlexx.gamelibrary.core.netty.handler.server.active.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.handler.server.active.AbstractNettyServerActive;

public class NettyEmptyServerActive extends AbstractNettyServerActive {

    @Override
    public void onServerActive(@NonNull Channel serverChannel) { }

}
