/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.brewchain.rcvm.exec.invoke;

import java.math.BigInteger;

//import org.apache.maven.model.Repository;
import org.brewchain.evmapi.gens.Block;
import org.brewchain.evmapi.gens.Block.BlockHeader;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.base.DataWord;
import org.brewchain.rcvm.program.Program;


public interface ProgramInvokeFactory {

    ProgramInvoke createProgramInvoke(MultiTransaction tx, Block block,
                                      EvmApi repository, BlockHeader blockStore);

    ProgramInvoke createProgramInvoke(Program program, DataWord toAddress, DataWord callerAddress,
                                             DataWord inValue, DataWord inGas,
                                             BigInteger balanceInt, byte[] dataIn,
                                             EvmApi repository, BlockHeader blockStore,
                                            boolean staticCall, boolean byTestingSuite);


}
