package org.brewchain.rcvm.exec.invoke;

import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;

import java.math.BigInteger;

import org.brewchain.evmapi.gens.Block;
import org.brewchain.evmapi.gens.Block.BlockEntity;
import org.brewchain.evmapi.gens.Block.BlockHeader;
import org.brewchain.evmapi.gens.Tx.MultiTransaction;
import org.apache.commons.lang3.StringUtils;
import org.brewchain.evm.api.EvmApi;
import org.brewchain.rcvm.base.DataWord;
import org.brewchain.rcvm.program.Program;
import org.brewchain.rcvm.utils.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProgramInvokeFactoryImpl implements ProgramInvokeFactory {

	@Override
	public ProgramInvoke createProgramInvoke(MultiTransaction tx, BlockEntity block, EvmApi repository) {

		byte[] address = Hex.decode(tx.getTxBody().getOutputs(0).getAddress().toByteArray());

		// YP: This is the sender of original transaction; it is never a contract.
		byte[] origin = Hex.decode(tx.getTxBody().getInputs(0).getAddress().toByteArray());

		/*** CALLER op ***/
		// YP: This is the address of the account that is directly responsible for this
		// execution.
		byte[] caller = Hex.decode(tx.getTxBody().getInputs(0).getAddress().toByteArray());

		/*** BALANCE op ***/
		byte[] balance = ByteUtil.bigIntegerToBytes(repository.getBalance(tx.getTxBody().getInputs(0).getAddress()));

		/*** GASPRICE op ***/
		byte[] gasPrice = ByteUtil.ZERO_BYTE_ARRAY;

		/*** GAS op ***/
		byte[] gas = ByteUtil.ZERO_BYTE_ARRAY;

		/*** CALLVALUE op ***/
		byte[] callValue = tx.getTxBody().getInputs(0).getAmount().toByteArray();
		// StringUtils.isBlank(tx.getTxBody().getExdata()) ? ByteUtil.EMPTY_BYTE_ARRAY
		// : Hex.decode(tx.getTxBody().getExdata().toByteArray());

		/*** CALLDATALOAD op ***/
		/*** CALLDATACOPY op ***/
		/*** CALLDATASIZE op ***/
		byte[] data = Hex.decode(tx.getTxBody().getData().toByteArray());

		/*** PREVHASH op ***/
		byte[] lastHash = Hex.decode(block.getHeader().getParentHash());

		/*** COINBASE op ***/
		byte[] coinbase = block.getMiner().getAddress().getBytes();

		/*** TIMESTAMP op ***/
		long timestamp = block.getHeader().getTimestamp();

		/*** NUMBER op ***/
		long number = block.getHeader().getNumber();

		/*** DIFFICULTY op ***/
		byte[] difficulty = ByteUtil.EMPTY_BYTE_ARRAY;

		/*** GASLIMIT op ***/
		byte[] gaslimit = ByteUtil.EMPTY_BYTE_ARRAY;

		if (log.isInfoEnabled()) {
			log.info("Top level call: \n" + "tx.hash={}\n" + "address={}\n" + "origin={}\n" + "caller={}\n"
					+ "balance={}\n" + "gasPrice={}\n" + "gas={}\n" + "callValue={}\n" + "data={}\n" + "lastHash={}\n"
					+ "coinbase={}\n" + "timestamp={}\n" + "blockNumber={}\n" + "difficulty={}\n" + "gaslimit={}\n",

					tx.getTxHash(), Hex.toHexString(address), Hex.toHexString(origin), Hex.toHexString(caller),
					ByteUtil.bytesToBigInteger(balance), ByteUtil.bytesToBigInteger(gasPrice),
					ByteUtil.bytesToBigInteger(gas), ByteUtil.bytesToBigInteger(callValue), Hex.toHexString(data),
					Hex.toHexString(lastHash), Hex.toHexString(coinbase), timestamp, number,
					Hex.toHexString(difficulty), gaslimit);
		}

		return new ProgramInvokeImpl(address, origin, caller, balance, callValue, data, lastHash, coinbase, timestamp,
				number, difficulty, repository);
	}

	/**
	 * This invocation created for contract call contract
	 */
	@Override
	public ProgramInvoke createProgramInvoke(Program program, DataWord toAddress, DataWord callerAddress,
			DataWord inValue, BigInteger balanceInt, byte[] dataIn, EvmApi repository, boolean isStaticCall,
			boolean byTestingSuite) {

		DataWord address = toAddress;
		DataWord origin = program.getOriginAddress();
		DataWord caller = callerAddress;

		DataWord balance = new DataWord(balanceInt.toByteArray());
		DataWord callValue = inValue;

		byte[] data = dataIn;
		DataWord lastHash = program.getPrevHash();
		DataWord coinbase = program.getCoinbase();
		DataWord timestamp = program.getTimestamp();
		DataWord number = program.getNumber();
		DataWord difficulty = program.getDifficulty();

		if (log.isInfoEnabled()) {
			log.info("Internal call: \n" + "address={}\n" + "origin={}\n" + "caller={}\n" + "balance={}\n"
					+ "gasPrice={}\n" + "gas={}\n" + "callValue={}\n" + "data={}\n" + "lastHash={}\n" + "coinbase={}\n"
					+ "timestamp={}\n" + "blockNumber={}\n" + "difficulty={}\n" + "gaslimit={}\n",
					Hex.toHexString(address.getLast20Bytes()), Hex.toHexString(origin.getLast20Bytes()),
					Hex.toHexString(caller.getLast20Bytes()), balance.toString(), 0, 0,
					Hex.toHexString(callValue.getNoLeadZeroesData()), data == null ? "" : Hex.toHexString(data),
					Hex.toHexString(lastHash.getData()), Hex.toHexString(coinbase.getLast20Bytes()),
					timestamp.longValue(), number.longValue(), Hex.toHexString(difficulty.getNoLeadZeroesData()), 0);
		}

		return new ProgramInvokeImpl(address, origin, caller, balance, callValue, data, lastHash, coinbase, timestamp,
				number, difficulty, repository, program.getCallDeep() + 1, isStaticCall, byTestingSuite);
	}

}
