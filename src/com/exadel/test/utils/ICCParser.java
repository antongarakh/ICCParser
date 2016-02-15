package com.exadel.test.utils;

import com.exadel.test.components.Tag;
import com.exadel.test.components.TaskDescription;
import com.exadel.test.exceptions.ParserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.ByteBuffer;

public class ICCParser {
    private byte[] iccProfile;
    private byte[] iccHeader;
    private byte[] colourSpace;
    private byte[] profileDescriptionTagData;
    private static final String RGB = "52474220";
    private static final String GRAY = "47524159";
    private static final String CMYK = "434D594B";
    private static final String DESC = "64657363";
    private Tag profileDescriptionTag;
    private TaskDescription taskDescription;

    public ICCParser(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        iccProfile = Files.readAllBytes(path);
    }

    private void divideHeader() {
        iccHeader = Arrays.copyOfRange(iccProfile, 0, 128);
    }

    private void divideColourSpace() {
        colourSpace = Arrays.copyOfRange(iccHeader, 16, 20);
    }

    private String colourSpaceToHex() {
        return DatatypeConverter.printHexBinary(colourSpace);
    }

    private String getColourSpaceType() {
        switch (colourSpaceToHex()) {
            case RGB:
                return "RGB";
            case CMYK:
                return "CMYK";
            case GRAY:
                return "GRAY";
            default:
                return "Colour Space Type of this ICC File is not provided in the task given";
        }
    }

    private void divideProfileDescriptionTag() {
        int count = ByteBuffer.wrap(Arrays.copyOfRange(iccProfile, 128, 132)).getInt();
        byte[] tagTable = Arrays.copyOfRange(iccProfile, 132, 132 + 12 * count);
        for (int i = 0; i < count; i++) {
            byte[] tag = Arrays.copyOfRange(tagTable, i * 12, (i + 1) * 12);
            byte[] tagSignature = Arrays.copyOfRange(tag, 0, 4);
            String tagSignatureHex = DatatypeConverter.printHexBinary(tagSignature);
            if (tagSignatureHex.equals(DESC)) {
                profileDescriptionTag = new Tag(tag);
                profileDescriptionTag.setTagSignature(DESC);
                profileDescriptionTag.setOffsetToTagData();
                profileDescriptionTag.setTagDataSize();
            }
        }
    }

    private String getDescription() throws ParserException {
        int offset = profileDescriptionTag.getOffsetToTagData();
        int size = profileDescriptionTag.getTagDataSize();
        profileDescriptionTagData = Arrays.copyOfRange(iccProfile, offset, offset + size);
        String description;
        try {
            String profileDescTagData = new String(profileDescriptionTagData, "UTF-8");
            if (profileDescTagData.startsWith("desc")) {
                description = profileDescTagData.substring(4).trim();
            }
            else {
                byte[] byteNumber = Arrays.copyOfRange(profileDescriptionTagData, 24, 28);
                int number = ByteBuffer.wrap(byteNumber).getInt();
                byte[] descriptionNeeded = Arrays.copyOfRange(profileDescriptionTagData, number, profileDescriptionTagData.length);
                description = new String(descriptionNeeded, "UTF-16BE").trim();
            }
        } catch (Exception e) {
            throw new ParserException(e);
        }
        return description;
    }

    public void makeJsonString() throws ParserException {
        divideHeader();
        divideColourSpace();
        divideProfileDescriptionTag();
        ObjectMapper mapper = new ObjectMapper();
        String colourSpace = new String(getColourSpaceType());
        String description = new String(getDescription());
        taskDescription = new TaskDescription(colourSpace, description);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDescription);
            jsonInString = jsonInString.replaceAll("\"", "");
            System.out.println(jsonInString);
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
