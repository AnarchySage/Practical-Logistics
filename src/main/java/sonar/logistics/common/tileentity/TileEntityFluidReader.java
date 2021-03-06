package sonar.logistics.common.tileentity;

import net.minecraftforge.common.util.ForgeDirection;
import sonar.core.api.BlockCoords;
import sonar.core.common.tileentity.TileEntityHandler;
import sonar.core.integration.fmp.FMPHelper;
import sonar.logistics.api.LogisticsAPI;
import sonar.logistics.api.connecting.IInfoEmitter;
import sonar.logistics.api.info.ILogicInfo;
import sonar.logistics.common.handlers.FluidReaderHandler;

public class TileEntityFluidReader extends TileEntityHandler implements IInfoEmitter {

	public FluidReaderHandler handler;

	@Override
	public FluidReaderHandler getTileHandler() {
		if (handler == null) {
			handler = new FluidReaderHandler(false, this);
		}
		return handler;
	}

	@Override
	public boolean canConnect(ForgeDirection dir) {
		return getTileHandler().canConnect(this, dir);
	}

	@Override
	public ILogicInfo currentInfo() {
		return getTileHandler().currentInfo(this);
	}

	public boolean maxRender() {
		return true;
	}

	@Override
	public BlockCoords getCoords() {
		return new BlockCoords(this);
	}

	public void onLoaded() {
		super.onLoaded();
		if (!this.worldObj.isRemote) {
			addConnections();
		}
	}

	public void invalidate() {
		if (!this.worldObj.isRemote) {
			removeConnections();
		}
		super.invalidate();
	}

	@Override
	public void addConnections() {
		LogisticsAPI.getCableHelper().addConnection(this, ForgeDirection.getOrientation(FMPHelper.getMeta(this)));
	}

	@Override
	public void removeConnections() {
		LogisticsAPI.getCableHelper().removeConnection(this, ForgeDirection.getOrientation(FMPHelper.getMeta(this)));
	}

}
