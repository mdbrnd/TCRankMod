package net.starfall.tcrm.util;

import java.util.ArrayList;

public class Config {
    public boolean enabled;
    public String defaultRank;
    public String defaultInviterName;
    public ArrayList<Player> players;


    public Config() {
        this.enabled = true;
        this.defaultRank = "admin";
        this.defaultInviterName = "taeko_chan";
        
        this.players = new ArrayList<>();
        this.players.add(new Player(defaultInviterName, defaultRank, defaultInviterName));
    }
}
