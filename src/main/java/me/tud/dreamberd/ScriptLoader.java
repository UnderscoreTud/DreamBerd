package me.tud.dreamberd;

import me.tud.dreamberd.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptLoader {

    private static final Map<String, Script> LOADED_SCRIPTS = new HashMap<>();

    public static Script load(String fileName) throws IOException {
        return load(new File(System.getProperty("user.dir") + "/scripts", fileName));
    }

    public static Script load(File file) throws IOException {
        String input = FileUtils.readFile(file);
        Script script = new Script(file, input);
        script.parse();
        return script;
    }

}
