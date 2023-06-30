package me.tud.dreamberd.utils;

import java.io.*;
import java.nio.file.Files;

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    public static String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

}
