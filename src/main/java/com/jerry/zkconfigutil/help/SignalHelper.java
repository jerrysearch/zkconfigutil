package com.jerry.zkconfigutil.help;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.jerry.zkconfigutil.resolve.Resolve;

@SuppressWarnings("restriction")
public class SignalHelper {
	static Logger logger = Logger.getLogger(SignalHelper.class);
	static {
		Signal.handle(new Signal("USR2"), new SignalHandler() {
			@Override
			public void handle(Signal arg0) {
				for (SonHelper sonHelper : list) {
					logger.info(sonHelper.toString());
				}
			}
		});
	}

	final static List<SonHelper> list = new LinkedList<SonHelper>();

	public static void mark(Class<?> claz, String fieldName, Resolve resolve,
			boolean update) {
		SonHelper sonHelper = new SonHelper(claz, fieldName, resolve, update);
		list.add(sonHelper);
	}

	private static class SonHelper {
		Class<?> claz;
		String fieldName;
		Resolve resolve;
		boolean update;

		SonHelper(Class<?> claz, String fieldName, Resolve resolve,
				boolean update) {
			this.claz = claz;
			this.fieldName = fieldName;
			this.resolve = resolve;
			this.update = update;
		}

		@Override
		public String toString() {
			return "SonHelper [claz=" + claz + ", fieldName=" + fieldName
					+ ", value=" + resolve.resolve() + ", resolve="
					+ resolve.getClass().getSimpleName() + ", update=" + update
					+ "]";
		}
	}
}
