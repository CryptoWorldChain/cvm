
package org.brewchain.rcvm.exec.invoke;

//import org.apache.maven.model.Repository;
import org.brewchain.account.gens.Block.BlockHeader;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.base.DataWord;

public interface ProgramInvoke {

    DataWord getOwnerAddress();

    DataWord getBalance();

    DataWord getOriginAddress();

    DataWord getCallerAddress();

    DataWord getMinGasPrice();

    DataWord getGas();

    long getGasLong();

    DataWord getCallValue();

    DataWord getDataSize();

    DataWord getDataValue(DataWord indexData);

    byte[] getDataCopy(DataWord offsetData, DataWord lengthData);

    DataWord getPrevHash();

    DataWord getCoinbase();

    DataWord getTimestamp();

    DataWord getNumber();

    DataWord getDifficulty();

    DataWord getGaslimit();

    boolean byTransaction();

    boolean byTestingSuite();

    int getCallDeep();

    EvmApi getRepository();

    BlockHeader getBlockStore();

    boolean isStaticCall();
}
