package org.brewchain.evm.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.brewchain.evmapi.gens.Act.Account;
import org.brewchain.evmapi.gens.Act.AccountCryptoToken;
import org.brewchain.evmapi.gens.Act.AccountValue;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public interface EvmApi {

	/**
	 * 获取用户账户
	 * 
	 * @param addr
	 * @return
	 */
	public Account GetAccount(byte[] addr);
	
	/**
	 * 创建账户
	 * @param address
	 * @param pubKey
	 * @return
	 */
	public Account CreateAccount(byte[] address, byte[] pubKey);
	
	/**
	 * 获取用户账户，如果用户不存在，则创建账户
	 * 
	 * @param addr
	 * @return
	 */
	public Account GetAccountOrCreate(byte[] addr);
	
	/**
	 * 创建合约账户
	 * @param address
	 * @param pubKey
	 * @param code
	 * @param exdata
	 * @return
	 */
	public Account CreateContract(byte[] address, byte[] pubKey, byte[] code, byte[] exdata);
	
	/**
	 * 创建账户
	 * @param address
	 * @param pubKey
	 * @param max
	 * @param acceptMax
	 * @param acceptLimit
	 * @param addresses
	 * @param code
	 * @param exdata
	 * @return
	 */
	public Account CreateAccount(byte[] address, byte[] pubKey, long max, long acceptMax, int acceptLimit,
			List<ByteString> addresses, byte[] code, byte[] exdata);
	
	/**
	 * 创建联合账户
	 * @param oAccount
	 * @return
	 */
	public Account CreateUnionAccount(Account oAccount);
	
	/**
	 * 移除账户。删除后不可恢复。
	 * 
	 * @param address
	 */
	public void DeleteAccount(byte[] address);
	
	/**
	 * 账户是否存在
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public boolean isExist(byte[] addr);
	
	/**
	 * Nonce自增1
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public int IncreaseNonce(byte[] addr);
	
	/**
	 * 增加用户账户余额
	 * 
	 * @param addr
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public long addBalance(byte[] addr, long balance);
	
	/**
	 * 获取账户余额
	 * @param addr
	 * @return
	 */
	public long getBalance(byte[] addr);
	
	/**
	 * 增加用户代币账户余额
	 * 
	 * @param addr
	 * @param balance
	 * @return
	 * @throws Exception
	 */
	public long addTokenBalance(byte[] addr, String token, long balance);
	
	public long addTokenLockBalance(byte[] addr, String token, long balance);
	
	/**
	 * 增加加密Token账户余额
	 * 
	 * @param addr
	 * @param symbol
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public long addCryptoBalance(byte[] addr, String symbol, AccountCryptoToken.Builder token);
	
	public long newCryptoBalances(byte[] addr, String symbol, ArrayList<AccountCryptoToken.Builder> tokens);
	
	/**
	 * 移除加密Token
	 * 
	 * @param addr
	 * @param symbol
	 * @param hash
	 * @return
	 */
	public long removeCryptoBalance(byte[] addr, String symbol, byte[] hash);
	
	/**
	 * 设置用户账户Nonce
	 * 
	 * @param addr
	 * @param nonce
	 * @return
	 * @throws Exception
	 */
	public int setNonce(byte[] addr, int nonce);
	
	/**
	 * 是否是合约账户
	 * @param addr
	 * @return
	 */
	public boolean isContract(byte[] addr);
	
	/**
	 * 获取用户账户Nonce
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public int getNonce(byte[] addr);
	
	/**
	 * 获取用户Token账户的Balance
	 * 
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	public long getTokenBalance(byte[] addr, String token);
	
	public long getTokenLockedBalance(byte[] addr, String token);
	
	/**
	 * 获取加密Token账户的余额
	 * 
	 * @param addr
	 * @param symbol
	 * @return
	 * @throws Exception
	 */
	public List<AccountCryptoToken> getCryptoTokenBalance(byte[] addr, String symbol);
	
	/**
	 * 生成加密Token方法。 调用时必须确保symbol不重复。
	 * 
	 * @param addr
	 * @param symbol
	 * @param name
	 * @param code
	 * @throws Exception
	 */
	public void generateCryptoToken(byte[] addr, String symbol, String[] name, String[] code);
	
	public void ICO(byte[] addr, String token);
	
	/**
	 * 判断token是否已经发行
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsToken(String token);
	
	public void putAccountValue(byte[] addr, AccountValue oAccountValue);
	
	/**
	 * 保存交易方法。 交易不会立即执行，而是等待被广播和打包。只有在Block中的交易，才会被执行。 交易签名规则 1. 清除signatures 2.
	 * txHash=ByteString.EMPTY 3. 签名内容=oMultiTransaction.toByteArray()
	 * 
	 * @param oMultiTransaction
	 * @throws Exception
	 */
	public ByteString CreateMultiTransaction(MultiTransaction.Builder oMultiTransaction);
	
	public ByteString CreateGenesisMultiTransaction(MultiTransaction.Builder oMultiTransaction);
	
	/**
	 * 广播交易方法。 交易广播后，节点收到的交易会保存在本地db中。交易不会立即执行，而且不被广播。只有在Block中的交易，才会被执行。
	 * 
	 * @param oTransaction
	 * @throws Exception
	 */
	public void SyncTransaction(MultiTransaction.Builder oMultiTransaction);
	
	/**
	 * 交易执行。交易只在接收到Block后才会被执行
	 * 
	 * @param oTransaction
	 */
	public void ExecuteTransaction(LinkedList<MultiTransaction> oMultiTransactions);
	
	/**
	 * 从待广播交易列表中，查询出交易进行广播。广播后，不论是否各节点接收成功，均放入待打包列表
	 * 
	 * @param count
	 * @return
	 * @throws InvalidProtocolBufferException
	 */
	public List<MultiTransaction> getWaitSendTx(int count);
	
	/**
	 * 从待打包交易列表中，查询出等待打包的交易。
	 * 
	 * @param count
	 * @return
	 * @throws InvalidProtocolBufferException
	 */
	public LinkedList<MultiTransaction> getWaitBlockTx(int count);
	
	public void removeWaitBlockTx(byte[] txHash);
	
	/**
	 * 根据交易Hash，返回交易实体。
	 * 
	 * @param txHash
	 * @return
	 * @throws Exception
	 */
	public MultiTransaction GetTransaction(byte[] txHash);
	
	
	/**
	 * 交易签名方法。该方法未实现
	 * 
	 * @param privKey
	 * @param oTransaction
	 * @throws Exception
	 */
	public void Signature(List<String> privKeys, MultiTransaction.Builder oTransaction);
	
	/**
	 * 获取交易签名后的Hash
	 * 
	 * @param oTransaction
	 * @throws Exception
	 */
	public void getTransactionHash(MultiTransaction.Builder oTransaction);
	
	/**
	 * 校验并保存交易。该方法不会执行交易，用户的账户余额不会发生变化。
	 * 
	 * @param oMultiTransaction
	 * @throws Exception
	 */
	public MultiTransaction verifyAndSaveMultiTransaction(MultiTransaction.Builder oMultiTransaction);
	
	/**
	 * 验证签名
	 * @param pubKey
	 * @param signature
	 * @param tx
	 */
	public void verifySignature(String pubKey, String signature, byte[] tx);
	
	public void setTransactionDone(byte[] txHash);
	
	public void setTransactionError(byte[] txHash);
	
	public byte[] getContractAddressByTransaction(MultiTransaction oMultiTransaction);
	
	
}
