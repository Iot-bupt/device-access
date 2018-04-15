package cn.edu.bupt.common.entry;

import cn.edu.bupt.common.KvEntryType;

import java.util.Optional;

/**
 * Created by Administrator on 2018/4/14.
 */
public class StringEntry extends BasicTelemetryKvEntry {
    private final String value;

    public  StringEntry(String key,String value) {
        super(key);
        this.value = value;
    }
    @Override
    public Optional<String> getStrValue() {
        return Optional.of(value);
    }

    @Override
    public String getDataType() {
        return KvEntryType.STRING;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
