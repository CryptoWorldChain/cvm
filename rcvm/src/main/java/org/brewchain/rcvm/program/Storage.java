package org.brewchain.rcvm.program;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.brewchain.evm.api.EvmApi;
import org.brewchain.evmapi.gens.Act.Account;
import org.brewchain.evmapi.gens.Act.AccountCryptoToken;
import org.brewchain.evmapi.gens.Act.AccountValue;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;
import org.brewchain.evmapi.gens.Tx.MultiTransaction.Builder;
import org.brewchain.rcvm.base.DataWord;
import org.brewchain.rcvm.exec.invoke.ProgramInvoke;
import org.brewchain.rcvm.exec.listener.ProgramListener;
import org.brewchain.rcvm.exec.listener.ProgramListenerAware;
import org.fc.brewchain.bcapi.EncAPI;

import com.google.protobuf.ByteString;

public class Storage implements EvmApi, ProgramListenerAware {

	@Override
	public EncAPI getEncApi() {
		return repository.getEncApi();
	}

//	@Override
//	public void saveCode(ByteString code, ByteString addr) {
//		repository.saveCode(code, addr);
//	}

	private final EvmApi repository;
	private final DataWord address;
	private ProgramListener programListener;

	public Storage(ProgramInvoke programInvoke) {
		this.address = programInvoke.getOwnerAddress();
		this.repository = programInvoke.getRepository();
	}

	@Override
	public void setProgramListener(ProgramListener listener) {
		this.programListener = listener;
	}

	@Override
	public Account GetAccount(ByteString arg0) {
		// TODO Auto-generated method stub
		return repository.GetAccount(arg0);
	}

//	@Override
//	public Account GetAccountOrCreate(ByteString arg0) {
//		// TODO Auto-generated method stub
//		return repository.GetAccountOrCreate(arg0);
//	}
//
//	@Override
//	public MultiTransaction GetTransaction(String arg0) {
//		// TODO Auto-generated method stub
//		return repository.GetTransaction(arg0);
//	}
//
//	@Override
//	public void ICO(ByteString arg0, String arg1) {
//		// TODO Auto-generated method stub
//		repository.ICO(arg0, arg1);
//	}
//
//	@Override
//	public int IncreaseNonce(ByteString arg0) {
//		// TODO Auto-generated method stub
//		return repository.IncreaseNonce(arg0);
//	}

	@Override
	public BigInteger addBalance(ByteString arg0, BigInteger arg1) {
		// TODO Auto-generated method stub
		return repository.addBalance(arg0, arg1);
	}

//	@Override
//	public long addCryptoBalance(ByteString arg0, String arg1, AccountCryptoToken.Builder arg2) {
//		// TODO Auto-generated method stub
//		return repository.addCryptoBalance(arg0, arg1, arg2);
//	}
//
//	@Override
//	public long addTokenBalance(ByteString arg0, String arg1, long arg2) {
//		// TODO Auto-generated method stub
//		return repository.addTokenBalance(arg0, arg1, arg2);
//	}
//
//	@Override
//	public long addTokenLockBalance(ByteString arg0, String arg1, long arg2) {
//		// TODO Auto-generated method stub
//		return repository.addTokenLockBalance(arg0, arg1, arg2);
//	}

	@Override
	public BigInteger getBalance(ByteString arg0) {
		// TODO Auto-generated method stub
		return repository.getBalance(arg0);
	}

//	@Override
//	public byte[] getContractAddressByTransaction(MultiTransaction arg0) {
//		// TODO Auto-generated method stub
//		return repository.getContractAddressByTransaction(arg0);
//	}
//
//	@Override
//	public int getNonce(ByteString arg0) {
//		// TODO Auto-generated method stub
//		return repository.getNonce(arg0);
//	}
//
//	@Override
//	public boolean isContract(ByteString arg0) {
//		// TODO Auto-generated method stub
//		return repository.isContract(arg0);
//	}

	@Override
	public boolean isExist(ByteString arg0) {
		// TODO Auto-generated method stub
		return repository.isExist(arg0);
	}

//	@Override
//	public boolean isExistsToken(String arg0) {
//		// TODO Auto-generated method stub
//		return repository.isExistsToken(arg0);
//	}
//
//	@Override
//	public int setNonce(ByteString arg0, int arg1) {
//		// TODO Auto-generated method stub
//		return repository.setNonce(arg0, arg1);
//	}

	@Override
	public Map<String, byte[]> getStorage(ByteString arg0, List<byte[]> arg1) {
		return repository.getStorage(arg0, arg1);
	}

	@Override
	public byte[] getStorage(ByteString arg0, byte[] arg1) {
		return repository.getStorage(arg0, arg1);
	}

	@Override
	public void saveStorage(ByteString arg0, byte[] arg1, byte[] arg2) {
		repository.saveStorage(arg0, arg1, arg2);
	}

//	@Override
//	public Account CreateAccount(ByteString arg0) {
//		return repository.CreateAccount(arg0);
//	}
//
//	@Override
//	public List<AccountCryptoToken> getCryptoTokenBalance(ByteString arg0, ByteString arg1) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getTokenBalance(ByteString arg0, ByteString arg1) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public long getTokenLockedBalance(ByteString arg0, ByteString arg1) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public byte[] getBlockHashByNumber(int arg0) {
//		return repository.getBlockHashByNumber(arg0);
//	}
}
