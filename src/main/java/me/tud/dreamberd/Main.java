package me.tud.dreamberd;

import me.tud.dreamberd.lexer.Lexer;
import me.tud.dreamberd.lexer.Token;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        String input = """
                if (1 += 2) print("hello")!""";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> buffer = lexer.tokenize();
        buffer.stream()
                .filter(token -> token.type().isSignificant())
                .forEach(System.out::println);
    }

}
