package org.brewchain.rcvm.base;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.brewchain.account.core.AccountHelper;
import org.brewchain.account.core.TransactionHelper;
import org.brewchain.account.gens.Tx.MultiTransaction;
import org.brewchain.account.gens.Tx.MultiTransactionBody;
import org.brewchain.account.gens.Tx.MultiTransactionInput;
import org.brewchain.account.gens.Tx.MultiTransactionOutput;
import org.brewchain.account.gens.Tx.MultiTransactionSignature;
import org.brewchain.evm.api.EvmApi;
import org.fc.brewchain.bcapi.EncAPI;
import org.spongycastle.util.encoders.Hex;

import com.google.protobuf.ByteString;

public class MTransaction {
	
	private MultiTransaction.Builder tx;
	private MultiTransactionBody.Builder txBody = MultiTransactionBody.newBuilder();
	private String token;
	
//	private AccountHelper accountHelper;
	private EvmApi evmApi;
	
	
	/**
	 * 创建交易对象
	 * @param accountHelper
	 */
//	public MTransaction(AccountHelper accountHelper) {
	public MTransaction(EvmApi evmApi) {
		
		this.tx = MultiTransaction.newBuilder();
		this.tx.setTxHash(ByteString.EMPTY);
		this.txBody = MultiTransactionBody.newBuilder();
		this.txBody.clearSignatures();

		this.evmApi = evmApi;
	}

	/**
	 * 创建TOKEN交易对象
	 * @param token
	 * @param accountHelper
	 */
	public MTransaction(String token,EvmApi evmApi) {
	//	public MTransaction(String token,AccountHelper accountHelper) {
		
		this.token = token;
		
		this.tx = MultiTransaction.newBuilder();
		this.tx.setTxHash(ByteString.EMPTY);
		this.txBody = MultiTransactionBody.newBuilder();
		this.txBody.clearSignatures();

		this.evmApi = evmApi;
	}
	
	/**
	 * 向交易对象中增加一个input
	 * @param address
	 * @param pubKey
	 * @param sign 
	 * @param value
	 * @param fee
	 * @param feeLimit
	 * @throws Exception
	 */
	public void addTXInput(String address, String pubKey, String sign, long value, long fee, long feeLimit) throws Exception {
		
		MultiTransactionInput.Builder txInput = MultiTransactionInput.newBuilder();
		txInput.setAddress(ByteString.copyFrom(Hex.decode(address)));
		txInput.setAmount(value);
		txInput.setFee((int)fee);
		txInput.setFeeLimit((int)feeLimit);
		txInput.setNonce(evmApi.getNonce(Hex.decode(address)));
		if(StringUtils.isNotBlank(token)) txInput.setToken(token);
		this.txBody.addInputs(txInput);
		
		// 签名，多重签名时，每个账户对原始交易签名字符串
		MultiTransactionSignature.Builder signature = MultiTransactionSignature.newBuilder();
		signature.setPubKey(pubKey);
		signature.setSignature(sign);
		this.txBody.addSignatures(signature);
		
    }
	
	/**
	 * 向交易对象中增加一个output
	 * @param address 创建合约交易address传null或""
	 * @param value
	 */
    public void addTXOutput(String address, long value) {
    		if(StringUtils.isNotBlank(address)) {
    			//合约时不能addOutputs
	    		MultiTransactionOutput.Builder TXOutput = MultiTransactionOutput.newBuilder();
	    		TXOutput.setAddress(ByteString.copyFrom(Hex.decode(address)));
	    		TXOutput.setAmount(value);
	    		this.txBody.addOutputs(TXOutput);
    		}
    }
    
    /**
     * 获取交易对象
     * @param data
     * @param exData
     * @return
     */
    public void setTXBodyData(byte[] data, byte[] exData) {
    		if(data != null) this.txBody.setData(ByteString.copyFrom(data));
    		if(exData != null) this.txBody.setExdata(ByteString.copyFrom(exData));
    }
    
    /**
     * 获取交易对象
     * @param data
     * @param exData
     * @return
     */
    public MultiTransaction.Builder genTX() {
    		tx.clearTxBody();
    		if(StringUtils.isNotBlank(token)) this.txBody.setData(ByteString.copyFromUtf8("02"));
    		txBody.setTimestamp(new Date().getTime());
		this.tx.setTxBody(this.txBody);
		return tx;
    }
    
    public void txSign(EncAPI encAPI,String pubKey,String priKey) {
    		if(StringUtils.isNotBlank(priKey)) {
    			txBody.clearSignatures();
    			// 签名
    			MultiTransactionSignature.Builder txSignature = MultiTransactionSignature.newBuilder();
    			txSignature.setPubKey(pubKey);
    			txSignature.setSignature(encAPI.hexEnc(encAPI.ecSign(priKey, txBody.build().toByteArray())));
    			txBody.addSignatures(txSignature);
    		}
    }
    public void setSign(String pubKey,String  sign) {
    		txBody.clearSignatures();
		// 签名
		MultiTransactionSignature.Builder txSignature = MultiTransactionSignature.newBuilder();
		txSignature.setPubKey(pubKey);
		txSignature.setSignature(sign);
    }
    
    /**
     * 发起交易
     * @param transactionHelper
     * @param data
     * @param exData
     * @throws Exception
     */
    public void sendTX(EvmApi evmApi) throws Exception {
//		if(txBody.getInputsList().size() == 0) {
//			//合约交易，无addOutputs
//		}else if(StringUtils.isNotBlank(token)){
//			// erc20交易
//		}else {
//		}
    		tx.clearTxBody();
    		if(StringUtils.isNotBlank(token)) this.txBody.setData(ByteString.copyFromUtf8("02"));
    		txBody.setTimestamp(new Date().getTime());
		this.tx.setTxBody(this.txBody);
		//evmApi.CreateMultiTransaction(tx);	
    }
    
    
//    public Account getAccount(String addr) {
//    		Account account = accountHelper.GetAccount(Hex.decode(addr));
//    		return account;
//    }
	
}
