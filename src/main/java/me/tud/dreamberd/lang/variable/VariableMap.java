package me.tud.dreamberd.lang.variable;

import me.tud.dreamberd.exceptions.VariableException;
import me.tud.dreamberd.lang.Identifier;
import me.tud.dreamberd.lang.VariableModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VariableMap {

    public static final int LENIENT = 0, STRICT = 1;

    @Nullable
    private final VariableMap parentMap;
    private final Map<String, VariableInfo> map = new HashMap<>();
    private final int flags;

    public VariableMap() {
        this(STRICT);
    }

    public VariableMap(int flags) {
        this(null, flags);
    }

    public VariableMap(@Nullable VariableMap parentMap) {
        this(parentMap, STRICT);
    }

    public VariableMap(@Nullable VariableMap parentMap, int flags) {
        this.parentMap = parentMap;
        this.flags = flags;
    }

    public VariableInfo declareVariable(byte modifiers, Identifier identifier, @Nullable Lifetime lifetime, int priority) {
        VariableInfo previous = map.get(identifier.get());
        if (previous != null && previous.priority > priority)
            return null;
        VariableInfo info = new VariableInfo(modifiers, priority, lifetime);
        map.put(identifier.get(), info);
        return info;
    }

    public void deleteVariable(String identifier) {
        map.remove(identifier);
    }

    public boolean variableExists(String identifier) {
        return (parentMap != null && flags == STRICT ? map.containsKey(identifier) && parentMap.variableExists(identifier) : map.containsKey(identifier));
    }

    public boolean variableExists(VariableInfo info) {
        return (parentMap != null && flags == STRICT ? map.containsValue(info) && parentMap.variableExists(info) : map.containsValue(info));
    }

    public void clear() {
        map.clear();
    }

    public @Nullable <T> T getVariable(String identifier, Class<? extends T>[] returnTypes) {
        return getVariable(identifier, returnTypes, false);
    }

    public @Nullable <T> T getVariable(String identifier, Class<? extends T>[] returnTypes, boolean previous) {
        VariableInfo info = getVariableInfo(identifier);
        Object value = previous ? info.previousValue : info.value;
        for (Class<? extends T> type : returnTypes) {
            if (type.isInstance(value))
                return type.cast(value);
        }
        return null;
    }

    public VariableInfo getVariableInfo(String identifier) {
        VariableInfo info = map.get(identifier);
        if (info == null && parentMap != null)
            return parentMap.getVariableInfo(identifier);
        if (info == null)
            throw VariableException.doesntExist(identifier);
        return info;
    }

    public void setVariable(String identifier, @Nullable Object value) throws VariableException {
        VariableInfo info = map.get(identifier);
        if (info == null)
            throw VariableException.doesntExist(identifier);
        if (!VariableModifiers.isAssignable(info.modifiers))
            throw VariableException.cannotBe(identifier, "re-assigned");
        info.setValue(value);
    }

    public Optional<VariableMap> getParentMap() {
        return Optional.ofNullable(parentMap);
    }

    private Map<String, VariableInfo> collectMaps() {
        Map<String, VariableInfo> map = new HashMap<>(this.map);
        if (parentMap != null)
            map.putAll(parentMap.collectMaps());
        return map;
    }

    @Override
    public String toString() {
        return collectMaps().toString();
    }

    public static class VariableInfo {

        private final byte modifiers;
        private final int priority;
        private final @Nullable Lifetime lifetime;
        private @Nullable Object value;
        private @Nullable Object previousValue;

        private VariableInfo(byte modifiers, int priority, @Nullable Lifetime lifetime) {
            this.modifiers = modifiers;
            this.priority = priority;
            this.lifetime = lifetime;
        }

        public byte getModifiers() {
            return modifiers;
        }

        public int getPriority() {
            return priority;
        }

        public Lifetime getLifetime() {
            return lifetime;
        }

        public @Nullable Object getValue() {
            return value;
        }

        public void setValue(@Nullable Object value) {
            this.previousValue = this.value;
            this.value = value;
        }

        public @Nullable Object getPreviousValue() {
            return previousValue;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("VariableInfo{");
            sb.append("modifiers=").append(modifiers);
            sb.append(", priority=").append(priority);
            sb.append(", lifetime=").append(lifetime);
            sb.append(", value=").append(value);
            sb.append(", previousValue=").append(previousValue);
            sb.append('}');
            return sb.toString();
        }

    }

}
