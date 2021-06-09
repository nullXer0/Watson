package eu.minemania.watson.network.ledger;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import eu.minemania.watson.Watson;
import eu.minemania.watson.config.Configs;
import eu.minemania.watson.db.BlockEdit;
import eu.minemania.watson.db.WatsonBlock;
import eu.minemania.watson.db.WatsonBlockRegistery;
import eu.minemania.watson.scheduler.SyncTaskQueue;
import eu.minemania.watson.scheduler.tasks.AddBlockEditTask;
import fi.dy.masa.malilib.network.IPluginChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class PluginDeltaLoggerTransactionPacketHandler implements IPluginChannelHandler
{
    public static final List<Identifier> CHANNELS = ImmutableList.of(new Identifier("ledger:action"));

    public static final PluginDeltaLoggerTransactionPacketHandler INSTANCE = new PluginDeltaLoggerTransactionPacketHandler();

    private boolean registered;

    public void reset()
    {
        registered = false;
    }

    @Override
    public List<Identifier> getChannels()
    {
        return CHANNELS;
    }

    @Override
    public void onPacketReceived(PacketByteBuf buf)
    {
        if (!buf.toString(Charsets.UTF_8).isEmpty())
        {
            this.registered = true;
        }

        if (this.registered)
        {
            System.out.println(buf.toString(Charsets.UTF_8));
            //BlockPos pos = buf.readBlockPos();
            //String type = buf.readString();
            //Identifier dim = buf.readIdentifier();
            //Identifier oldObj = buf.readIdentifier();
            Identifier newObj = buf.readIdentifier();
            String source = buf.readString();
            //Long time = buf.readLong();
            //String additional = buf.readString();

            //System.out.println(pos.toString());
            //System.out.println(type);
            //System.out.println(dim.toString());
            //System.out.println(oldObj.toString());
            System.out.println(newObj.toString());
            System.out.println(source);
            //System.out.println(time);
            //System.out.println(additional);
            /*int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            String player = buf.readString();
            long time = buf.readLong();
            int amount = buf.readInt();
            String itemType = buf.readString();
            String world = buf.readString();
            String action = amount < 0 ? "took" : "put";
            WatsonBlock block = WatsonBlockRegistery.getInstance().getWatsonBlockByName(itemType);
            long actualTime = time * 1000;
            amount = Math.abs(amount);

            if (Configs.Generic.DEBUG.getBooleanValue())
            {
                Watson.logger.debug("x:" + x);
                Watson.logger.debug("y:" + y);
                Watson.logger.debug("z:" + z);
                Watson.logger.debug("playername:" + player);
                Watson.logger.debug("time:" + time);
                Watson.logger.debug("actualTime:" + actualTime);
                Watson.logger.debug("amount:" + amount);
                Watson.logger.debug("itemtype:" + itemType);
                Watson.logger.debug("world:" + world);
                Watson.logger.debug("action:" + action);
                Watson.logger.debug("block:" + block.getName());
                Watson.logger.debug("");
            }

            BlockEdit edit = new BlockEdit(actualTime, player, action, x, y, z, block, world, amount);
            SyncTaskQueue.getInstance().addTask(new AddBlockEditTask(edit, false));*/
        }
    }

    @Override
    public PacketByteBuf onPacketSend()
    {
        return null;
    }
}