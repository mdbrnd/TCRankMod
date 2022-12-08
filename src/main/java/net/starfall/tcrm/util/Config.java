package net.starfall.tcrm.util;

import java.util.ArrayList;

public class Config {
    public boolean enabled;
    public String defaultRank;
    public String defaultInviterUUID;
    public String defaultInviterName;
    // A list of player names
    public ArrayList<String> players;

    public Config() {
        this.enabled = true;
        this.defaultRank = "visitor";
        this.defaultInviterUUID = "615281c4-6670-445f-9bd6-433002d3cbf7";
        this.defaultInviterName = "taeko_chan";
        // Add the default inviter name as a first player
        this.players = new ArrayList<>();
        this.players.add(this.defaultInviterName);
    }
}
