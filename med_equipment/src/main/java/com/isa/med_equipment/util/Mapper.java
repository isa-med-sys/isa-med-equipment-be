package com.isa.med_equipment.util;

import org.springframework.data.domain.Page;

import java.util.List;

public interface Mapper {

    <T, U> U map(T source, Class<U> targetClass);

    <T, U> Page<U> mapPage(Page<T> sourcePage, Class<U> targetClass);

    <T, U> List<U> mapList(List<T> sourceList, Class<U> targetClass);
}
