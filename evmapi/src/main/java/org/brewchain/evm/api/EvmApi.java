package org.brewchain.evm.api;

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

	public void saveCode(String code, String address);

	public EncAPI getEncApi();
	/**
	 * 获取用户账户
	 * 
	 * @param addr
	 * @return
	 */
	public Account GetAccount(String addr);

	/**
	 * 创建账户
	 * 
	 * @param address
	 * @param pubKey
	 * @return
	 */
	public Account CreateAccount(String address);

	/**
	 * 获取用户账户，如果用户不存在，则创建账户
	 * 
	 * @param addr
	 * @return
	 */
	public Account GetAccountOrCreate(String addr);

	/**
	 * 创建合约账户
	 * 
	 * @param address
	 * @param pubKey
	 * @param code
	 * @param exdata
	 * @return
	 */
	public Account CreateContract(String address, byte[] code, byte[] exdata);

	/**
	 * 创建联合账户
	 * 
	 * @param oAccount
	 * @return
	 */
	public Account CreateUnionAccount(Account oAccount);

	/**
	 * 账户是否存在
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(String addr);

	/**
	 * Nonce自增1
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public int IncreaseNonce(String addr);

	/**
	 * 增加用户账户余额
	 * 
	 * @param addr
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public long addBalance(String addr, long balance);

	/**
	 * 获取账户余额
	 * 
	 * @param addr
	 * @return
	 */
	public long getBalance(String addr);

	/**
	 * 增加用户代币账户余额
	 * 
	 * @param addr
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public long addTokenBalance(String addr, String token, long balance);

	public long addTokenLockBalance(String addr, String token, long balance);

	/**
	 * 增加加密Token账户余额
	 * 
	 * @param addr
	 * @param symbol
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public long addCryptoBalance(String addr, String symbol, AccountCryptoToken.Builder token);

	public long newCryptoBalances(String addr, String symbol, ArrayList<AccountCryptoToken.Builder> tokens);

	/**
	 * 移除加密Token
	 * 
	 * @param addr
	 * @param symbol
	 * @param hash
	 * @return
	 */
	public long removeCryptoBalance(String addr, String symbol, byte[] hash);

	/**
	 * 设置用户账户Nonce
	 * 
	 * @param addr
	 * @param nonce
	 * @return
	 * @throws Exception
	 */
	public int setNonce(String addr, int nonce);

	/**
	 * 是否是合约账户
	 * 
	 * @param addr
	 * @return
	 */
	public boolean isContract(String addr);

	/**
	 * 获取用户账户Nonce
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public int getNonce(String addr);

	/**
	 * 获取用户Token账户的Balance
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public long getTokenBalance(String addr, String token);

	public long getTokenLockedBalance(String addr, String token);

	/**
	 * 获取加密Token账户的余额
	 * 
	 * @param addr
	 * @param symbol
	 * @return
	 * @throws Exception
	 */
	public List<AccountCryptoToken> getCryptoTokenBalance(String addr, String symbol);

	/**
	 * 生成加密Token方法。 调用时必须确保symbol不重复。
	 * 
	 * @param addr
	 * @param symbol
	 * @param name
	 * @param code
	 * @throws Exception
	 */
	public void generateCryptoToken(String addr, String symbol, String[] name, String[] code);

	public void ICO(String addr, String token);

	/**
	 * 判断token是否已经发行
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsToken(String token);

	/**
	 * 根据交易Hash，返回交易实体。
	 * 
	 * @param txHash
	 * @return
	 * @throws Exception
	 */
	public MultiTransaction GetTransaction(String txHash);

	public byte[] getContractAddressByTransaction(MultiTransaction oMultiTransaction);

	public void saveStorage(String address, byte[] key, byte[] value);
	public Map<String, byte[]> getStorage(String address, List<byte[]> keys);
	public byte[] getStorage(String address, byte[] key) ;
}
