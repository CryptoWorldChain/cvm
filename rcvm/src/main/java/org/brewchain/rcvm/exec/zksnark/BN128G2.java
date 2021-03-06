package org.brewchain.rcvm.exec.zksnark;

import java.math.BigInteger;


import static org.brewchain.rcvm.exec.zksnark.Params.R;
import static org.brewchain.rcvm.exec.zksnark.Params.TWIST_MUL_BY_P_X;
import static org.brewchain.rcvm.exec.zksnark.Params.TWIST_MUL_BY_P_Y;
public class BN128G2 extends BN128Fp2 {

    BN128G2(BN128<Fp2> p) {
        super(p.x, p.y, p.z);
    }

    BN128G2(Fp2 x, Fp2 y, Fp2 z) {
        super(x, y, z);
    }

    @Override
    public BN128G2 toAffine() {
        return new BN128G2(super.toAffine());
    }

    /**
     * Checks whether provided data are coordinates of a point belonging to subgroup,
     * if check has been passed it returns a point, otherwise returns null
     */
    public static BN128G2 create(byte[] a, byte[] b, byte[] c, byte[] d) {

        BN128<Fp2> p = BN128Fp2.create(a, b, c, d);

        // fails if point is invalid
        if (p == null) {
            return null;
        }

        // check whether point is a subgroup member
        if (!isGroupMember(p)) return null;

        return new BN128G2(p);
    }

    private static boolean isGroupMember(BN128<Fp2> p) {
        BN128<Fp2> left = p.mul(FR_NEG_ONE).add(p);
        return left.isZero(); // should satisfy condition: -1 * p + p == 0, where -1 belongs to F_r
    }
    static final BigInteger FR_NEG_ONE = BigInteger.ONE.negate().mod(R);

    BN128G2 mulByP() {

        Fp2 rx = TWIST_MUL_BY_P_X.mul(x.frobeniusMap(1));
        Fp2 ry = TWIST_MUL_BY_P_Y.mul(y.frobeniusMap(1));
        Fp2 rz = z.frobeniusMap(1);

        return new BN128G2(rx, ry, rz);
    }
}
