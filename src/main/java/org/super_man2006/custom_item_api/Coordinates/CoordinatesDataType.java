package org.super_man2006.custom_item_api.Coordinates;

import org.bukkit.World;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class CoordinatesDataType implements PersistentDataType<byte[], Coordinates> {
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Coordinates> getComplexType() {
        return Coordinates.class;
    }

    @Override
    public byte[] toPrimitive(Coordinates complex, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[24]);
        bb.putDouble(complex.x);
        bb.putDouble(complex.y);
        bb.putDouble(complex.z);
        return bb.array();
    }

    @Override
    public Coordinates fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        return new Coordinates(bb.getDouble(), bb.getDouble(), bb.getDouble());
    }
}
