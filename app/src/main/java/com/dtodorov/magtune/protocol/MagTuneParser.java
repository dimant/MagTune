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
        int dir = direction == Direction.Left ? 0 : 1;
        int rpm = 0;
        int steps = 0;
        String cmd;

        switch(speed)
        {
            case Slow:
                rpm = 60;
                steps = 5;
                break;
            case Medium:
                rpm = 120;
                steps = 10;
                break;
            case Fast:
                rpm = 240;
                steps = 20;
                break;
        }

        cmd = String.format("%d,%d,%d\r\n", dir, rpm, steps);

        return cmd.getBytes();
    }
}
