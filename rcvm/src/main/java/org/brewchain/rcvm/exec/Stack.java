
package org.brewchain.rcvm.exec;

import org.brewchain.rcvm.base.DataWord;
import org.brewchain.rcvm.exec.listener.ProgramListener;
import org.brewchain.rcvm.exec.listener.ProgramListenerAware;

public class Stack extends java.util.Stack<DataWord> implements ProgramListenerAware {

    private ProgramListener programListener;

    @Override
    public void setProgramListener(ProgramListener listener) {
        this.programListener = listener;
    }

    @Override
    public synchronized DataWord pop() {
        if (programListener != null) programListener.onStackPop();
        return super.pop();
    }

    @Override
    public DataWord push(DataWord item) {
        if (programListener != null) programListener.onStackPush(item);
        return super.push(item);
    }

    public void swap(int from, int to) {
        if (isAccessible(from) && isAccessible(to) && (from != to)) {
            if (programListener != null) programListener.onStackSwap(from, to);
            DataWord tmp = get(from);
            set(from, set(to, tmp));
        }
    }

    private boolean isAccessible(int from) {
        return from >= 0 && from < size();
    }
}
