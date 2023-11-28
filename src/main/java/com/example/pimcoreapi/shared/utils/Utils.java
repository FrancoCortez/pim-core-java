package com.example.pimcoreapi.shared.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
@RequiredArgsConstructor
public class Utils {

    public String generateCode (String text) {
        String normalizeText = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
        String[] works = normalizeText.split("\\s+");
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < works.length; i++) {
            String work = works[i].replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (i > 0) {
                work = work.substring(0, 1).toUpperCase() + work.substring(1);
            }
            code.append(work);
        }
        return code.toString();
    }
}
