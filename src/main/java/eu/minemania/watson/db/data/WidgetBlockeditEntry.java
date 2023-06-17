package eu.minemania.watson.db.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import eu.minemania.watson.client.Teleport;
import eu.minemania.watson.selection.PlayereditUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntrySortable;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class WidgetBlockeditEntry extends WidgetListEntrySortable<BlockeditEntry>
{
    private static final String[] HEADERS = new String[]{
            "watson.gui.label.blockedit.title.action",
            "watson.gui.label.blockedit.title.time",
            "watson.gui.label.blockedit.title.coords",
            "watson.gui.label.blockedit.title.world",
            "watson.gui.label.blockedit.title.amount",
            "watson.gui.label.blockedit.title.description"
    };
    private static int maxActionLength;
    private static int maxTimeLength;
    private static int maxCoordsLength;
    private static int maxWorldLength;
    private static int maxAmountLength;
    private static int maxDescriptionLength;

    @Nullable
    private final BlockeditEntry entry;
    @Nullable
    private final String header1;
    @Nullable
    private final String header2;
    @Nullable
    private final String header3;
    @Nullable
    private final String header4;
    @Nullable
    private final String header5;
    @Nullable
    private final String header6;
    private final boolean isOdd;

    public WidgetBlockeditEntry(int x, int y, int width, int height, boolean isOdd,
                                @Nullable BlockeditEntry entry, int listIndex)
    {
        super(x, y, width, height, entry, listIndex);

        this.columnCount = 1;
        this.entry = entry;
        this.isOdd = isOdd;
        int posX = x + width;
        int posY = y + 1;

        if (this.entry != null)
        {
            this.header1 = null;
            this.header2 = null;
            this.header3 = null;
            this.header4 = null;
            this.header5 = null;
            this.header6 = null;
            this.entry.setButton(this.createButtonGeneric(posX, posY, WidgetBlockeditEntry.ButtonListenerTeleport.ButtonType.TELEPORT));
        }
        else
        {
            this.header1 = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[0]) + GuiBase.TXT_RST;
            this.header2 = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[1]) + GuiBase.TXT_RST;
            this.header3 = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[2]) + GuiBase.TXT_RST;
            this.header4 = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[3]) + GuiBase.TXT_RST;
            this.header5 = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[4]) + GuiBase.TXT_RST;
            this.header6 = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[5]) + GuiBase.TXT_RST;
        }
    }

    private ButtonBase createButtonGeneric(int xRight, int y, WidgetBlockeditEntry.ButtonListenerTeleport.ButtonType type)
    {
        String label = type.getDisplayName();
        WidgetBlockeditEntry.ButtonListenerTeleport listener = new WidgetBlockeditEntry.ButtonListenerTeleport(type, this.entry);
        return this.addButton(new ButtonGeneric(xRight, y, -1, true, label), listener);
    }

    public static void setMaxNameLength(List<BlockeditEntry> edits)
    {
        maxActionLength = StringUtils.getStringWidth(GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[0]) + GuiBase.TXT_RST);
        maxTimeLength = StringUtils.getStringWidth(GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[1]) + GuiBase.TXT_RST);
        maxCoordsLength = StringUtils.getStringWidth(GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[2]) + GuiBase.TXT_RST);
        maxWorldLength = StringUtils.getStringWidth(GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[3]) + GuiBase.TXT_RST);
        maxAmountLength = StringUtils.getStringWidth(GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[4]) + GuiBase.TXT_RST);
        maxDescriptionLength = StringUtils.getStringWidth(GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[5]) + GuiBase.TXT_RST);

        for (BlockeditEntry entry : edits)
        {
            maxActionLength = Math.max(maxActionLength, StringUtils.getStringWidth(PlayereditUtils.blockString(entry.getEdit(), PlayereditUtils.Edit.ACTION)));
            maxTimeLength = Math.max(maxTimeLength, StringUtils.getStringWidth(PlayereditUtils.blockString(entry.getEdit(), PlayereditUtils.Edit.TIME)));
            maxCoordsLength = Math.max(maxCoordsLength, StringUtils.getStringWidth(PlayereditUtils.blockString(entry.getEdit(), PlayereditUtils.Edit.COORDS)));
            maxWorldLength = Math.max(maxWorldLength, StringUtils.getStringWidth(PlayereditUtils.blockString(entry.getEdit(), PlayereditUtils.Edit.WORLD)));
            maxAmountLength = Math.max(maxAmountLength, StringUtils.getStringWidth(PlayereditUtils.blockString(entry.getEdit(), PlayereditUtils.Edit.AMOUNT)));
            maxDescriptionLength = Math.max(maxDescriptionLength, StringUtils.getStringWidth(PlayereditUtils.blockString(entry.getEdit(), PlayereditUtils.Edit.DESCRIPTION)));
        }
    }

    @Override
    public boolean canSelectAt(int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext)
    {
        RenderUtils.color(1f, 1f, 1f, 1f);

        if (this.header1 == null && (selected || this.isMouseOver(mouseX, mouseY)))
        {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
        }
        else if (this.isOdd)
        {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
        }
        else
        {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x50FFFFFF);
        }

        int x1 = this.getColumnPosX(0);
        int x2 = this.getColumnPosX(1);
        int x3 = this.getColumnPosX(2);
        int x4 = this.getColumnPosX(3);
        int x5 = this.getColumnPosX(4);
        int x6 = this.getColumnPosX(5);
        int y = this.y + 7;
        int color = 0xFFFFFFFF;

        if (this.header1 != null)
        {
            this.drawString(x1, y, color, this.header1, drawContext);
            this.drawString(x2, y, color, this.header2, drawContext);
            this.drawString(x3, y, color, this.header3, drawContext);
            this.drawString(x4, y, color, this.header4, drawContext);
            this.drawString(x5, y, color, this.header5, drawContext);
            this.drawString(x6, y, color, this.header6, drawContext);
        }
        else if (this.entry != null)
        {
            String action = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.ACTION);
            String time = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.TIME);
            String coords = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.COORDS);
            String world = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.WORLD);
            String amount = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.AMOUNT);
            String description = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.DESCRIPTION);
            if (x6 + StringUtils.getStringWidth(description) > width - this.entry.getButton().getWidth() + 15)
            {
                description = this.textRenderer.trimToWidth(description, width - x6 - this.entry.getButton().getWidth() + 5).concat("...");
            }
            this.drawString(x1, y, 0xFFFFFFFF, action, drawContext);
            this.drawString(x2, y, 0xFFFFFFFF, time, drawContext);
            this.drawString(x3, y, 0xFFFFFFFF, coords, drawContext);
            this.drawString(x4, y, 0xFFFFFFFF, world, drawContext);
            this.drawString(x5, y, 0xFFFFFFFF, amount, drawContext);
            this.drawString(x6, y, 0xFFFFFFFF, description, drawContext);

            super.render(mouseX, mouseY, selected, drawContext);
        }
    }

    @Override
    public void postRenderHovered(int mouseX, int mouseY, boolean selected, DrawContext drawContext)
    {
        if (this.entry != null)
        {
            MatrixStack matrixStack = drawContext.getMatrices();

            String descriptionText = PlayereditUtils.blockString(this.entry.getEdit(), PlayereditUtils.Edit.DESCRIPTION);
            if (mouseX > this.getColumnPosX(5) && this.getColumnPosX(5) + StringUtils.getStringWidth(descriptionText) > width - this.entry.getButton().getWidth() + 15)
            {
                matrixStack.push();
                matrixStack.translate(0, 0, 200);
                RenderSystem.applyModelViewMatrix();
                String header = GuiBase.TXT_BOLD + StringUtils.translate(HEADERS[5]);
                int w1 = this.getStringWidth(header);
                int w2 = this.width - 100;
                int totalWidth = Math.max(w1, w2);
                List<String> descriptions = new ArrayList<>();
                StringUtils.splitTextToLines(descriptions, descriptionText, totalWidth);
                for (String description : descriptions)
                {
                    w1 = Math.max(w1, StringUtils.getStringWidth(description));
                }
                int x = mouseX + 10;
                int y = mouseY - 10;
                if (x + w1 - 20 >= this.width)
                {
                    x -= w1 + 40;
                }

                int x1 = x + 10;

                RenderUtils.drawOutlinedBox(x, y, w1 + 20, 60, 0xFF000000, GuiBase.COLOR_HORIZONTAL_BAR);
                y += 6;
                y += 4;

                this.drawString(x1, y, 0xFFFFFFFF, header, drawContext);
                y += 16;

                for (int i = 0; i < descriptions.size(); i++)
                {
                    this.drawString(x1, y + (i * 8) - 7, 0xFFFFFFFF, descriptions.get(i), drawContext);
                }
                matrixStack.pop();
            }
            if (mouseX < this.getColumnPosX(5))
            {
                if (this.entry.getEdit().getAdditional() == null || this.entry.getEdit().getAdditional().isEmpty())
                {
                    return;
                }
                matrixStack.push();
                matrixStack.translate(0, 0, 200);
                RenderSystem.applyModelViewMatrix();
                int w1 = 0;
                int w2 = 0;
                for (Map.Entry<?,?> entry : this.entry.getEdit().getAdditional().entrySet())
                {
                    w1 = Math.max(w1, this.getStringWidth(String.valueOf(entry.getKey())));
                    w2 = Math.max(w2, this.getStringWidth(String.valueOf(entry.getValue())));
                }

                int totalWidth = w1 + w2 + 60;
                int x = mouseX + 10;
                int y = mouseY - 10;
                if (x + totalWidth - 20 >= this.width)
                {
                    x -= totalWidth + 40;
                }

                int x1 = x + 10;
                int x2 = x1 + w1 + 20;

                RenderUtils.drawOutlinedBox(x, y, totalWidth, 60, 0xFF000000, GuiBase.COLOR_HORIZONTAL_BAR);
                y += 10;

                for (Map.Entry<?,?> entry : this.entry.getEdit().getAdditional().entrySet())
                {
                    this.drawString(x1, y, 0xFFFFFFFF, String.valueOf(entry.getKey()), drawContext);
                    this.drawString(x2, y, 0xFFFFFFFF, String.valueOf(entry.getValue()), drawContext);
                    y += 16;
                }
                matrixStack.pop();
            }
        }
    }

    static class ButtonListenerTeleport implements IButtonActionListener
    {
        private final WidgetBlockeditEntry.ButtonListenerTeleport.ButtonType type;
        private final BlockeditEntry entry;

        public ButtonListenerTeleport(WidgetBlockeditEntry.ButtonListenerTeleport.ButtonType type, BlockeditEntry entry)
        {
            this.type = type;
            this.entry = entry;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            if (this.type == WidgetBlockeditEntry.ButtonListenerTeleport.ButtonType.TELEPORT)
            {
                Teleport.teleport(entry.getEdit().x, entry.getEdit().y, entry.getEdit().z, entry.getEdit().world);
            }
        }

        public enum ButtonType
        {
            TELEPORT("watson.gui.label.blockedit.list.teleport");

            private final String translationKey;

            ButtonType(String translationKey)
            {
                this.translationKey = translationKey;
            }

            public String getDisplayName()
            {
                return StringUtils.translate(this.translationKey);
            }
        }
    }

    @Override
    protected int getColumnPosX(int column)
    {
        int x1 = this.x + 4;
        int x2 = x1 + maxActionLength + 20;
        int x3 = x2 + maxTimeLength + 20;
        int x4 = x3 + maxCoordsLength + 20;
        int x5 = x4 + maxWorldLength + 20;
        int x6 = x5 + maxAmountLength + 20;

        return switch (column)
                {
                    case 0 -> x1;
                    case 1 -> x2;
                    case 2 -> x3;
                    case 3 -> x4;
                    case 4 -> x5;
                    case 5 -> x6;
                    default -> x1;
                };
    }

    @Override
    protected int getCurrentSortColumn()
    {
        return 0;
    }

    @Override
    protected boolean getSortInReverse()
    {
        return false;
    }
}
