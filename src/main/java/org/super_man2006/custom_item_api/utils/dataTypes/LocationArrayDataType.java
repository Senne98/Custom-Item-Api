package org.super_man2006.custom_item_api.utils.dataTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationArrayDataType implements PersistentDataType<byte[], Location[]> {
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Location[]> getComplexType() {
        return Location[].class;
    }

    @Override
    public byte[] toPrimitive(Location[] complex, PersistentDataAdapterContext context) {

        List<byte[]> bytes = new ArrayList<>();

        for (Location location : complex) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[40]);
            bb.putLong(location.getWorld().getUID().getMostSignificantBits());
            bb.putLong(location.getWorld().getUID().getLeastSignificantBits());
            bb.putDouble(location.getX());
            bb.putDouble(location.getY());
            bb.putDouble(location.getZ());
            bytes.add(bb.array());
        }

        ByteBuffer bb = ByteBuffer.wrap(new byte[40 * bytes.size()]);
        for (byte[] b : bytes) {
            bb.put(b);
        }

        return bb.array();
    }

    @Override
    public Location[] fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);

        Location[] locations = new Location[primitive.length / 40];
        for (int i = 0; i < primitive.length / 40; i++) {
            locations[i] = new Location(Bukkit.getWorld(new UUID(bb.getLong(), bb.getLong())), bb.getDouble(), bb.getDouble(), bb.getDouble());
        }

        return locations;
    }
}
