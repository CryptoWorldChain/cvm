
package org.brewchain.rcvm.exec.invoke;

import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.base.DataWord;

public interface ProgramInvoke {

    DataWord getOwnerAddress();

    DataWord getBalance();

    DataWord getOriginAddress();

    DataWord getCallerAddress();




    DataWord getCallValue();

    DataWord getDataSize();

    DataWord getDataValue(DataWord indexData);

    byte[] getDataCopy(DataWord offsetData, DataWord lengthData);

    DataWord getPrevHash();

    DataWord getCoinbase();

    DataWord getTimestamp();

    DataWord getNumber();

    DataWord getDifficulty();


    boolean byTransaction();

    boolean byTestingSuite();

    int getCallDeep();

    EvmApi getRepository();

//    BlockHeader getBlockStore();

    boolean isStaticCall();
}
