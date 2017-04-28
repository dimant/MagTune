package com.dtodorov.magtune.protocol;

public class MagTuneParser implements IMagTuneParser {

    public Speed intToSpeed(int i)
    {
        switch(i)
        {
            case 0:
                return Speed.Slow;
            case 1:
                return Speed.Medium;
            case 2:
                return Speed.Fast;
        }

        return Speed.Slow;
    }

    public byte[] encodeRotation(Direction direction, Speed speed) {
        int dir = direction == Direction.Left ? 0 : 1;
        int rpm = 0;
        int steps = 0;
        String cmd;

        switch(speed)
        {
            case Slow:
                rpm = 60;
                steps = 5;
            case Medium:
                rpm = 120;
                steps = 10;
            case Fast:
                rpm = 240;
                steps = 20;
        }

        cmd = String.format("%d,%d,%d", dir, rpm, steps);

        return cmd.getBytes();
    }
}
