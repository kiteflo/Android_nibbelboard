package com.jooik.nibbelboard.com.jooik.nibbelboard.domain;

/**
 * A simple data structure in order to group several sound related properties.
 * It's much easier to pass around a holder instead of passing each property as
 * a method parameter...
 */
public class SoundHolder
{
    private int soundID;
    private String soundFile;
    private String soundLabel;
    private String soundImageFile;

    public String getSoundFile()
    {
        return soundFile;
    }

    public void setSoundFile(String soundFile)
    {
        this.soundFile = soundFile;
    }

    public String getSoundLabel()
    {
        return soundLabel;
    }

    public void setSoundLabel(String soundLabel)
    {
        this.soundLabel = soundLabel;
    }

    public String getSoundImageFile()
    {
        return soundImageFile;
    }

    public void setSoundImageFile(String soundImageFile)
    {
        this.soundImageFile = soundImageFile;
    }

    public int getSoundID()
    {
        return soundID;
    }

    public void setSoundID(int soundID)
    {
        this.soundID = soundID;
    }
}
