package org.brewchain.rcvm.program;

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

	@Override
	public void saveCode(String code, String addr) {
		repository.saveCode(code, addr);
	}

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
	public Account CreateContract(String arg0, byte[] arg2, byte[] arg3) {
		// TODO Auto-generated method stub
		return repository.CreateContract(arg0, arg2, arg3);
	}

	@Override
	public Account GetAccount(String arg0) {
		// TODO Auto-generated method stub
		return repository.GetAccount(arg0);
	}

	@Override
	public Account GetAccountOrCreate(String arg0) {
		// TODO Auto-generated method stub
		return repository.GetAccountOrCreate(arg0);
	}

	@Override
	public MultiTransaction GetTransaction(String arg0) {
		// TODO Auto-generated method stub
		return repository.GetTransaction(arg0);
	}

	@Override
	public void ICO(String arg0, String arg1) {
		// TODO Auto-generated method stub
		repository.ICO(arg0, arg1);
	}

	@Override
	public int IncreaseNonce(String arg0) {
		// TODO Auto-generated method stub
		return repository.IncreaseNonce(arg0);
	}

	@Override
	public long addBalance(String arg0, long arg1) {
		// TODO Auto-generated method stub
		return repository.addBalance(arg0, arg1);
	}

	@Override
	public long addCryptoBalance(String arg0, String arg1, AccountCryptoToken.Builder arg2) {
		// TODO Auto-generated method stub
		return repository.addCryptoBalance(arg0, arg1, arg2);
	}

	@Override
	public long addTokenBalance(String arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub
		return repository.addTokenBalance(arg0, arg1, arg2);
	}

	@Override
	public long addTokenLockBalance(String arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub
		return repository.addTokenLockBalance(arg0, arg1, arg2);
	}

	@Override
	public void generateCryptoToken(String arg0, String arg1, String[] arg2, String[] arg3) {
		// TODO Auto-generated method stub
		repository.generateCryptoToken(arg0, arg1, arg2, arg3);
	}

	@Override
	public long getBalance(String arg0) {
		// TODO Auto-generated method stub
		return repository.getBalance(arg0);
	}

	@Override
	public byte[] getContractAddressByTransaction(MultiTransaction arg0) {
		// TODO Auto-generated method stub
		return repository.getContractAddressByTransaction(arg0);
	}

	@Override
	public List<AccountCryptoToken> getCryptoTokenBalance(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return repository.getCryptoTokenBalance(arg0, arg1);
	}

	@Override
	public int getNonce(String arg0) {
		// TODO Auto-generated method stub
		return repository.getNonce(arg0);
	}

	@Override
	public long getTokenBalance(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return repository.getTokenBalance(arg0, arg1);
	}

	@Override
	public long getTokenLockedBalance(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return repository.getTokenLockedBalance(arg0, arg1);
	}

	@Override
	public boolean isContract(String arg0) {
		// TODO Auto-generated method stub
		return repository.isContract(arg0);
	}

	@Override
	public boolean isExist(String arg0) {
		// TODO Auto-generated method stub
		return repository.isExist(arg0);
	}

	@Override
	public boolean isExistsToken(String arg0) {
		// TODO Auto-generated method stub
		return repository.isExistsToken(arg0);
	}

	@Override
	public long newCryptoBalances(String arg0, String arg1, ArrayList<AccountCryptoToken.Builder> arg2) {
		// TODO Auto-generated method stub
		return repository.newCryptoBalances(arg0, arg1, arg2);
	}

	@Override
	public long removeCryptoBalance(String arg0, String arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		return repository.removeCryptoBalance(arg0, arg1, arg2);
	}

	@Override
	public int setNonce(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return repository.setNonce(arg0, arg1);
	}

	@Override
	public Map<String, byte[]> getStorage(String arg0, List<byte[]> arg1) {
		return repository.getStorage(arg0, arg1);
	}

	@Override
	public byte[] getStorage(String arg0, byte[] arg1) {
		return repository.getStorage(arg0, arg1);
	}

	@Override
	public void saveStorage(String arg0, byte[] arg1, byte[] arg2) {
		repository.saveStorage(arg0, arg1, arg2);
	}

	@Override
	public Account CreateAccount(String arg0) {
		return repository.CreateAccount(arg0);
	}

	@Override
	public Account CreateUnionAccount(Account arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
