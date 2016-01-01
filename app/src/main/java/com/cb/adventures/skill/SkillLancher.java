package com.cb.adventures.skill;

import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.prop.IUsable;
import com.cb.adventures.view.Player;
import java.lang.ref.WeakReference;

/**
 * Created by jenics on 2015/12/31.
 */
public class SkillLancher implements IUsable {
    private WeakReference<Player> playerWeakReference;
    private SkillPropetry skillPropetry;
    public SkillLancher(Player player,SkillPropetry skillPropetry) {
        this.skillPropetry = skillPropetry;
        playerWeakReference = new WeakReference<>(player);
    }
    @Override
    public void use() {
        Player player = playerWeakReference.get();
        if (player != null) {
            if (skillPropetry != null) {
                player.attack(skillPropetry.getSkillId());
            }
        }
    }

    @Override
    public String getIcon() {
        return skillPropetry.getIcon();
    }
}
