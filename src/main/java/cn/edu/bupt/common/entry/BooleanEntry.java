package cn.edu.bupt.common.entry;

import cn.edu.bupt.common.KvEntryType;

/**
 * Created by Administrator on 2018/4/14.
 */
public class BooleanEntry extends BasicTelemetryKvEntry {

    private final Boolean  value ;

    public BooleanEntry(String key,Boolean value){
        super(key);
        this.value = value;
    }

    @Override
    public String getDataType() {
        return KvEntryType.BOOLEAN;
    }

    @Override
    public String getValueAsString() {
        return Boolean.toString(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
