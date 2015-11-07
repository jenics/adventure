package com.cb.adventures.data;

import android.content.res.AssetManager;
import android.util.Xml;

import com.cb.adventures.application.MyApplication;
import com.cb.adventures.constants.GameConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by jenics on 2015/10/25.
 */
public class GameData {
    private static GameData mInstance;
    private XmlPullParser parser;
    private HashMap<Integer, SkillPropetry> mSkillMap;
    private HashMap<Integer,MonsterPropetry> mMonsterMap;
    private HashMap<Integer,MapPropetry> mMapInfo;

    private GameData() {
        if (mSkillMap == null) {
            mSkillMap = new HashMap<>();
        }
        if(mMonsterMap == null) {
            mMonsterMap = new HashMap<>();
        }
        if(mMapInfo == null) {
            mMapInfo = new HashMap<>();
        }
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
     * 获取skill属性
     *
     * @param id 属性id
     */
    public MonsterPropetry getMonsterPropetry(int id) {
        return mMonsterMap.get(id);
    }

    /**
     * 获取地图属性
     *
     * @param id 属性id
     */
    public MapPropetry getMapPropetry(int id) {
        return mMapInfo.get(id);
    }

    public void synParseSkills() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            parser = Xml.newPullParser();
            AssetManager am = MyApplication.getContextObj().getAssets();
            InputStream is = am.open("skill.xml");
            parser.setInput(is, "UTF-8");

            SkillPropetry skillPropetry = null;
            Frame frame = null;
            SrcInfo srcInfo = null;

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
                        }  else if ("skillAnimationType".equals(nodeName)) {
                            skillPropetry.setSkillAnimationType(Integer.parseInt(parser.nextText()));
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
                        } else if("srcInfo".equals(nodeName)) {
                            srcInfo = new SrcInfo();
                            skillPropetry.setSrcInfo(srcInfo);
                        } else if ("srcName".equals(nodeName)) {
                            srcInfo.setSrcName(parser.nextText());
                        } else if("rowFramCount".equals(nodeName)) {
                            int rowFrameCount = Integer.parseInt(parser.nextText());
                            srcInfo.setRowFramCount(rowFrameCount);
                        } else if("colFramCount".equals(nodeName)){
                            int colFramCount = Integer.parseInt(parser.nextText());
                            srcInfo.setColFramCont(colFramCount);
                        } else if ("offsetX".equals(nodeName)) {
                            skillPropetry.setOffsetX(Integer.parseInt(parser.nextText()));
                        } else if ("frame".equals(nodeName)) {
                            frame = new Frame();
                            skillPropetry.getFrames().add(frame);
                        } else if ("row".equals(nodeName)) {
                        frame.setRow(Integer.parseInt(parser.nextText()));
                        } else if ("col".equals(nodeName)) {
                        frame.setCol(Integer.parseInt(parser.nextText()));
                        } else if ("timeDuration".equals(nodeName)) {
                            skillPropetry.setTimeDuration(Long.parseLong(parser.nextText()));
                        } else if ("maxMoveDistance".equals(nodeName)) {
                            float maxMoveDistanceRatio = Float.parseFloat(parser.nextText());
                            skillPropetry.setMaxMoveDistance(GameConstants.sGameWidth*maxMoveDistanceRatio);
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("skill".equals(nodeName)) {
                            mSkillMap.put(skillPropetry.getSkillId(), skillPropetry);
                            if(srcInfo.getRowFramCount() == 1 && skillPropetry.getFrames().isEmpty()) {
                                ///行数等于1并且没有指定帧
                                for(int i=0; i<srcInfo.getColFramCont(); ++i) {
                                    frame = new Frame();
                                    frame.setRow(0);
                                    frame.setCol(i);
                                    skillPropetry.getFrames().add(frame);
                                }
                            }
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseSkills() {

    }

    public void synParseMonsters() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            parser = Xml.newPullParser();
            AssetManager am = MyApplication.getContextObj().getAssets();
            InputStream is = am.open("monster.xml");
            parser.setInput(is, "UTF-8");

            MonsterPropetry monsterPropetry = null;
            Frame frame = null;
            SrcInfo srcInfo = null;

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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseMonsters() {

    }

    public void synParseMaps() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            parser = Xml.newPullParser();
            AssetManager am = MyApplication.getContextObj().getAssets();
            InputStream is = am.open("map.xml");
            parser.setInput(is, "UTF-8");

            MapPropetry mapPropetry = null;
            MapPropetry.MonsterPack monsterPack = null;
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
                            mapPropetry.setMapId(Integer.parseInt(parser.nextText()));
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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asyParseMaps() {

    }

}
