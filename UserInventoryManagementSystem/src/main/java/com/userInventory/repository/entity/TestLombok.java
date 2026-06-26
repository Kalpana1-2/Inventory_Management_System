package com.userInventory.repository.entity;

import lombok.*;

@Data
public class TestLombok {
private int id;
private String name;

public static void main(String[] args) {
	TestLombok t=new TestLombok();
	t.setId(1);
	t.setName("Kalpana");
	System.out.println("ID:"+t.getId()+"\nName:"+t.getName());
}
}
