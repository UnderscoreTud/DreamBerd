package me.tud.dreamberd.lang.variable;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class Lifetime {

    private @Nullable final Long lines;
    private @Nullable final Instant duration;

    public Lifetime(Long lines) {
        this(lines, null);
    }

    public Lifetime(Instant duration) {
        this(null, duration);
    }

    private Lifetime(@Nullable Long lines, @Nullable Instant duration) {
        this.lines = lines;
        this.duration = duration;
    }

    public Long getLines() {
        return lines;
    }

    public Instant getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Lifetime{");
        sb.append("lines=").append(lines);
        sb.append(", duration=").append(duration);
        sb.append('}');
        return sb.toString();
    }

}
