package org.brewchain.rcvm.jsonrpc;

import static org.brewchain.rcvm.jsonrpc.TypeConverter.toJsonHex;

import org.brewchain.evmapi.gens.Block;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;

public class TransactionReceiptDTOExt extends TransactionReceiptDTO {

    public String returnData;
    public String error;

    public TransactionReceiptDTOExt(Block block, MultiTransaction txInfo) {
        super(block, txInfo);
        throw new RuntimeException("该方法待实现");
//        returnData = toJsonHex(txInfo.getReceipt().getExecutionResult());
//        error = txInfo.getReceipt().getError();
    }
}
