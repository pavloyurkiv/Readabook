package ua.readabook.service;

import ua.readabook.domain.LangDTO;

import java.util.List;

public interface LangService {

    void createLang(LangDTO langDTO);

    List<LangDTO> findAllLangs();

    LangDTO findLangById(Long id);

}
