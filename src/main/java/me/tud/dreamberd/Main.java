package me.tud.dreamberd;

import me.tud.dreamberd.lexer.Lexer;

public class Main {

    public static void main(String[] args) {
        String input = """
                test!¡""";
        Lexer lexer = new Lexer(input);
        System.out.println(input);
        lexer.tokenize().forEach(System.out::println);
    }

}
