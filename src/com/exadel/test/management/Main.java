package com.exadel.test.management;

import com.exadel.test.exceptions.ParserException;
import com.exadel.test.utils.ICCParser;

import java.io.File;

public class Main {


    public static void main(String[] args) throws ParserException {
        if (args != null && args.length > 0) {
            String path = args[0];
            ICCParser parser = null;
            try {
                File file = new File(path);
                parser = new ICCParser(file);
                parser.makeJsonString();
            } catch (Exception e) {
                throw new ParserException(e);
            }
        }
    }

}

