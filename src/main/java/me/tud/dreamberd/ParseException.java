package me.tud.dreamberd;

import me.tud.dreamberd.utils.StringReader;

public class ParseException extends RuntimeException {

    public ParseException(StringReader reader) {
        this("Unexpected character: " + reader.peek(), reader.getCursor());
    }

    public ParseException(String error, int position) {
        this(error + " at C" + position);
    }

    public ParseException(String message) {
        super(message);
    }

}
