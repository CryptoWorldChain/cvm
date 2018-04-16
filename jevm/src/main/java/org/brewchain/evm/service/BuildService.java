package org.brewchain.evm.service;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.cvm.pbgens.Cvm.PCommand;
import org.brewchain.cvm.pbgens.Cvm.PModule;
import org.brewchain.cvm.pbgens.Cvm.PRetBuild;
import org.brewchain.cvm.pbgens.Cvm.PSBuildCode;
import org.brewchain.evm.utils.VMUtil;
import org.fc.brewchain.bcapi.EncAPI;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import onight.oapi.scala.commons.SessionModules;
import onight.osgi.annotation.NActorProvider;
import onight.tfw.async.CompleteHandler;
import onight.tfw.ntrans.api.annotation.ActorRequire;
import onight.tfw.otransio.api.PacketHelper;
import onight.tfw.otransio.api.beans.FramePacket;

@NActorProvider
@Data
@Slf4j
public class BuildService extends SessionModules<PSBuildCode> {

	@ActorRequire(name = "bc_encoder",scope = "global")
	EncAPI encAPI;
	
	@Override
	public String getModule() {
		return PModule.CVM.name();
	}

	@Override
	public String[] getCmds() {
		return new String[] { PCommand.BCD.name() };
	}

	public String toString() {
		return "BuildService";
	}

	@Override
	public void onPBPacket(FramePacket pack, PSBuildCode pbo, CompleteHandler handler) {
		final PRetBuild.Builder ret = PRetBuild.newBuilder();
		try {
			checkNull(pbo);
			//CompilationResult result = null;
			// IOException
			
			VMUtil.solidCompoler(encAPI,ret,pbo.getCode().getBytes());

		} catch (IllegalArgumentException e) {
			ret.setRetCode(-1);
			ret.setRetMessage(e.getMessage());
			log.error("error：：" + e.getMessage());
		} catch (UnknownError e) {
			ret.setRetCode(-1);
			ret.setRetMessage(e.getMessage());
			log.error("error：：" + e.getMessage());
		} catch (Exception e) {
			ret.setRetCode(-1);
			ret.setRetMessage(e.getMessage());
			log.error("error：：" + e.getMessage());
		} finally {

		}
		handler.onFinished(PacketHelper.toPBReturn(pack, ret.build()));
	}
	
	public void checkNull(PSBuildCode pb) {
		if (pb == null) {
			throw new IllegalArgumentException("无请求参数");
		}
		if (StringUtils.isBlank(pb.getCode())) {
			throw new IllegalArgumentException("参数code,不能为空");
		}
	}

}
