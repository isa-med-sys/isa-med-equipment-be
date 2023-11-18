package com.isa.med_equipment.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperImpl implements Mapper {

    private final ModelMapper modelMapper;

    public MapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <T, U> U map(T source, Class<U> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    @Override
    public <T, U> Page<U> mapPage(Page<T> sourcePage, Class<U> targetClass) {
        List<U> content = mapList(sourcePage.getContent(), targetClass);
        return new PageImpl<>(content, sourcePage.getPageable(), sourcePage.getTotalElements());
    }

    private <T, U> List<U> mapList(List<T> sourceList, Class<U> targetClass) {
        return sourceList.stream()
                .map(source -> map(source, targetClass))
                .collect(Collectors.toList());
    }
}
