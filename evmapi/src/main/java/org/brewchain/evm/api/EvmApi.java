package org.brewchain.evm.api;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.brewchain.evmapi.gens.Act.Account;
import org.brewchain.evmapi.gens.Act.AccountCryptoToken;
import org.brewchain.evmapi.gens.Act.AccountValue;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;
import org.fc.brewchain.bcapi.EncAPI;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public interface EvmApi {

//	public byte[] getBlockHashByNumber(int blockNumber);
	
//	public void saveCode(ByteString code, ByteString address);
//
	public EncAPI getEncApi();
	/**
	 * 获取用户账户
	 * 
	 * @param addr
	 * @return
	 */
	public Account GetAccount(ByteString addr);
//
//	/**
//	 * 创建账户
//	 * 
//	 * @param address
//	 * @param pubKey
//	 * @return
//	 */
//	public Account CreateAccount(ByteString address);
//
//	/**
//	 * 获取用户账户，如果用户不存在，则创建账户
//	 * 
//	 * @param addr
//	 * @return
//	 */
//	public Account GetAccountOrCreate(ByteString addr);
//
	/**
	 * 账户是否存在
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(ByteString addr);
//
//	/**
//	 * Nonce自增1
//	 * 
//	 * @param addr
//	 * @return
//	 * @throws Exception
//	 */
//	public int IncreaseNonce(ByteString addr);
//
	/**
	 * 增加用户账户余额
	 * 
	 * @param addr
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public BigInteger addBalance(ByteString addr, BigInteger balance);
//
	/**
	 * 获取账户余额
	 * 
	 * @param addr
	 * @return
	 */
	public BigInteger getBalance(ByteString addr);
//
//	/**
//	 * 增加用户代币账户余额
//	 * 
//	 * @param addr
//	 * @param balance
//	 * @return
//	 * @throws Exception
//	 */
//	public long addTokenBalance(ByteString addr, String token, long balance);
//
//	public long addTokenLockBalance(ByteString addr, String token, long balance);
//
//	/**
//	 * 增加加密Token账户余额
//	 * 
//	 * @param addr
//	 * @param symbol
//	 * @param token
//	 * @return
//	 * @throws Exception
//	 */
//	public long addCryptoBalance(ByteString addr, String symbol, AccountCryptoToken.Builder token);
//
//	/**
//	 * 设置用户账户Nonce
//	 * 
//	 * @param addr
//	 * @param nonce
//	 * @return
//	 * @throws Exception
//	 */
//	public int setNonce(ByteString addr, int nonce);
//
//	/**
//	 * 是否是合约账户
//	 * 
//	 * @param addr
//	 * @return
//	 */
//	public boolean isContract(ByteString addr);
//
//	/**
//	 * 获取用户账户Nonce
//	 * 
//	 * @param addr
//	 * @return
//	 * @throws Exception
//	 */
//	public int getNonce(ByteString addr);
//
//	/**
//	 * 获取用户Token账户的Balance
//	 * 
//	 * @param addr
//	 * @return
//	 * @throws Exception
//	 */
//	public long getTokenBalance(ByteString addr, ByteString token);
//
//	public long getTokenLockedBalance(ByteString addr, ByteString token);
//
//	/**
//	 * 获取加密Token账户的余额
//	 * 
//	 * @param addr
//	 * @param symbol
//	 * @return
//	 * @throws Exception
//	 */
//	public List<AccountCryptoToken> getCryptoTokenBalance(ByteString addr, ByteString symbol);
//
//	public void ICO(ByteString addr, String token);
//
//	/**
//	 * 判断token是否已经发行
//	 * 
//	 * @param token
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean isExistsToken(String token);
//
//	/**
//	 * 根据交易Hash，返回交易实体。
//	 * 
//	 * @param txHash
//	 * @return
//	 * @throws Exception
//	 */
//	public MultiTransaction GetTransaction(String txHash);
//
//	public byte[] getContractAddressByTransaction(MultiTransaction oMultiTransaction);
//
	public void saveStorage(ByteString address, byte[] key, byte[] value);
	public Map<String, byte[]> getStorage(ByteString address, List<byte[]> keys);
	public byte[] getStorage(ByteString address, byte[] key) ;
}
