package jevm.util;

import java.math.BigInteger;

public class Number {
	private static final BigInteger ZERO = BigInteger.ZERO;
	private static final BigInteger ONE = BigInteger.ONE;
	private static final BigInteger TWO = BigInteger.valueOf(2);

	private static class Integer {
		private final BigInteger min;
		private final BigInteger max;
		private final BigInteger value;

		public Integer(BigInteger min, BigInteger max, BigInteger value) {
			this.min = min;
			this.max = max;
			this.value = value;
			check();
		}

		public void check() {
			if (value.compareTo(min) < 0) {
				throw new ArithmeticException();
			} else if (value.compareTo(max) > 0) {
				throw new ArithmeticException();
			}
		}
	}

	private static class Signed extends Integer {
		public Signed(int width, long value) {
			this(width,BigInteger.valueOf(value));
		}
		public Signed(int width, BigInteger value) {
			super(TWO.pow(width-1).negate(), TWO.pow(width-1).subtract(ONE), value);
		}
	}

	private static class Unsigned extends Integer {
		public Unsigned(int width, long value) {
			this(width,BigInteger.valueOf(value));
		}
		public Unsigned(int width, BigInteger value) {
			super(ZERO, TWO.pow(width).subtract(ONE), value);
		}
	}

	public final static class i8 extends Signed {
		public i8(long value) {
			super(8,value);
		}
		public i8(BigInteger value) {
			super(8,value);
		}
	}

	public final static class i256 extends Signed {
		public i256(long value) {
			super(256,value);
		}
		public i256(BigInteger value) {
			super(256,value);
		}
	}

	public final static class u160 extends Unsigned {
		public u160(long value) {
			super(160,value);
		}
		public u160(BigInteger value) {
			super(160,value);
		}
	}

	public final static class u256 extends Unsigned {
		public u256(long value) {
			super(256,value);
		}
		public u256(BigInteger value) {
			super(256,value);
		}
	}
}
