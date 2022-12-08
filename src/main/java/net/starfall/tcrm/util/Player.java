package net.starfall.tcrm.util;

import net.starfall.tcrm.TCRankingMod;

public class Player {
    public String name;
    public String rank;
    public String inviterName;

    public Player(String name, String rank, String inviterName) {
        this.name = name;
        this.rank = rank;
        this.inviterName = inviterName;
    }

    public Player(String name) {
        this.name = name;
        this.rank = TCRankingMod.config.defaultRank;
        this.inviterName = TCRankingMod.config.defaultInviterName;
    }
}
