package org.brewchain.rcvm.exec.listener;

import org.brewchain.rcvm.base.DataWord;

public class ProgramListenerAdaptor implements ProgramListener {

    @Override
    public void onMemoryExtend(int delta) {

    }

    @Override
    public void onMemoryWrite(int address, byte[] data, int size) {

    }

    @Override
    public void onStackPop() {

    }

    @Override
    public void onStackPush(DataWord value) {

    }

    @Override
    public void onStackSwap(int from, int to) {

    }

    @Override
    public void onStoragePut(DataWord key, DataWord value) {

    }

    @Override
    public void onStorageClear() {

    }
}
