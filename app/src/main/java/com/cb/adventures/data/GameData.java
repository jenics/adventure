package com.cb.adventures.data;

import android.content.res.AssetManager;
import android.util.Xml;

import com.cb.adventures.application.AdventureApplication;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.prop.Consume;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by jenics on 2015/10/25.
 */
public class GameData {
    public interface OnLoadDataListener {
        void onLoadFinish(LoadStepEnum step);
        void onLoadFailed(LoadStepEnum step);
    }

    public enum LoadStepEnum {
        STEP_ANIMATION,
        STEP_SKILL,
        STEP_MONSTER,
        STEP_MAP,
        STEP_CONSUME,
        STEP_EQUIP,

        ///全部载入完成回调此枚举
        STEP_END
    }
    private OnLoadDataListener mLoadDataListener;
    private static GameData mInstance;
    private XmlPullParser parser;
    private HashMap<Integer,AnimationPropetry> mAnimationMap;
    private HashMap<Integer, SkillPropetry> mSkillMap;
    private HashMap<Integer,MonsterPropetry> mMonsterMap;
    private HashMap<Integer,MapPropetry> mMapInfo;
    private HashMap<Integer,PropPropetry> mPropMap;

    private GameData() {
        if (mAnimationMap == null) {
            mAnimationMap = new HashMap<>();
        }
        if (mSkillMap == null) {
            mSkillMap = new HashMap<>();
        }
        if(mMonsterMap == null) {
            mMonsterMap = new HashMap<>();
        }
        if(mMapInfo == null) {
            mMapInfo = new HashMap<>();
        }
        if (mPropMap == null) {
            mPropMap = new HashMap<>();
        }
    }

    public void asyParsAll(final OnLoadDataListener listener) {
        mLoadDataListener = listener;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synParseAnimations();
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_ANIMATION);
                synParseSkills();
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_SKILL);
                synParseMonsters();
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_MONSTER);
                synParseMaps();
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_MAP);
                synParseConsumes();
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_CONSUME);
                synParseEquips();
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_EQUIP);

                ///全部载入完成回调此枚举
                mLoadDataListener.onLoadFinish(LoadStepEnum.STEP_END);
            }
        });
        thread.start();
    }

    public static synchronized GameData getInstance() {
        if (mInstance == null) {
            mInstance = new GameData();
        }
        return mInstance;
    }

    /**
     * 获取skill属性
     *
     * @param id 属性id
     */
    public SkillPropetry getSkillPropetry(int id) {
        return mSkillMap.get(id);
    }

    /**
     * 获取monster属性
     *
     * @param id 属性id
     */
    public MonsterPropetry getMonsterPropetry(int id) {
        return mMonsterMap.get(id);
    }

    /**
     * 获取Animation属性
     *
     * @param id 属性id
     */
    public AnimationPropetry getAnimationPropetry(int id) {
        return mAnimationMap.get(id);
    }

    /**
     * 获取地图属性
     *
     * @param id 属性id
     */
    public MapPropetry getMapPropetry(int id) {
        return mMapInfo.get(id);
    }

    /**
     * @param id 属性ID
     * @return 装备道具
     */
    public PropPropetry getProp(int id) {
        return mPropMap.get(id);
    }


    public void synParseConsumes() {
        try {
            parser = Xml.newPullParser();
            AssetManager am = AdventureApplication.getContextObj().getAssets();
            InputStream is = am.open("consume.xml");
            parser.setInput(is, "UTF-8");

            ConsumePropetry consumePropetry = new ConsumePropetry();
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    ///第一个开始节点
                    case XmlPullParser.START_TAG:
                        if ("consume".equals(nodeName)) {
                            consumePropetry = new ConsumePropetry();
                        } else if ("propId".equals(nodeName)) {
                            consumePropetry.setPropId(Integer.parseInt(parser.nextText()));
                        } else if ("bloodVolume".equals(nodeName)) {
                            consumePropetry.setBloodVolume(Integer.parseInt(parser.nextText()));
                        } else if ("magicVolume".equals(nodeName)) {
                            consumePropetry.setMagicVolume(Integer.parseInt(parser.nextText()));
                        } else if ("desc".equals(nodeName)) {
                            consumePropetry.setDesc(parser.nextText());
                        } else if ("icon".equals(nodeName)) {
                            consumePropetry.setIcon(parser.nextText());
                        } else if ("name".equals(nodeName)) {
                            consumePropetry.setName(parser.nextText());
                        } else if ("maxStackSize".equals(nodeName)) {
                            consumePropetry.setMaxStackSize(Integer.parseInt(parser.nextText()));
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("consume".equals(nodeName)) {
                            mPropMap.put(consumePropetry.getPropId(), consumePropetry);
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseConsumes() {

    }

    public void synParseEquips() {
        try {
            parser = Xml.newPullParser();
            AssetManager am = AdventureApplication.getContextObj().getAssets();
            InputStream is = am.open("equipment.xml");
            parser.setInput(is, "UTF-8");

            EquipmentPropetry equipmentPropetry = new EquipmentPropetry();
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    ///第一个开始节点
                    case XmlPullParser.START_TAG:
                        if ("equipment".equals(nodeName)) {
                            equipmentPropetry = new EquipmentPropetry();
                        } else if ("propId".equals(nodeName)) {
                            equipmentPropetry.setPropId(Integer.parseInt(parser.nextText()));
                        } else if ("loc".equals(nodeName)) {
                            equipmentPropetry.setLoc(Integer.parseInt(parser.nextText()));
                        } else if ("attackPower".equals(nodeName)) {
                            equipmentPropetry.setAttackPower(Integer.parseInt(parser.nextText()));
                        } else if ("defensivePower".equals(nodeName)) {
                            equipmentPropetry.setDefensivePower(Integer.parseInt(parser.nextText()));
                        } else if ("rank".equals(nodeName)) {
                            equipmentPropetry.setRank(Integer.parseInt(parser.nextText()));
                        } else if ("bloodVolume".equals(nodeName)) {
                            equipmentPropetry.setBloodVolume(Integer.parseInt(parser.nextText()));
                        } else if ("magicVolume".equals(nodeName)) {
                            equipmentPropetry.setMagicVolume(Integer.parseInt(parser.nextText()));
                        } else if ("desc".equals(nodeName)) {
                            equipmentPropetry.setDesc(parser.nextText());
                        } else if ("icon".equals(nodeName)) {
                            equipmentPropetry.setIcon(parser.nextText());
                        } else if ("name".equals(nodeName)) {
                            equipmentPropetry.setName(parser.nextText());
                        }  else if ("maxStackSize".equals(nodeName)) {
                            equipmentPropetry.setMaxStackSize(Integer.parseInt(parser.nextText()));
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("equipment".equals(nodeName)) {
                            mPropMap.put(equipmentPropetry.getPropId(), equipmentPropetry);
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseEquips() {

    }

    public void synParseAnimations() {
        try {
            parser = Xml.newPullParser();
            AssetManager am = AdventureApplication.getContextObj().getAssets();
            InputStream is = am.open("animation.xml");
            parser.setInput(is, "UTF-8");

            AnimationPropetry animationPropetry = new AnimationPropetry();
            Frame frame = new Frame();
            SrcInfo srcInfo = new SrcInfo();

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    ///第一个开始节点
                    case XmlPullParser.START_TAG:
                        if ("animation".equals(nodeName)) {
                            animationPropetry = new AnimationPropetry();
                        } else if ("animationId".equals(nodeName)) {
                            animationPropetry.setAnimationId(Integer.parseInt(parser.nextText()));
                        } else if ("loopTimes".equals(nodeName)) {
                            animationPropetry.setLoopTimes(Integer.parseInt(parser.nextText()));
                        } else if ("isStopInLast".equals(nodeName)) {
                            animationPropetry.setIsStopInLast(Boolean.parseBoolean(parser.nextText()));
                        } else if ("name".equals(nodeName)) {
                            animationPropetry.setName(parser.nextText());
                        }  else if ("animationType".equals(nodeName)) {
                            animationPropetry.setAnimationType(Integer.parseInt(parser.nextText()));
                        } else if("srcInfo".equals(nodeName)) {
                            srcInfo = new SrcInfo();
                            animationPropetry.setSrcInfo(srcInfo);
                        } else if ("srcName".equals(nodeName)) {
                            srcInfo.setSrcName(parser.nextText());
                        } else if("rowFramCount".equals(nodeName)) {
                            int rowFrameCount = Integer.parseInt(parser.nextText());
                            srcInfo.setRowFramCount(rowFrameCount);
                        } else if("colFramCount".equals(nodeName)){
                            int colFramCount = Integer.parseInt(parser.nextText());
                            srcInfo.setColFramCont(colFramCount);
                        } else if ("frame".equals(nodeName)) {
                            frame = new Frame();
                            animationPropetry.getFrames().add(frame);
                        } else if ("row".equals(nodeName)) {
                        frame.setRow(Integer.parseInt(parser.nextText()));
                        } else if ("col".equals(nodeName)) {
                        frame.setCol(Integer.parseInt(parser.nextText()));
                        } else if ("timeDuration".equals(nodeName)) {
                            animationPropetry.setTimeDuration(Long.parseLong(parser.nextText()));
                        } else if ("maxMoveDistance".equals(nodeName)) {
                            float maxMoveDistanceRatio = Float.parseFloat(parser.nextText());
                            animationPropetry.setMaxMoveDistance(GameConstants.sGameWidth * maxMoveDistanceRatio);
                        } else if ("actionRange".equals(nodeName)) {
                            animationPropetry.setActionRange(Float.parseFloat(parser.nextText()));
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("animation".equals(nodeName)) {
                            mAnimationMap.put(animationPropetry.getAnimationId(), animationPropetry);
                            if(srcInfo.getRowFramCount() == 1 && animationPropetry.getFrames().isEmpty()) {
                                ///行数等于1并且没有指定帧
                                for(int i=0; i<srcInfo.getColFramCont(); ++i) {
                                    frame = new Frame();
                                    frame.setRow(0);
                                    frame.setCol(i);
                                    animationPropetry.getFrames().add(frame);
                                }
                            }
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseAnimations() {

    }

    public void synParseSkills() {
        try {
            parser = Xml.newPullParser();
            AssetManager am = AdventureApplication.getContextObj().getAssets();
            InputStream is = am.open("skill.xml");
            parser.setInput(is, "UTF-8");

            SkillPropetry skillPropetry = new SkillPropetry();

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    ///第一个开始节点
                    case XmlPullParser.START_TAG:
                        if ("skill".equals(nodeName)) {
                            skillPropetry = new SkillPropetry();
                        } else if ("skillId".equals(nodeName)) {
                            skillPropetry.setSkillId(Integer.parseInt(parser.nextText()));
                        } else if ("skillType".equals(nodeName)) {
                            skillPropetry.setSkillType(Integer.parseInt(parser.nextText()));
                        } else if ("extraAttack".equals(nodeName)) {
                            skillPropetry.setExtraAttack(Integer.parseInt(parser.nextText()));
                        } else if ("effectTarget".equals(nodeName)) {
                            skillPropetry.setEffectTarget(Integer.parseInt(parser.nextText()));
                        } else if ("extraBlood".equals(nodeName)) {
                            skillPropetry.setExtraBlood(Integer.parseInt(parser.nextText()));
                        } else if ("extraMagic".equals(nodeName)) {
                            skillPropetry.setExtraMagic(Integer.parseInt(parser.nextText()));
                        } else if ("extraDefensive".equals(nodeName)) {
                            skillPropetry.setExtraDefensive(Integer.parseInt(parser.nextText()));
                        } else if ("freeMagic".equals(nodeName)) {
                            skillPropetry.setFreeMagic(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(nodeName)) {
                            skillPropetry.setName(parser.nextText());
                        } else if ("hitEffectId".equals(nodeName)) {
                            skillPropetry.setHitEffectId(Integer.parseInt(parser.nextText()));
                        }  else if ("icon".equals(nodeName)) {
                            skillPropetry.setIcon(parser.nextText());
                        } else if ("isInterruptWhileHit".equals(nodeName)) {
                            Boolean bInterruptWhileHit = Boolean.parseBoolean(parser.nextText());
                            skillPropetry.setInterruptWhileHit(bInterruptWhileHit);
                        } else if ("animationId".equals(nodeName)) {
                            int animationId = Integer.parseInt(parser.nextText());
                            skillPropetry.setAnimationPropetry(mAnimationMap.get(animationId));
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("skill".equals(nodeName)) {
                            mSkillMap.put(skillPropetry.getSkillId(), skillPropetry);
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseSkills() {

    }

    public void synParseMonsters() {
        try {
            parser = Xml.newPullParser();
            AssetManager am = AdventureApplication.getContextObj().getAssets();
            InputStream is = am.open("monster.xml");
            parser.setInput(is, "UTF-8");

            MonsterPropetry monsterPropetry = new MonsterPropetry();
            Frame frame = new Frame();
            SrcInfo srcInfo = new SrcInfo();

            final int LEFT_FRAME = 0;
            final int RIGHT_FRAME = 1;
            final int ATTACK_FRAME = 2;
            int frameIndicater = LEFT_FRAME;     ///frame指示器

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    ///第一个开始节点
                    case XmlPullParser.START_TAG:
                        if ("monster".equals(nodeName)) {
                            monsterPropetry = new MonsterPropetry();
                        } else if ("monsterId".equals(nodeName)) {
                            monsterPropetry.setMonsterId(Integer.parseInt(parser.nextText()));
                        } else if ("monsterName".equals(nodeName)) {
                            monsterPropetry.setMonsterName(parser.nextText());
                        } else if("srcInfo".equals(nodeName)) {
                            srcInfo = new SrcInfo();
                            monsterPropetry.setSrcInfo(srcInfo);
                        } else if ("srcName".equals(nodeName)) {
                            srcInfo.setSrcName(parser.nextText());
                        } else if("rowFramCount".equals(nodeName)) {
                            int rowFrameCount = Integer.parseInt(parser.nextText());
                            srcInfo.setRowFramCount(rowFrameCount);
                        } else if("colFramCount".equals(nodeName)){
                            int colFramCount = Integer.parseInt(parser.nextText());
                            srcInfo.setColFramCont(colFramCount);
                        } else if ("frame".equals(nodeName)) {
                            frame = new Frame();
                        } else if ("row".equals(nodeName)) {
                            frame.setRow(Integer.parseInt(parser.nextText()));
                        } else if ("col".equals(nodeName)) {
                            frame.setCol(Integer.parseInt(parser.nextText()));
                        } else if ("leftFrames".equals(nodeName)) {
                            frameIndicater = LEFT_FRAME;
                            monsterPropetry.setLeftFrames(new LinkedList<Frame>());
                        } else if ("rightFrames".equals(nodeName)) {
                            frameIndicater = RIGHT_FRAME;
                            monsterPropetry.setRightFrames(new LinkedList<Frame>());
                        } else if ("speed".equals(nodeName)) {
                            monsterPropetry.setSpeed(Integer.parseInt(parser.nextText()));
                        }else if ("attackPower".equals(nodeName)) {
                            monsterPropetry.setAttackPower(Integer.parseInt(parser.nextText()));
                        }else if ("defensivePower".equals(nodeName)) {
                            monsterPropetry.setDefensivePower(Integer.parseInt(parser.nextText()));
                        }else if ("bloodTotalVolume".equals(nodeName)) {
                            int blood = Integer.parseInt(parser.nextText());
                            monsterPropetry.setBloodTotalVolume(blood);
                            monsterPropetry.setBloodVolume(blood);
                        }else if ("magicTotalVolume".equals(nodeName)) {
                            int magic = Integer.parseInt(parser.nextText());
                            monsterPropetry.setMagicTotalVolume(magic);
                            monsterPropetry.setMagicVolume(magic);
                        }else if ("attackLength".equals(nodeName)) {
                            monsterPropetry.setAttackLength(Integer.parseInt(parser.nextText()));
                        }else if ("rank".equals(nodeName)) {
                            monsterPropetry.setRank(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("monster".equals(nodeName)) {
                            mMonsterMap.put(monsterPropetry.getMonsterId(), monsterPropetry);
                        } else if ("frame".equals(nodeName)) {
                            if (frameIndicater == LEFT_FRAME) {
                                monsterPropetry.getLeftFrames().add(frame);
                            } else if(frameIndicater == RIGHT_FRAME) {
                                monsterPropetry.getRightFrames().add(frame);
                            }
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseMonsters() {

    }

    public void synParseMaps() {
        try {
            parser = Xml.newPullParser();
            AssetManager am = AdventureApplication.getContextObj().getAssets();
            InputStream is = am.open("map.xml");
            parser.setInput(is, "UTF-8");

            MapPropetry mapPropetry = new MapPropetry();
            MapPropetry.MonsterPack monsterPack = mapPropetry.new MonsterPack();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    ///第一个开始节点
                    case XmlPullParser.START_TAG:
                        if ("map".equals(nodeName)) {
                            mapPropetry = new MapPropetry();
                        } else if ("mapId".equals(nodeName)) {
                            int id = Integer.parseInt(parser.nextText());
                            mapPropetry.setMapId(id);
                            mapPropetry.setPreGate(id-1);   ///默认值
                            mapPropetry.setNextGate(id+1);   ///默认值
                        } else if ("srcName".equals(nodeName)) {
                            mapPropetry.setSrcName(parser.nextText());
                        } else if("mapLenRatio".equals(nodeName)) {
                            mapPropetry.setMapLenRatio(Float.parseFloat(parser.nextText()));
                        } else if ("monster".equals(nodeName)) {
                            monsterPack = mapPropetry.new MonsterPack();
                            mapPropetry.getMonsterPaks().add(monsterPack);
                        } else if ("monsterId".equals(nodeName)) {
                            monsterPack.setMonsterId(Integer.parseInt(parser.nextText()));
                        } else if ("monsterNum".equals(nodeName)) {
                            monsterPack.setMonsterNum(Integer.parseInt(parser.nextText()));
                        } else if ("preGate".equals(nodeName)) {
                            mapPropetry.setPreGate(Integer.parseInt(parser.nextText()));
                        } else if ("nextGate".equals(nodeName)) {
                            mapPropetry.setNextGate(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(nodeName)) {
                            mapPropetry.setName(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("map".equals(nodeName)) {
                            mMapInfo.put(mapPropetry.getMapId(),mapPropetry);
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseMaps() {

    }

}
