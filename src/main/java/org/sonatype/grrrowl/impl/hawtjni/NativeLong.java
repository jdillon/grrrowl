package org.sonatype.grrrowl.impl.hawtjni;

public class NativeLong {
    
    private long value;

    public NativeLong() {
    }
    public NativeLong(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("[NativeLong 0x%x]", longValue()); //$NON-NLS-1$
    }

    public long longValue() {
        return value;
    }

    public boolean isNull() {
        return longValue() == 0;
    }


}
