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
package com.egoal.darkestpixeldungeon.actors.mobs;

import com.egoal.darkestpixeldungeon.Dungeon;
import com.egoal.darkestpixeldungeon.actors.Char;
import com.egoal.darkestpixeldungeon.actors.Damage;
import com.egoal.darkestpixeldungeon.actors.buffs.Buff;
import com.egoal.darkestpixeldungeon.actors.buffs.Weakness;
import com.egoal.darkestpixeldungeon.items.Generator;
import com.egoal.darkestpixeldungeon.items.potions.PotionOfHealing;
import com.egoal.darkestpixeldungeon.levels.Level;
import com.egoal.darkestpixeldungeon.mechanics.Ballistica;
import com.egoal.darkestpixeldungeon.messages.Messages;
import com.egoal.darkestpixeldungeon.sprites.CharSprite;
import com.egoal.darkestpixeldungeon.sprites.WarlockSprite;
import com.egoal.darkestpixeldungeon.utils.GLog;
import com.egoal.darkestpixeldungeon.items.Item;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Warlock extends Mob implements Callback {

  private static final float TIME_TO_ZAP = 1f;

  {
    spriteClass = WarlockSprite.class;

    HP = HT = 70;
    defenseSkill = 18;

    EXP = 11;
    maxLvl = 21;

    loot = Generator.Category.POTION;
    lootChance = 0.83f;

    addResistances(Damage.Element.SHADOW, 1.25f);
    addResistances(Damage.Element.HOLY, .667f);
  }

  @Override
  public Damage giveDamage(Char target) {
    return new Damage(Random.NormalIntRange(16, 22), this, target).addElement
            (Damage.Element.SHADOW);
  }

  @Override
  public Damage defendDamage(Damage dmg) {
    dmg.value -= Random.NormalIntRange(0, 8);
    return dmg;
  }

  @Override
  public int attackSkill(Char target) {
    return 25;
  }

  @Override
  protected boolean canAttack(Char enemy) {
    return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos
            == enemy.pos;
  }

  protected boolean doAttack(Char enemy) {

    if (Dungeon.level.adjacent(pos, enemy.pos)) {

      return super.doAttack(enemy);

    } else {

      boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
      if (visible) {
        sprite.zap(enemy.pos);
      } else {
        zap();
      }

      return !visible;
    }
  }

  private void zap() {
    spend(TIME_TO_ZAP);

    Damage dmg = new Damage(Random.Int(12, 18),
            this, enemy).type(Damage.Type.MAGICAL).addElement(Damage.Element
            .SHADOW);

    if (enemy.checkHit(dmg)) {
      if (enemy == Dungeon.hero && Random.Int(2) == 0) {
        Buff.prolong(enemy, Weakness.class, Weakness.duration(enemy));
      }

      enemy.defendDamage(dmg);
      enemy.takeDamage(dmg);

      if (!enemy.isAlive() && enemy == Dungeon.hero) {
        Dungeon.fail(getClass());
        GLog.n(Messages.get(this, "bolt_kill"));
      }
    } else {
      enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
    }
  }

  public void onZapComplete() {
    zap();
    next();
  }

  @Override
  public void call() {
    next();
  }

  @Override
  public Item createLoot() {
    Item loot = super.createLoot();

    if (loot instanceof PotionOfHealing) {

      //count/10 chance of not dropping potion
      if (Random.Int(10) - Dungeon.limitedDrops.warlockHP.count < 0) {
        return null;
      } else
        Dungeon.limitedDrops.warlockHP.count++;

    }

    return loot;
  }
}
