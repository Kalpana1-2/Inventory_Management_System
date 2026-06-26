package com.userInventory.repository.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Table(name="category")
public class Category {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="categoryId")
private int categoryId;

@Column(name="categoryType",length=20)
private String categoryType;

}
