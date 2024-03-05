package org.super_man2006.custom_item_api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class VersionCheck {

    public VersionCheck() {
        URL url;
        try {
            url = new URL("https://api.github.com/repos/Senne98/Custom-Item-Api/releases");
            try (InputStream input = url.openStream()) {
                InputStreamReader isr = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder jsonString = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1) {
                    jsonString.append((char) c);
                }
                JsonArray jsonObject = new JsonParser().parse(String.valueOf(jsonString)).getAsJsonArray();

                AtomicReference<String> latestName = new AtomicReference<>("");
                AtomicReference<String> link = new AtomicReference<>("");
                AtomicLong latestVersion = new AtomicLong(0);

                jsonObject.forEach(jsonElement -> {
                    try {
                        //2024-02-20T20:43:24Z
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        Date date = dateFormat.parse(jsonElement.getAsJsonObject().get("published_at").getAsString());
                        if (date.getTime() > latestVersion.get()) {
                            latestVersion.set(date.getTime());
                            latestName.set(jsonElement.getAsJsonObject().get("tag_name").getAsString());
                            link.set(jsonElement.getAsJsonObject().get("html_url").getAsString());
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });

                String latest = latestName.get().replace("Custom_Item_Api-", "");
                String current = CustomItemApi.plugin.getDescription().getVersion();

                if (!latest.equals(current)) {
                    CustomItemApi.plugin.getComponentLogger().info(Component.text("A new version of Custom Item Api is available at: ").append(Component.text(link.get()).clickEvent(ClickEvent.openUrl(link.get()))).color(NamedTextColor.GOLD));
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
