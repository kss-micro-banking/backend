package com.kss.backend.customer;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.kss.backend.customer.dto.CustomerUpdateDto;

/**
 * CustomerMapper
 */
@Component
public class CustomerMapper {

    private ModelMapper modelMapper;

    public CustomerMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPropertyCondition(Conditions.isNotNull());
    }

    void updateFromDto(CustomerUpdateDto dto, Customer customer) {
        modelMapper.map(dto, customer);
    }

}
