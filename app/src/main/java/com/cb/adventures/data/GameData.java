package com.cb.adventures.data;

import android.content.res.AssetManager;
import android.util.Xml;

import com.cb.adventures.common.MyApplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;

/**
 * Created by jenics on 2015/10/25.
 */
public class GameData {
    private static GameData mInstance;
    private XmlPullParser parser;
    private HashMap<Integer, SkillPropetry> mSkillMap;

    private GameData() {
        if (mSkillMap == null) {
            mSkillMap = new HashMap<>();
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
    public SkillPropetry findSkill(int id) {
        return mSkillMap.get(id);
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
                        } else if ("srcName".equals(nodeName)) {
                            srcInfo.setSrcName(parser.nextText());
                        } else if("rowFramCount".equals(nodeName)) {
                            srcInfo.setRowFramCount(Integer.parseInt(parser.nextText()));
                        } else if("colFramCont".equals(nodeName)){
                            srcInfo.setColFramCont(Integer.parseInt(parser.nextText()));
                        } else if ("offsetX".equals(nodeName)) {
                            skillPropetry.setOffsetX(Integer.parseInt(parser.nextText()));
                        } else if ("frame".equals(nodeName)) {
                            frame = new Frame();
                        } else if ("row".equals(nodeName)) {
                        frame.setRow(Integer.parseInt(parser.nextText()));
                        } else if ("col".equals(nodeName)) {
                        frame.setCol(Integer.parseInt(parser.nextText()));
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("skill".equals(nodeName)) {
                            mSkillMap.put(skillPropetry.getSkillId(), skillPropetry);
                        } else if ("frame".equals(nodeName)) {
                            skillPropetry.getFrames().add(frame);
                        } else if("srcInfo".equals(nodeName)) {
                            skillPropetry.setSrcInfo(srcInfo);
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

        asyParseSkills();
    }

    public void asyParseSkills() {

    }

}
