package com.userInventory.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
	ADMIN,
	MANAGER,
	VENDOR,
	CUSTOMER;
	 @JsonCreator
	    public static Role from(String value) {
	        return Role.valueOf(value.toUpperCase());
	    }

	    @JsonValue
	    public String toValue() {
	        return this.name();
	    }
}
