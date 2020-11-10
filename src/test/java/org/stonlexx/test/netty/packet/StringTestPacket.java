package org.stonlexx.test.netty.packet;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.packet.impl.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.utility.location.PointLocation;

public class StringTestPacket extends AbstractNettyPacket {

    @Override
    public void writePacket(NettyPacketBuffer packetBuffer) {
        packetBuffer.writeString("tEst l1ne");

        handleData.addHandleData("location", new PointLocation(2.1, 2.2, 8));
    }

    @Override
    public void readPacket(NettyPacketBuffer packetBuffer) {
        String testLine = packetBuffer.readString();

        GameLibrary.getInstance().getLogger().info("Packet response: " + testLine);
    }

    @Override
    public void handle(Channel channel) {
        PointLocation pointLocation = handleData.getHandleDataObject(PointLocation.class, "location");

        GameLibrary.getInstance().getLogger().info("Packet handle data value: " + pointLocation.toString());
    }

}
