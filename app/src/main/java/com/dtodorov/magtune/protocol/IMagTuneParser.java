package com.dtodorov.magtune.protocol;

/**
 * Created by ditodoro on 4/27/2017.
 */

public interface IMagTuneParser {
    public enum Direction {
        Left,
        Right
    }

    public enum Speed {
        Slow,
        Medium,
        Fast
    }

    byte[] encodeRotation(Direction direction, Speed speed);
    Speed intToSpeed(int i);
}
