package com.aobei.train;

public class Test {

	public static void main(String[] args) {
		LevelId levelId = new LevelId(4, "0005");
		for (int i = 0; i < 5; i++) {
			System.out.println(levelId.next());
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(levelId.nextChildren());
		}
	}
}