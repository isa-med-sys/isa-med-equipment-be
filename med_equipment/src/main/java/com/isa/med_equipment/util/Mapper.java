package com.isa.med_equipment.util;

import org.springframework.data.domain.Page;

public interface Mapper {

    <T, U> U map(T source, Class<U> targetClass);

    <T, U> Page<U> mapPage(Page<T> sourcePage, Class<U> targetClass);
}
