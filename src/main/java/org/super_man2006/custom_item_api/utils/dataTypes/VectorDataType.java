package org.super_man2006.custom_item_api.utils.dataTypes;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class VectorDataType implements PersistentDataType<byte[], Vector> {
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Vector> getComplexType() {
        return Vector.class;
    }

    @Override
    public byte[] toPrimitive(Vector complex, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[24]);
        bb.putDouble(complex.getX());
        bb.putDouble(complex.getY());
        bb.putDouble(complex.getZ());
        return bb.array();
    }

    @Override
    public Vector fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        return new Vector(bb.getDouble(), bb.getDouble(), bb.getDouble());
    }
}
