/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  javax.annotation.Nonnull
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityEgg
 *  net.minecraft.entity.projectile.EntitySnowball
 *  net.minecraft.entity.projectile.EntityWitherSkull
 *  net.minecraft.init.MobEffects
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.text.TextFormatting
 */
package me.zeroeightsix.kami.gui.kami;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.gui.kami.DisplayGuiScreen;
import me.zeroeightsix.kami.gui.kami.RootFontRenderer;
import me.zeroeightsix.kami.gui.kami.Stretcherlayout;
import me.zeroeightsix.kami.gui.kami.component.ActiveModules;
import me.zeroeightsix.kami.gui.kami.component.Radar;
import me.zeroeightsix.kami.gui.kami.component.SettingsPanel;
import me.zeroeightsix.kami.gui.kami.theme.kami.KamiTheme;
import me.zeroeightsix.kami.gui.rgui.GUI;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Scrollpane;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.TickListener;
import me.zeroeightsix.kami.gui.rgui.component.use.CheckButton;
import me.zeroeightsix.kami.gui.rgui.component.use.Label;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import me.zeroeightsix.kami.gui.rgui.util.ContainerHelper;
import me.zeroeightsix.kami.gui.rgui.util.Docking;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.ColourHolder;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.LagCompensator;
import me.zeroeightsix.kami.util.Pair;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class KamiGUI
extends GUI {
    public static final RootFontRenderer fontRenderer = new RootFontRenderer(1.0f);
    private static final int DOCK_OFFSET = 0;
    private static final String witherSkullText = (Object)TextFormatting.DARK_GRAY + "Wither Skull";
    private static final String enderCrystalText = (Object)TextFormatting.LIGHT_PURPLE + "End Crystal";
    private static final String thrownEnderPearlText = (Object)TextFormatting.LIGHT_PURPLE + "Thrown Ender Pearl";
    private static final String minecartText = "Minecart";
    private static final String itemFrameText = "Item Frame";
    private static final String thrownEggText = "Thrown Egg";
    private static final String thrownSnowballText = "Thrown Snowball";
    public static ColourHolder primaryColour = new ColourHolder(29, 29, 29);
    public Theme theme = this.getTheme();

    public KamiGUI() {
        super(new KamiTheme());
    }

    private static String getEntityName(@Nonnull Entity entity) {
        if (entity instanceof EntityItem) {
            return (Object)TextFormatting.DARK_AQUA + ((EntityItem)entity).func_92059_d().func_77973_b().func_77653_i(((EntityItem)entity).func_92059_d());
        }
        if (entity instanceof EntityWitherSkull) {
            return witherSkullText;
        }
        if (entity instanceof EntityEnderCrystal) {
            return enderCrystalText;
        }
        if (entity instanceof EntityEnderPearl) {
            return thrownEnderPearlText;
        }
        if (entity instanceof EntityMinecart) {
            return minecartText;
        }
        if (entity instanceof EntityItemFrame) {
            return itemFrameText;
        }
        if (entity instanceof EntityEgg) {
            return thrownEggText;
        }
        if (entity instanceof EntitySnowball) {
            return thrownSnowballText;
        }
        return entity.func_70005_c_();
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), (Comparable)entry.getValue());
        }
        return result;
    }

    public static void dock(Frame component) {
        Docking docking = component.getDocking();
        if (docking.isTop()) {
            component.setY(0);
        }
        if (docking.isBottom()) {
            component.setY(Wrapper.getMinecraft().field_71440_d / DisplayGuiScreen.getScale() - component.getHeight() - 0);
        }
        if (docking.isLeft()) {
            component.setX(0);
        }
        if (docking.isRight()) {
            component.setX(Wrapper.getMinecraft().field_71443_c / DisplayGuiScreen.getScale() - component.getWidth() - 0);
        }
        if (docking.isCenterHorizontal()) {
            component.setX(Wrapper.getMinecraft().field_71443_c / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2);
        }
        if (docking.isCenterVertical()) {
            component.setY(Wrapper.getMinecraft().field_71440_d / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
        }
    }

    @Override
    public void drawGUI() {
        super.drawGUI();
    }

    @Override
    public void initializeGUI() {
        int y;
        HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>> categoryScrollpaneHashMap = new HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>>();
        for (final Module module : ModuleManager.getModules()) {
            Scrollpane scrollpane;
            if (module.getCategory().isHidden()) continue;
            Module.Category moduleCategory = module.getCategory();
            if (!categoryScrollpaneHashMap.containsKey((Object)moduleCategory)) {
                Stretcherlayout stretcherlayout = new Stretcherlayout(1);
                stretcherlayout.setComponentOffsetWidth(0);
                scrollpane = new Scrollpane(this.getTheme(), stretcherlayout, 300, 260);
                scrollpane.setMaximumHeight(180);
                categoryScrollpaneHashMap.put(moduleCategory, new Pair<Scrollpane, SettingsPanel>(scrollpane, new SettingsPanel(this.getTheme(), null)));
            }
            final Pair pair = (Pair)categoryScrollpaneHashMap.get((Object)moduleCategory);
            scrollpane = (Scrollpane)pair.getKey();
            final CheckButton checkButton = new CheckButton(module.getName());
            checkButton.setToggled(module.isEnabled());
            checkButton.addTickListener(() -> {
                checkButton.setToggled(module.isEnabled());
                checkButton.setName(module.getName());
            });
            checkButton.addMouseListener(new MouseListener(){

                @Override
                public void onMouseDown(MouseListener.MouseButtonEvent event) {
                    if (event.getButton() == 1) {
                        ((SettingsPanel)pair.getValue()).setModule(module);
                        ((SettingsPanel)pair.getValue()).setX(event.getX() + checkButton.getX());
                        ((SettingsPanel)pair.getValue()).setY(event.getY() + checkButton.getY());
                    }
                }

                @Override
                public void onMouseRelease(MouseListener.MouseButtonEvent event) {
                }

                @Override
                public void onMouseDrag(MouseListener.MouseButtonEvent event) {
                }

                @Override
                public void onMouseMove(MouseListener.MouseMoveEvent event) {
                }

                @Override
                public void onScroll(MouseListener.MouseScrollEvent event) {
                }
            });
            checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>(){

                @Override
                public void execute(CheckButton component, CheckButton.CheckButtonPoof.CheckButtonPoofInfo info) {
                    if (info.getAction().equals((Object)CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                        module.setEnabled(checkButton.isToggled());
                    }
                }
            });
            scrollpane.addChild(checkButton);
        }
        int x = 10;
        int nexty = y = 10;
        for (Map.Entry entry : categoryScrollpaneHashMap.entrySet()) {
            Stretcherlayout stretcherlayout = new Stretcherlayout(1);
            stretcherlayout.COMPONENT_OFFSET_Y = 1;
            Frame frame = new Frame(this.getTheme(), stretcherlayout, ((Module.Category)((Object)entry.getKey())).getName());
            Scrollpane scrollpane = (Scrollpane)((Pair)entry.getValue()).getKey();
            frame.addChild(scrollpane);
            frame.addChild((Component)((Pair)entry.getValue()).getValue());
            scrollpane.setOriginOffsetY(0);
            scrollpane.setOriginOffsetX(0);
            frame.setCloseable(false);
            frame.setX(x);
            frame.setY(y);
            this.addChild(frame);
            nexty = Math.max(y + frame.getHeight() + 10, nexty);
            if (!((float)(x += frame.getWidth() + 10) > (float)Wrapper.getMinecraft().field_71443_c / 1.2f)) continue;
            nexty = y = nexty;
        }
        this.addMouseListener(new MouseListener(){

            private boolean isNotBetweenNullAnd(int val, int max) {
                return val > max || val < 0;
            }

            @Override
            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                List<SettingsPanel> panels = ContainerHelper.getAllChildren(SettingsPanel.class, KamiGUI.this);
                for (SettingsPanel settingsPanel : panels) {
                    if (!settingsPanel.isVisible()) continue;
                    int[] real = GUI.calculateRealPosition(settingsPanel);
                    int pX = event.getX() - real[0];
                    int pY = event.getY() - real[1];
                    if (!this.isNotBetweenNullAnd(pX, settingsPanel.getWidth()) && !this.isNotBetweenNullAnd(pY, settingsPanel.getHeight())) continue;
                    settingsPanel.setVisible(false);
                }
            }

            @Override
            public void onMouseRelease(MouseListener.MouseButtonEvent event) {
            }

            @Override
            public void onMouseDrag(MouseListener.MouseButtonEvent event) {
            }

            @Override
            public void onMouseMove(MouseListener.MouseMoveEvent event) {
            }

            @Override
            public void onScroll(MouseListener.MouseScrollEvent event) {
            }
        });
        ArrayList<Frame> frames = new ArrayList<Frame>();
        Frame frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Active modules");
        frame.setCloseable(false);
        frame.addChild(new ActiveModules());
        frame.setPinneable(true);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Info");
        frame.setCloseable(false);
        frame.setPinneable(true);
        Label information = new Label("");
        information.setShadow(true);
        information.addTickListener(() -> {
            information.setText("");
            StringBuilder stringBuilder = new StringBuilder().append(KamiMod.getInstance().guiManager.getTextColor()).append(Math.round(LagCompensator.INSTANCE.getTickRate())).append(ChatFormatting.DARK_GRAY.toString()).append(" tps  |  ").append(KamiMod.getInstance().guiManager.getTextColor());
            Wrapper.getMinecraft();
            information.addLine(stringBuilder.append(Minecraft.field_71470_ab).append(ChatFormatting.DARK_GRAY.toString()).append(" fps").toString());
        });
        frame.addChild(information);
        information.setFontRenderer(fontRenderer);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Text Radar");
        Label list = new Label("");
        DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        StringBuilder healthSB = new StringBuilder();
        StringBuilder potsSB = new StringBuilder();
        list.addTickListener(() -> {
            if (!list.isVisible()) {
                return;
            }
            list.setText("");
            Minecraft mc = Wrapper.getMinecraft();
            if (mc.field_71439_g == null) {
                return;
            }
            List entityList = mc.field_71441_e.field_73010_i;
            Map<String, Integer> players = new HashMap();
            int playerStep = 0;
            for (Entity entity : entityList) {
                if (playerStep >= KamiMod.getInstance().guiManager.getTextRadarPlayers()) break;
                if (entity.func_70005_c_().equals(mc.field_71439_g.func_70005_c_())) continue;
                EntityPlayer entityPlayer = (EntityPlayer)entity;
                String posString = entityPlayer.field_70163_u > mc.field_71439_g.field_70163_u ? ChatFormatting.DARK_GREEN.toString() + "+" : (entityPlayer.field_70163_u == mc.field_71439_g.field_70163_u ? " " : ChatFormatting.DARK_RED.toString() + "-");
                float hpRaw = entityPlayer.func_110143_aJ() + entityPlayer.func_110139_bj();
                String hp = dfHealth.format(hpRaw);
                if (hpRaw >= 20.0f) {
                    healthSB.append(ChatFormatting.GREEN.toString());
                } else if (hpRaw >= 10.0f) {
                    healthSB.append(ChatFormatting.YELLOW.toString());
                } else if (hpRaw >= 5.0f) {
                    healthSB.append(ChatFormatting.GOLD.toString());
                } else {
                    healthSB.append(ChatFormatting.RED.toString());
                }
                healthSB.append(hp);
                healthSB.append(" ");
                if (KamiMod.getInstance().guiManager.isTextRadarPots()) {
                    int duration;
                    PotionEffect effectweakness;
                    int duration2;
                    PotionEffect effectStrength = entityPlayer.func_70660_b(MobEffects.field_76420_g);
                    if (effectStrength != null && entityPlayer.func_70644_a(MobEffects.field_76420_g) && (duration2 = effectStrength.func_76459_b()) > 0) {
                        potsSB.append((Object)ChatFormatting.RED);
                        potsSB.append(" S ");
                        potsSB.append((Object)ChatFormatting.GRAY);
                        potsSB.append(Potion.func_188410_a((PotionEffect)effectStrength, (float)1.0f));
                    }
                    if ((effectweakness = entityPlayer.func_70660_b(MobEffects.field_76437_t)) != null && entityPlayer.func_70644_a(MobEffects.field_76437_t) && (duration = effectweakness.func_76459_b()) > 0) {
                        potsSB.append((Object)ChatFormatting.GOLD);
                        potsSB.append(" W ");
                        potsSB.append((Object)ChatFormatting.GRAY);
                        potsSB.append(Potion.func_188410_a((PotionEffect)effectweakness, (float)1.0f));
                    }
                }
                String nameColor = Friends.isFriend(entity.func_70005_c_()) ? ChatFormatting.GREEN.toString() : ChatFormatting.GRAY.toString();
                players.put(ChatFormatting.GRAY.toString() + posString + " " + healthSB.toString() + nameColor + entityPlayer.func_70005_c_() + potsSB.toString(), (int)mc.field_71439_g.func_70032_d((Entity)entityPlayer));
                healthSB.setLength(0);
                potsSB.setLength(0);
                ++playerStep;
            }
            if (players.isEmpty()) {
                list.setText("");
                return;
            }
            players = KamiGUI.sortByValue(players);
            for (Map.Entry entry : players.entrySet()) {
                list.addLine((String)entry.getKey() + " " + ChatFormatting.DARK_GRAY.toString() + entry.getValue());
            }
        });
        frame.setCloseable(false);
        frame.setPinneable(true);
        frame.setMinimumWidth(75);
        list.setShadow(true);
        frame.addChild(list);
        list.setFontRenderer(fontRenderer);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Entities");
        final Label entityLabel = new Label("");
        frame.setCloseable(false);
        entityLabel.addTickListener(new TickListener(){
            Minecraft mc = Wrapper.getMinecraft();

            @Override
            public void onTick() {
                if (this.mc.field_71439_g == null || !entityLabel.isVisible()) {
                    return;
                }
                ArrayList entityList = new ArrayList(this.mc.field_71441_e.field_72996_f);
                if (entityList.size() <= 1) {
                    entityLabel.setText("");
                    return;
                }
                Map<String, Integer> entityCounts = entityList.stream().filter(Objects::nonNull).filter(e -> !(e instanceof EntityPlayer)).collect(Collectors.groupingBy(x$0 -> KamiGUI.getEntityName(x$0), Collectors.reducing(0, ent -> {
                    if (ent instanceof EntityItem) {
                        return ((EntityItem)ent).func_92059_d().func_190916_E();
                    }
                    return 1;
                }, Integer::sum)));
                entityLabel.setText("");
                entityCounts.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(entry -> (Object)TextFormatting.GRAY + (String)entry.getKey() + " " + (Object)TextFormatting.DARK_GRAY + "x" + entry.getValue()).forEach(entityLabel::addLine);
            }
        });
        frame.addChild(entityLabel);
        frame.setPinneable(true);
        entityLabel.setShadow(true);
        entityLabel.setFontRenderer(fontRenderer);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Coordinates");
        frame.setCloseable(false);
        frame.setPinneable(true);
        final Label coordsLabel = new Label("");
        coordsLabel.addTickListener(new TickListener(){
            Minecraft mc = Minecraft.func_71410_x();

            @Override
            public void onTick() {
                boolean inHell = this.mc.field_71441_e.func_180494_b(this.mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
                int posX = (int)this.mc.field_71439_g.field_70165_t;
                int posY = (int)this.mc.field_71439_g.field_70163_u;
                int posZ = (int)this.mc.field_71439_g.field_70161_v;
                float f = !inHell ? 0.125f : 8.0f;
                int hposX = (int)(this.mc.field_71439_g.field_70165_t * (double)f);
                int hposZ = (int)(this.mc.field_71439_g.field_70161_v * (double)f);
                coordsLabel.setText(String.format(" %sf%,d%s7, %sf%,d%s7, %sf%,d %s7(%sf%,d%s7, %sf%,d%s7, %sf%,d%s7)", Character.valueOf(Command.SECTIONSIGN()), posX, Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), posY, Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), posZ, Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), hposX, Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), posY, Character.valueOf(Command.SECTIONSIGN()), Character.valueOf(Command.SECTIONSIGN()), hposZ, Character.valueOf(Command.SECTIONSIGN())));
            }
        });
        frame.addChild(coordsLabel);
        coordsLabel.setFontRenderer(fontRenderer);
        coordsLabel.setShadow(true);
        frame.setHeight(20);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Radar");
        frame.setCloseable(false);
        frame.setMinimizeable(true);
        frame.setPinneable(true);
        frame.addChild(new Radar());
        frame.setWidth(100);
        frame.setHeight(100);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Greeter");
        frame.setCloseable(false);
        frame.setMinimizeable(true);
        frame.setPinneable(true);
        Label greeter = new Label("");
        greeter.addTickListener(() -> {
            if (Wrapper.getMinecraft().field_71439_g == null) {
                return;
            }
            greeter.setText("");
            greeter.addLine(ChatFormatting.DARK_GRAY.toString() + "Greetings " + KamiMod.getInstance().guiManager.getTextColor() + Wrapper.getMinecraft().field_71439_g.getDisplayNameString() + ChatFormatting.DARK_GRAY.toString() + " OwO");
        });
        frame.addChild(greeter);
        greeter.setFontRenderer(fontRenderer);
        greeter.setShadow(true);
        frames.add(frame);
        frame = new Frame(this.getTheme(), new Stretcherlayout(1), "Watermark");
        frame.setCloseable(false);
        frame.setMinimizeable(true);
        frame.setPinneable(true);
        Label watermark = new Label("");
        watermark.addTickListener(() -> {
            if (Wrapper.getMinecraft().field_71439_g == null) {
                return;
            }
            watermark.setText("");
            watermark.addLine(KamiMod.getInstance().guiManager.getTextColor() + "\u722a\u4e47\u15ea\u3129\u4e59\u5342");
        });
        frame.addChild(watermark);
        watermark.setFontRenderer(fontRenderer);
        watermark.setShadow(true);
        frames.add(frame);
        for (Frame frame1 : frames) {
            frame1.setX(x);
            frame1.setY(y);
            nexty = Math.max(y + frame1.getHeight() + 10, nexty);
            x += frame1.getWidth() + 10;
            if ((float)(x * DisplayGuiScreen.getScale()) > (float)Wrapper.getMinecraft().field_71443_c / 1.2f) {
                nexty = y = nexty;
                x = 10;
            }
            this.addChild(frame1);
        }
    }

    @Override
    public void destroyGUI() {
        this.kill();
    }
}

