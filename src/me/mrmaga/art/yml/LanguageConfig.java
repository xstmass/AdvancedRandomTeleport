package me.mrmaga.art.yml;

import me.mrmaga.art.Main;
import me.mrmaga.art.Utils;

import java.util.HashMap;

public class LanguageConfig extends CustomConfig {

    private HashMap<String, String> messages;

    public LanguageConfig(Main main) {
        super(main, "language", true);
        messages = new HashMap<>();
        this.initData("messages");
    }

    private void initData(String section) {
        for (String path : yml.getConfigurationSection(section).getKeys(false)) {
            messages.put(path, Utils.color(yml.getString(section + "." + path)));
        }
    }

    private void reloadData(String section) {
        for (String path : yml.getConfigurationSection(section).getKeys(false)) {
            String formated = Utils.color(yml.getString(section + "." + path));
            if (messages.get(path).equals(formated)) {
                continue;
            }
            messages.put(path, formated);
        }
    }

    @Override
    public void reload() {
        super.reload();
        this.reloadData("messages");
    }

    public String getMsg(String path) {
        return messages.get(path);
    }

    public String getPrefixedMsg(String path) {
        return plugin.getMainConfig().getPrefix() + " " + messages.get(path);
    }
}
