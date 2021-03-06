package org.brewchain.rcvm.exec.trace;

import static java.lang.String.format;
import static org.brewchain.rcvm.exec.trace.Serializers.serializeFieldsOnly;

import java.util.ArrayList;
import java.util.List;

import org.brewchain.rcvm.exec.OpCode;
import org.brewchain.rcvm.exec.invoke.ProgramInvoke;
import org.fc.brewchain.bcapi.EncAPI;
import org.spongycastle.util.encoders.Hex;

import onight.tfw.ntrans.api.annotation.ActorRequire;

public class ProgramTrace {
	
	@ActorRequire(name = "bc_encoder", scope = "global")
	EncAPI encApi;
	
    private List<Op> ops = new ArrayList<>();
    private String result;
    private String error;
    private String contractAddress;

    public ProgramTrace() {
        this(null);
    }

    public ProgramTrace(ProgramInvoke programInvoke) {
        if (programInvoke != null) {
            contractAddress = Hex.toHexString(programInvoke.getOwnerAddress().getLast20Bytes());
        }
    }

    public List<Op> getOps() {
        return ops;
    }

    public void setOps(List<Op> ops) {
        this.ops = ops;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public ProgramTrace result(byte[] result) {
        setResult(encApi.hexEnc(result));
        return this;
    }

    public ProgramTrace error(Exception error) {
        setError(error == null ? "" : format("%s: %s", error.getClass(), error.getMessage()));
        return this;
    }

    public Op addOp(byte code, int pc, int deep, OpActions actions) {
        Op op = new Op();
        op.setActions(actions);
        op.setCode(OpCode.code(code));
        op.setDeep(deep);
        
        op.setPc(pc);

        ops.add(op);

        return op;
    }

    /**
     * Used for merging sub calls execution.
     */
    public void merge(ProgramTrace programTrace) {
        this.ops.addAll(programTrace.ops);
    }

    public String asJsonString(boolean formatted) {
        return serializeFieldsOnly(this, formatted);
    }

    @Override
    public String toString() {
        return asJsonString(true);
    }
}
