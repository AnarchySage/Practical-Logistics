package sonar.logistics.integration.multipart;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.api.BlockCoords;
import sonar.core.integration.fmp.FMPHelper;
import sonar.core.integration.fmp.handlers.TileHandler;
import sonar.logistics.Logistics;
import sonar.logistics.api.LogisticsAPI;
import sonar.logistics.api.connecting.IInfoEmitter;
import sonar.logistics.api.info.ILogicInfo;
import sonar.logistics.client.renderers.RenderHandlers;
import sonar.logistics.common.handlers.EnergyReaderHandler;
import sonar.logistics.integration.multipart.ForgeMultipartHandler.MultiPart;
import sonar.logistics.network.LogisticsGui;
import sonar.logistics.registries.BlockRegistry;
import codechicken.lib.vec.Cuboid6;

public class EnergyReaderPart extends ConnectionPart implements IInfoEmitter {

	public EnergyReaderHandler handler = new EnergyReaderHandler(true, tile());

	public EnergyReaderPart() {
		super();
	}

	public EnergyReaderPart(int meta) {
		super(meta);
	}

	@Override
	public TileHandler getTileHandler() {
		return handler;
	}

	@Override
	public boolean canConnect(ForgeDirection dir) {
		return handler.canConnect(tile(), dir);
	}

	@Override
	public ILogicInfo currentInfo() {
		return handler.currentInfo(tile());
	}

	public boolean activate(EntityPlayer player, MovingObjectPosition pos, ItemStack stack) {
		if (player != null) {
			player.openGui(Logistics.instance, LogisticsGui.energyReader, tile().getWorldObj(), x(), y(), z());
			return true;
		}
		return false;
	}

	@Override
	public Cuboid6 getBounds() {
		if (meta == 2 || meta == 3) {
			return new Cuboid6(6 * 0.0625, 6 * 0.0625, 0.0F, 1.0F - 6 * 0.0625, 1.0F - 6 * 0.0625, 1.0F);
		}
		if (meta == 4 || meta == 5) {
			return new Cuboid6(0.0F, 6 * 0.0625, 6 * 0.0625, 1.0F, 1.0F - 6 * 0.0625, 1.0F - 6 * 0.0625);
		}
		return new Cuboid6(4 * 0.0625, 4 * 0.0625, 4 * 0.0625, 1 - 4 * 0.0625, 1 - 4 * 0.0625, 1 - 4 * 0.0625);
	}

	@Override
	public Object getSpecialRenderer() {
		return new RenderHandlers.EnergyReader();
	}

	@Override
	public MultiPart getPartType() {
		return MultiPart.ENERGY_READER;
	}

	@Override
	public BlockCoords getCoords() {
		return new BlockCoords(tile());
	}

	@Override
	public void addConnections() {
		LogisticsAPI.getCableHelper().addConnection(tile(), ForgeDirection.getOrientation(FMPHelper.getMeta(tile())));
	}

	@Override
	public void removeConnections() {
		LogisticsAPI.getCableHelper().removeConnection(tile(), ForgeDirection.getOrientation(FMPHelper.getMeta(tile())));
	}

	@Override
	public Block getBlock() {
		return BlockRegistry.energyReader;
	}
}
