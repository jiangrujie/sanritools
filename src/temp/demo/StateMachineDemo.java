package temp.demo;
/**
 * 
 * 创建时间:2016-10-2上午8:53:24<br/>
 * 创建者:sanri<br/>
 * 功能:有限状态机<br/>
 */
public class StateMachineDemo {

	public static void main(String[] args) {
		final long money = 600000L;
		final int preMoney = (int)money;
		final int loanYears = 30;
		final int everyMonthMoney = 2000;

		final BuyHouseStateMachine.Prepare prepare = BuyHouseStateMachine.Prepare.prepare()
											 .carriedMoney(money)
											 .preMoney(preMoney)
											 .loanYears(loanYears)
											 .everyMonthMoney(everyMonthMoney)
											 .build();
		final BuyHouseStateMachine machine = new BuyHouseStateMachine(prepare);

		machine.start();
	}
	
	static final class BuyHouseStateMachine {
		public static final class Prepare {
			private long mCarriedMoney; // 带来的买房钱
			private int mPreMoney;      // 首付的钱
			private int mLoanYears;	   // 贷款年限
			private int mEveryMonthMoney; // 每个月需要还的钱

			public long carriedMoney() {
				return mCarriedMoney;
			}

			public int preMoney() {
				return mPreMoney;
			}

			public int loanYears() {
				return mLoanYears;
			}

			public int everyMonthMoney() {
				return mEveryMonthMoney;
			}

			private Prepare(){}

			public static Builder prepare() {
				return new Prepare.Builder();
			}

			private static final class Builder {
				private Prepare prepare;
				public Builder() {
					prepare = new Prepare();
				}

				public Prepare build() {
					return prepare;
				}

				public Builder carriedMoney(long carriedMoney) {
					prepare.mCarriedMoney = carriedMoney;
					return this;
				}

				public Builder preMoney(int preMoney) {
					prepare.mPreMoney = preMoney;
					return this;
				}

				public Builder loanYears(int loanYears) {
					prepare.mLoanYears = loanYears;
					return this;
				}

				public Builder everyMonthMoney(int everyMonthMoney) {
					prepare.mEveryMonthMoney = everyMonthMoney;
					return this;
				}
			}

			public Prepare(long carriedMoney,int preMoney,int loanYears) {
				mCarriedMoney = carriedMoney;
				mPreMoney = preMoney;
				mLoanYears = loanYears;
			}
		}

		private final Prepare mPrepare;

		public BuyHouseStateMachine(Prepare prepare) {
			mPrepare = prepare;
		}

		public void start() {
			final MiddleCompanyState middleCompany = new MiddleCompanyState(mPrepare);
			middleCompany.next();
		}
	}

	static interface IBuyHouseState {
		long HOUSE_PRICE = 1000000L;	
		long ENOUGH_MONEY = 1000000L;

		int _30_PERCENT = 300000;
		int _20_PERCENT = 200000;
	}

	static abstract class AbstractBuyHouseState implements IBuyHouseState {
		protected BuyHouseStateMachine.Prepare mPrepare;
		protected AbstractBuyHouseState mLastState;

		public void setLastState(AbstractBuyHouseState lastState) {
			mLastState = lastState;
		}


		public AbstractBuyHouseState(BuyHouseStateMachine.Prepare prepare) {
			mPrepare = prepare;
		}

		public void next() {
			final AbstractBuyHouseState nextState = getNextState();
			nextState.start();
		}

		public void start() {
		}

		protected abstract AbstractBuyHouseState getNextState();
	}

	static final class MiddleCompanyState extends AbstractBuyHouseState {
		public MiddleCompanyState(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		protected AbstractBuyHouseState getNextState() {
			if (mPrepare.carriedMoney() > HOUSE_PRICE)
				return new BuyHouseSuccessState(mPrepare); // 一次性付清
			return new LoanState(mPrepare); // 需要贷款买房
		}
	}

	static final class BuyHouseSuccessState extends AbstractBuyHouseState {
		public BuyHouseSuccessState(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			return this;
		}

		public void start() {
			System.out.println("终于可以买房子了!");
		}
	}

	static final class LoanState extends AbstractBuyHouseState {
		public LoanState(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mPrepare.preMoney() > _30_PERCENT)
				return new _30_Percent_state(mPrepare);
			else if (mPrepare.preMoney() > _20_PERCENT)
				return new _20_Percent_state(mPrepare);
			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("您只带了 " + mPrepare.carriedMoney() + "元，房子价格为: " + HOUSE_PRICE + "元,需要贷款才能买房");
			next();
		}
	}

	static final class _30_Percent_state extends AbstractBuyHouseState {
		public _30_Percent_state(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mPrepare.loanYears() == 30) {
				final AbstractBuyHouseState result = new _30_Years_State(mPrepare);
				result.setLastState(this);
				return result;
			} else if (mPrepare.loanYears() == 20) {
				final AbstractBuyHouseState result = new _20_Year_State(mPrepare);
				result.setLastState(this);
				return result;
			}

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("您的首付准备了: " + mPrepare.preMoney() + "元，而首付只需要: " + _30_PERCENT + "元, 足够付三成首付");
			next();
		}
	}

	static final class _30_Years_State extends AbstractBuyHouseState {
		public _30_Years_State(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mLastState != null) {
				if (mLastState instanceof _30_Percent_state)
					return new _3000_Money_Month(mPrepare);
				else if (mLastState instanceof _20_Percent_state)
					return new _5000_Money_Month(mPrepare);
			}

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("您计划贷款30年...");
			next();
		}
	}

	static final class _3000_Money_Month extends AbstractBuyHouseState {
		public _3000_Money_Month(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {

			if (mPrepare.everyMonthMoney() > 3000)
				return new BuyHouseSuccessState(mPrepare);

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("您需要每个月还贷3000元,而您计划每个月还贷" + mPrepare.everyMonthMoney() + "元");
			next();
		}
	}

	static final class _4000_Money_Month extends AbstractBuyHouseState {
		public _4000_Money_Month(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {

			if (mPrepare.everyMonthMoney() > 4000)
				return new BuyHouseSuccessState(mPrepare);

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("每个月还贷4000元");
			next();
		}
	}

	static final class _5000_Money_Month extends AbstractBuyHouseState {
		public _5000_Money_Month(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mPrepare.everyMonthMoney() > 5000)
				return new BuyHouseSuccessState(mPrepare);

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("每个月还贷5000元");
			next();
		}
	}

	static final class _6000_Money_Month extends AbstractBuyHouseState {
		public _6000_Money_Month(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mPrepare.everyMonthMoney() > 6000)
				return new BuyHouseSuccessState(mPrepare);

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("每个月还贷6000元");
			next();
		}
	}

	static final class _20_Year_State extends AbstractBuyHouseState {
		public _20_Year_State(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mLastState != null) {
				if (mLastState instanceof _30_Percent_state)
					return new _4000_Money_Month(mPrepare);
				else if (mLastState instanceof _20_Percent_state)
					return new _6000_Money_Month(mPrepare);
			}

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("贷款20年");
			next();
		}
	}

	static final class _20_Percent_state extends AbstractBuyHouseState {
		public _20_Percent_state(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			if (mPrepare.loanYears() == 30) {
				final AbstractBuyHouseState result = new _30_Years_State(mPrepare);
				result.setLastState(this);
				return result;
			}else if (mPrepare.loanYears() == 20) {
				final AbstractBuyHouseState result = new _20_Year_State(mPrepare);
				result.setLastState(this);
				return result;
			}

			return new BuyHouseFailedState(mPrepare);
		}

		public void start() {
			System.out.println("付两成首付");
			next();
		}
	}

	static final class BuyHouseFailedState extends AbstractBuyHouseState {
		public BuyHouseFailedState(BuyHouseStateMachine.Prepare prepare) {
			super(prepare);
		}

		public AbstractBuyHouseState getNextState() {
			return this;
		}

		public void start() {
			System.out.println("哎，买不起房子!");
		}
	}
}
