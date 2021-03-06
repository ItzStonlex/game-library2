package org.stonlexx.gamelibrary.utility.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class HeadLocation implements Cloneable, Serializable {

    private float yaw, pitch;

    public HeadLocation add(float yaw, float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;

        return this;
    }

    public HeadLocation add(HeadLocation headLocation) {
        return add(headLocation.yaw, headLocation.pitch);
    }

    public HeadLocation subtract(float yaw, float pitch) {
        return add(-yaw, -pitch);
    }

    public HeadLocation subtract(HeadLocation headLocation) {
        return add(-headLocation.yaw, -headLocation.pitch);
    }

    @Override
    public HeadLocation clone() {
        return new HeadLocation(yaw, pitch);
    }

}
