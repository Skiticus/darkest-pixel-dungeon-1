/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.egoal.darkestpixeldungeon.actors.buffs;

import com.egoal.darkestpixeldungeon.actors.Char;
import com.egoal.darkestpixeldungeon.items.rings.RingOfElements.Resistance;
import com.egoal.darkestpixeldungeon.messages.Messages;
import com.egoal.darkestpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Charm extends FlavourBuff {

  public int object = 0;

  private static final String OBJECT = "object";

  {
    type = buffType.NEGATIVE;
  }

  @Override
  public void storeInBundle(Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put(OBJECT, object);
  }

  @Override
  public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    object = bundle.getInt(OBJECT);
  }

  @Override
  public int icon() {
    return BuffIndicator.HEART;
  }

  @Override
  public String toString() {
    return Messages.get(this, "name");
  }

  public static float durationFactor(Char ch) {
    Resistance r = ch.buff(Resistance.class);
    return r != null ? r.durationFactor() : 1;
  }

  @Override
  public String heroMessage() {
    return Messages.get(this, "heromsg");
  }

  @Override
  public String desc() {
    return Messages.get(this, "desc", dispTurns());
  }

  // charm attach should be delayed to avoid detach in Char::takeDamage 
  public static class Attacher extends FlavourBuff {
    {
      actPriority = Integer.MIN_VALUE;
      type = buffType.NEGATIVE;
    }

    int charmer = -1;
    int charm_duration = 0;

    public Attacher(int charmer_id, int charmDuration) {
      charmer = charmer_id;
      charm_duration = charmDuration;
    }

    @Override
    public boolean act() {
      Buff.affect(target, Charm.class, Charm.durationFactor(target) * 
              charm_duration).object = charmer;
      return super.act();
    }
  }
}
