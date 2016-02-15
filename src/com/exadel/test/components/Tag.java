package com.exadel.test.components;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Tag {
    private byte[] tagInfo;
    private String tagSignature;
    private int offsetToTagData;
    private int tagDataSize;

    public Tag(byte[] tagInfo) {
        this.tagInfo = tagInfo.clone();
    }

    public void setTagSignature(String tagSignature) {
        this.tagSignature = tagSignature;
    }

    public void setOffsetToTagData() {
        byte[] offsetToTagDataArr = Arrays.copyOfRange(tagInfo, 4, 8);
        offsetToTagData = ByteBuffer.wrap(offsetToTagDataArr).getInt();
    }

    public void setTagDataSize() {
        byte[] tagDataSizeArr = Arrays.copyOfRange(tagInfo, 8, 12);
        tagDataSize = ByteBuffer.wrap(tagDataSizeArr).getInt();
    }

    public String getTagSignature() {
        return tagSignature;
    }

    public int getOffsetToTagData() {
        return offsetToTagData;
    }

    public int getTagDataSize() {
        return tagDataSize;
    }
}
