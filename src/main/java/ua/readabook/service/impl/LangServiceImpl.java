package ua.readabook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.readabook.domain.LangDTO;
import ua.readabook.entity.LangEntity;
import ua.readabook.repository.LangRepository;
import ua.readabook.service.LangService;
import ua.readabook.utils.ObjectMapperUtils;

import java.util.List;

@Service
public class LangServiceImpl implements LangService {

    @Autowired
    private LangRepository langRepository;

    @Autowired
    private ObjectMapperUtils modelMapper;

    @Override
    public void createLang(LangDTO langDTO) {
        LangEntity langEntity =
                modelMapper.map(langDTO, LangEntity.class);
        langRepository.save(langEntity);
    }

    @Override
    public List<LangDTO> findAllLangs() {
        List<LangEntity> langEntities =
                langRepository.findAll();
        List<LangDTO> langDTOS =
                modelMapper.mapAll(langEntities, LangDTO.class);
        return langDTOS;
    }

    @Override
    public LangDTO findLangById(Long id) {
        LangEntity langEntity = langRepository.findById(id).get();
        LangDTO langDTO = modelMapper.map(langEntity, LangDTO.class);
        return langDTO;
    }
}
