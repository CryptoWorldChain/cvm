package org.brewchain.rcvm.exec;

import org.brewchain.rcvm.program.Program;

public interface VMHook {
    void startPlay(Program program);
    void step(Program program, OpCode opcode);
    void stopPlay(Program program);
}
