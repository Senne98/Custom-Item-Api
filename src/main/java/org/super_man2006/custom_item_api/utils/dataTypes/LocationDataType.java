package org.super_man2006.custom_item_api.utils.dataTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

public class LocationDataType implements PersistentDataType<byte[], Location> {
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public byte[] toPrimitive(Location complex, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[40]);
        bb.putLong(complex.getWorld().getUID().getMostSignificantBits());
        bb.putLong(complex.getWorld().getUID().getLeastSignificantBits());
        bb.putDouble(complex.getX());
        bb.putDouble(complex.getY());
        bb.putDouble(complex.getZ());
        return bb.array();
    }

    @Override
    public Location fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        return new Location(Bukkit.getWorld(new UUID(bb.getLong(), bb.getLong())), bb.getDouble(), bb.getDouble(), bb.getDouble());
    }
}
