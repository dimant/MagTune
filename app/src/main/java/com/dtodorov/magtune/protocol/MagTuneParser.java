package com.dtodorov.magtune.protocol;

public class MagTuneParser implements IMagTuneParser {

    public Speed intToSpeed(int i)
    {
        Speed speed = Speed.Slow;

        switch(i)
        {
            case 0:
                speed = Speed.Slow;
                break;
            case 1:
                speed = Speed.Medium;
                break;
            case 2:
                speed = Speed.Fast;
                break;
        }

        return speed;
    }

    public byte[] encodeRotation(Direction direction, Speed speed) {
        int dir = direction == Direction.Left ? -1 : 1;
        int rpm = 0;
        int steps = 0;
        String cmd;

        switch(speed)
        {
            case Slow:
                rpm = 15;
                steps = 15 * dir;
                break;
            case Medium:
                rpm = 30;
                steps = 30 * dir;
                break;
            case Fast:
                rpm = 60;
                steps = 60 * dir;
                break;
        }

        cmd = String.format("0,%d,%d\r\n", rpm, steps);

        return cmd.getBytes();
    }
}
