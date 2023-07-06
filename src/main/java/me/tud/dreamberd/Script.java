package me.tud.dreamberd;

import me.tud.dreamberd.lang.Context;
import me.tud.dreamberd.lang.variable.VariableMap;
import me.tud.dreamberd.parser.Parser;
import me.tud.dreamberd.parser.RootNode;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class Script {

    private final File file;
    private final String name;
    private final String content;
    private final Context context;
    private final Parser parser;
    private @Nullable RootNode root;

    public Script(File file, String content) {
        this.file = file;
        this.name = file.getName();
        this.content = content;
        this.context = new Context(this, new VariableMap());
        this.parser = new Parser(context, content);
    }

    public boolean parse() {
        root = parser.parse();
        root.init();
        return true;
    }

    public void execute() {
        if (root == null)
            throw new IllegalStateException("The script has not been parsed yet");
        root.execute();
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Context getContext() {
        return context;
    }

    public Parser getParser() {
        return parser;
    }

}
