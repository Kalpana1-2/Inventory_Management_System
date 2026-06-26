package com.userInventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userInventory.dto.UserAddressDTO;
import com.userInventory.repository.UserAddressRepository;
import com.userInventory.repository.UserRepository;
import com.userInventory.repository.entity.UserAddress;
import com.userInventory.repository.entity.Users;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository addressRepo;

    @Autowired
    private UserRepository userRepo;

    // ENTITY → DTO
    private UserAddressDTO convertToDTO(UserAddress addr) {

        UserAddressDTO dto = new UserAddressDTO();

        dto.setUserId(addr.getUserID());

        if (addr.getUser() != null) {
            dto.setUserName(addr.getUser().getUserName());
        }

        dto.setHouseNo(addr.getHouseNo());

        dto.setStreet(addr.getStreet());
        dto.setCity(addr.getCity());
        dto.setDistt(addr.getDistt());
        dto.setState(addr.getState());
        dto.setPinCode(addr.getPincode());

        return dto;
    }

    // DTO → ENTITY
    private UserAddress convertToEntity(UserAddressDTO dto) {

        UserAddress addr = new UserAddress();

        addr.setHouseNo(dto.getHouseNo());

        addr.setStreet(dto.getStreet());

        addr.setCity(dto.getCity());
        addr.setDistt(dto.getDistt());
        addr.setState(dto.getState());
        addr.setPincode(dto.getPinCode());

        Users user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        addr.setUser(user);

        return addr;
    }

    // ADD ADDRESS
    public UserAddressDTO addAddress(UserAddressDTO dto){

        System.out.println(dto.getUserId());

        Users user=userRepo.findById(dto.getUserId())
                .orElseThrow(
                ()->new RuntimeException(
                "User not found"));

        System.out.println(user.getUserId());

        UserAddress address=convertToEntity(dto);

        UserAddress saved=addressRepo.save(address);

        return convertToDTO(saved);

    }

    // VIEW ALL
    public List<UserAddressDTO> viewAll() {

        return addressRepo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // SEARCH BY USER ID
    public Optional<UserAddressDTO> getByUserId(int userId) {

        return addressRepo.findById(userId)
                .map(this::convertToDTO);
    }

    // UPDATE ADDRESS
    public UserAddressDTO updateAddress(int userId, UserAddressDTO dto) {

        UserAddress addr = addressRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addr.setCity(dto.getCity());
        addr.setDistt(dto.getDistt());
        addr.setState(dto.getState());
        addr.setPincode(dto.getPinCode());

        UserAddress updated = addressRepo.save(addr);

        return convertToDTO(updated);
    }

    // DELETE ADDRESS
    public void deleteAddress(int userId) {

        addressRepo.deleteById(userId);
    }
}