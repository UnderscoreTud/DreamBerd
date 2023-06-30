package me.tud.dreamberd;

import me.tud.dreamberd.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptLoader {

    private static final Map<String, Script> LOADED_SCRIPTS = new HashMap<>();

    public Script load(String fileName) throws IOException {
        return load(new File("scripts", fileName));
    }

    public Script load(File file) throws IOException {
        String input = FileUtils.readFile(file);
        // parsing and loading stuff
        return null;
    }

}
