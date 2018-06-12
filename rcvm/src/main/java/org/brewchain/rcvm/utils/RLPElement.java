package org.brewchain.rcvm.utils;

import java.io.Serializable;

public interface RLPElement extends Serializable {

    byte[] getRLPData();
}
