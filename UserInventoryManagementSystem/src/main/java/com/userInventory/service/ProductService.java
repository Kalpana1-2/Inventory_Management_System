package com.userInventory.service;

import org.springframework.data.domain.Pageable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.userInventory.dto.ProductDTO;
import com.userInventory.repository.CategoryRepository;
import com.userInventory.repository.ProductRepository;
import com.userInventory.repository.entity.Category;
import com.userInventory.repository.entity.Product;
import com.userInventory.repository.entity.ProductStock;


@Service
public class ProductService {
	@Autowired
	ProductRepository product_repo;
	
	@Autowired
	CategoryRepository category_repo;
	//ENTITY TO DTO
	private ProductDTO convertToDTO(Product product) {
		ProductDTO dto=new ProductDTO();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setDescription(product.getDescription());
		dto.setPrice(product.getPrice());
		dto.setImgUrl(product.getImgUrl());
		
		if(product.getCategory()!=null) {
			dto.setCategoryType(product.getCategory().getCategoryType());
			dto.setCategoryId(product.getCategory().getCategoryId());
		}
		
		if(product.getStock()!=null) {
			dto.setStockQuantity(product.getStock().getQuantity());
		}
		
		return dto;
	}
	//DTO TO ENTITY
	private Product convertToEntity(ProductDTO dto) {
		Product product=new Product();
		product.setProductName(dto.getProductName());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setImgUrl(dto.getImgUrl());
		 if (dto.getStockQuantity() > 0) {
		        ProductStock stock = new ProductStock();
		        stock.setQuantity(dto.getStockQuantity()); // ✅ quantity is the field name
		        stock.setProduct(product);                 // ✅ link back to product
		        product.setStock(stock);
		    }
		// Set Category
		 if(dto.getCategoryType() != null) {

		        Category category = new Category();
		        category.setCategoryType(dto.getCategoryType());

		        product.setCategory(category);
		    }
		return product;
	}
	
	//GET PRODUCTS BY PAGES
	public Map<String, Object> getAllPages(int page,int size){
		Pageable pageable=PageRequest.of(page, size);
		 Page<Product> productPage =
		            product_repo.findAll(pageable);

		    List<ProductDTO> content = productPage.getContent()
		            .stream()
		            .map(this::convertToDTO)
		            .collect(Collectors.toList());
		    Map<String, Object> response = new LinkedHashMap<>();
		    response.put("products",     content);
		    response.put("currentPage",  productPage.getNumber());
		    response.put("totalItems",   productPage.getTotalElements());
		    response.put("totalPages",   productPage.getTotalPages());
		    response.put("isLastPage",   productPage.isLast());

		    return response;
	}
	
	//SEARCH BY NAME
	public List<ProductDTO> searchByName(String name) {
	    return product_repo
	           .findByProductNameContainingIgnoreCase(name)
	           .stream()
	           .map(this::convertToDTO)
	           .collect(Collectors.toList());
	}
	
	//GET BY CATEGORY
	public List<ProductDTO> getByCategory(int categoryId) {
	    return product_repo
	           .findByCategory_CategoryId(categoryId)
	           .stream()
	           .map(this::convertToDTO)
	           .collect(Collectors.toList());
	}
	//VIEW PRODUCTS
	public List<ProductDTO> viewProducts(){
		return product_repo.findAll().
				stream().map(this::convertToDTO).
				collect(Collectors.toList());
	}
	
	//CREATE NEW PRODUCT
	public ProductDTO createProduct(ProductDTO dto) {
		Product product=convertToEntity(dto);
		Product saved=product_repo.save(product);
		return convertToDTO(saved);
	}
	
	//SEARCH PRODUCT BY ID
	public Optional<ProductDTO> searchProduct(int id) {
		return product_repo.findById(id).map(this::convertToDTO);
	}
	
	//UPDATE PRODUCT BY GETTING ID
	public Optional<ProductDTO> updateProduct(int id,ProductDTO dto) {
		return product_repo.findById(id).map(existing ->{
				
		existing.setProductName(dto.getProductName());
		existing.setDescription(dto.getDescription());
		existing.setPrice(dto.getPrice());
		existing.setImgUrl(dto.getImgUrl());
		 // Set Category
        if(dto.getCategoryId() != 0) {

            Category category = category_repo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category Not Found"));

            existing.setCategory(category);
        }
        // Update Stock ✅
        if (existing.getStock() != null) {
            existing.getStock().setQuantity(dto.getStockQuantity()); // update existing stock
        } else {
            ProductStock stock = new ProductStock();
            stock.setQuantity(dto.getStockQuantity());
            stock.setProduct(existing);
            existing.setStock(stock);
        }

		Product update=product_repo.save(existing);
		
		return convertToDTO(update);});
	}
	
	//DELETE PRODUCT BY ID
	public String deleteProduct(int id) {
		if(product_repo.existsById(id)) {
			product_repo.deleteById(id);
			return "Product Deleted Successfully!";
		}
		return "Product Not Found With ID:"+id;
	}

}
