package org.brewchain.rcvm.exec.invoke;

import java.math.BigInteger;

//import org.apache.maven.model.Repository;
import org.brewchain.evmapi.gens.Block;
import org.brewchain.evmapi.gens.Block.BlockEntity;
import org.brewchain.evmapi.gens.Block.BlockHeader;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.base.DataWord;
import org.brewchain.rcvm.program.Program;


public interface ProgramInvokeFactory {

    ProgramInvoke createProgramInvoke(MultiTransaction tx, BlockEntity block,
                                      EvmApi repository);

    ProgramInvoke createProgramInvoke(Program program, DataWord toAddress, DataWord callerAddress,
                                             DataWord inValue,
                                             BigInteger balanceInt, byte[] dataIn,
                                             EvmApi repository,
                                            boolean staticCall, boolean byTestingSuite);


}
