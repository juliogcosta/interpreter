package br.com.comigo.assistencia.infrastructure.config.component;

import java.util.Arrays;

import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToListConverter implements Converter<String, List<String>> {
    @Override
    public List<String> convert(String source) {
        return Arrays.asList(source.split(","));
    }
}