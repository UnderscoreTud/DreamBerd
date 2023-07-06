package me.tud.dreamberd.lang;

import me.tud.dreamberd.exceptions.ParseException;

public interface Initiable {

    void init(Context context) throws ParseException;

}
