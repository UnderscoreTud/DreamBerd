package me.tud.dreamberd;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Script script = ScriptLoader.load("test.db");
        script.execute();
//        System.out.println(script.getContext().getVariableMap().getVariable("true", new Class[]{Object.class}));
//        System.out.println(script.getContext().getVariableMap().getVariable("false", new Class[]{Object.class}));
//        System.out.println(script.getContext().getVariableMap().getVariable("poop", new Class[]{Object.class}));
    }

}
