package org.brewchain.rcvm.program;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

import com.google.protobuf.ByteString;

public class Storage implements EvmApi, ProgramListenerAware {

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
	public Account CreateAccount(byte[] arg0, byte[] arg1, long arg2, long arg3, int arg4, List<ByteString> arg5,
			byte[] arg6, byte[] arg7) {
		// TODO Auto-generated method stub
		return repository.CreateAccount(arg0,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
	}

	@Override
	public Account CreateAccount(byte[] arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		return repository.CreateAccount(arg0, arg1);
	}

	@Override
	public Account CreateContract(byte[] arg0, byte[] arg1, byte[] arg2, byte[] arg3) {
		// TODO Auto-generated method stub
		return repository.CreateContract(arg0, arg1, arg2, arg3);
	}

	@Override
	public ByteString CreateGenesisMultiTransaction(Builder arg0) {
		// TODO Auto-generated method stub
		return repository.CreateGenesisMultiTransaction(arg0);
	}

	@Override
	public ByteString CreateMultiTransaction(Builder arg0) {
		// TODO Auto-generated method stub
		return repository.CreateMultiTransaction(arg0);
	}

	@Override
	public Account CreateUnionAccount(Account arg0) {
		// TODO Auto-generated method stub
		return repository.CreateUnionAccount(arg0);
	}

	@Override
	public void DeleteAccount(byte[] arg0) {
		// TODO Auto-generated method stub
		repository.DeleteAccount(arg0);
	}

	@Override
	public void ExecuteTransaction(LinkedList<MultiTransaction> arg0) {
		// TODO Auto-generated method stub
		repository.ExecuteTransaction(arg0);
	}

	@Override
	public Account GetAccount(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.GetAccount(arg0);
	}

	@Override
	public Account GetAccountOrCreate(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.GetAccountOrCreate(arg0);
	}

	@Override
	public MultiTransaction GetTransaction(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.GetTransaction(arg0);
	}

	@Override
	public void ICO(byte[] arg0, String arg1) {
		// TODO Auto-generated method stub
		repository.ICO(arg0, arg1);
	}

	@Override
	public int IncreaseNonce(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.IncreaseNonce(arg0);
	}

	@Override
	public void Signature(List<String> arg0, Builder arg1) {
		// TODO Auto-generated method stub
		repository.Signature(arg0, arg1);
	}

	@Override
	public void SyncTransaction(Builder arg0) {
		// TODO Auto-generated method stub
		repository.SyncTransaction(arg0);
	}

	@Override
	public long addBalance(byte[] arg0, long arg1) {
		// TODO Auto-generated method stub
		return repository.addBalance(arg0, arg1);
	}

	@Override
	public long addCryptoBalance(byte[] arg0, String arg1,
			AccountCryptoToken.Builder arg2) {
		// TODO Auto-generated method stub
		return repository.addCryptoBalance(arg0, arg1, arg2);
	}

	@Override
	public long addTokenBalance(byte[] arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub
		return repository.addTokenBalance(arg0, arg1, arg2);
	}

	@Override
	public long addTokenLockBalance(byte[] arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub
		return repository.addTokenLockBalance(arg0, arg1, arg2);
	}

	@Override
	public void generateCryptoToken(byte[] arg0, String arg1, String[] arg2, String[] arg3) {
		// TODO Auto-generated method stub
		repository.generateCryptoToken(arg0, arg1, arg2, arg3);
	}

	@Override
	public long getBalance(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.getBalance(arg0);
	}

	@Override
	public byte[] getContractAddressByTransaction(MultiTransaction arg0) {
		// TODO Auto-generated method stub
		return repository.getContractAddressByTransaction(arg0);
	}

	@Override
	public List<AccountCryptoToken> getCryptoTokenBalance(byte[] arg0, String arg1) {
		// TODO Auto-generated method stub
		return repository.getCryptoTokenBalance(arg0, arg1);
	}

	@Override
	public int getNonce(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.getNonce(arg0);
	}

	@Override
	public long getTokenBalance(byte[] arg0, String arg1) {
		// TODO Auto-generated method stub
		return repository.getTokenBalance(arg0, arg1);
	}

	@Override
	public long getTokenLockedBalance(byte[] arg0, String arg1) {
		// TODO Auto-generated method stub
		return repository.getTokenLockedBalance(arg0, arg1);
	}

	@Override
	public void getTransactionHash(Builder arg0) {
		// TODO Auto-generated method stub
		repository.getTransactionHash(arg0);
	}

	@Override
	public LinkedList<MultiTransaction> getWaitBlockTx(int arg0) {
		// TODO Auto-generated method stub
		return repository.getWaitBlockTx(arg0);
	}

	@Override
	public List<MultiTransaction> getWaitSendTx(int arg0) {
		// TODO Auto-generated method stub
		return repository.getWaitSendTx(arg0);
	}

	@Override
	public boolean isContract(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.isContract(arg0);
	}

	@Override
	public boolean isExist(byte[] arg0) {
		// TODO Auto-generated method stub
		return repository.isExist(arg0);
	}

	@Override
	public boolean isExistsToken(String arg0) {
		// TODO Auto-generated method stub
		return repository.isExistsToken(arg0);
	}

	@Override
	public long newCryptoBalances(byte[] arg0, String arg1,
			ArrayList<AccountCryptoToken.Builder> arg2) {
		// TODO Auto-generated method stub
		return repository.newCryptoBalances(arg0, arg1, arg2);
	}

	@Override
	public void putAccountValue(byte[] arg0, AccountValue arg1) {
		// TODO Auto-generated method stub
		repository.putAccountValue(arg0, arg1);
	}

	@Override
	public long removeCryptoBalance(byte[] arg0, String arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		return repository.removeCryptoBalance(arg0, arg1, arg2);
	}

	@Override
	public void removeWaitBlockTx(byte[] arg0) {
		// TODO Auto-generated method stub
		repository.removeWaitBlockTx(arg0);
	}

	@Override
	public int setNonce(byte[] arg0, int arg1) {
		// TODO Auto-generated method stub
		return repository.setNonce(arg0, arg1);
	}

	@Override
	public void setTransactionDone(byte[] arg0) {
		// TODO Auto-generated method stub
		repository.setTransactionDone(arg0);
	}

	@Override
	public void setTransactionError(byte[] arg0) {
		// TODO Auto-generated method stub
		repository.setTransactionError(arg0);
	}

	@Override
	public MultiTransaction verifyAndSaveMultiTransaction(Builder arg0) {
		// TODO Auto-generated method stub
		return repository.verifyAndSaveMultiTransaction(arg0);
	}

	@Override
	public void verifySignature(String arg0, String arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		repository.verifySignature(arg0, arg1, arg2);
	}
    
    

}
