package com.cqut.transacteSuccess.interfaces;

import java.io.Serializable;

public abstract class MakeSuccess{
	public abstract void execute(Object o);
	public abstract void noData(Object o);
	public abstract void error(Object o);
}
