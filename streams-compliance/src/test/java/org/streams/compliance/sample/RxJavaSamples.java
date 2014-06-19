package org.streams.compliance.sample;

import rx.Observable;
import rx.functions.Action1;

;

public class RxJavaSamples {

	public RxJavaSamples() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		hello("vijay", "mohan", "rao");
	}

	public static void hello(String... strings) {

		Observable.from(strings).subscribe(new Action1<String>() {

			@Override
			public void call(String data) {
				System.out.println("Hello " + data + "!");
			}

		});
	}

}
