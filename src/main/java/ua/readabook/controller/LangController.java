package ua.readabook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.readabook.domain.LangDTO;
import ua.readabook.service.LangService;

import java.util.List;

@RestController
@RequestMapping("langs")
public class LangController {

    @Autowired
    private LangService langService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LangDTO langDTO) {
        langService.createLang(langDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<LangDTO> langDTOS = langService.findAllLangs();
        return new ResponseEntity<List<LangDTO>>(langDTOS, HttpStatus.OK);
    }

    @GetMapping("{langId}")
    public ResponseEntity<?> getById(@PathVariable("langId") Long id) {
        LangDTO langDTO = langService.findLangById(id);
        return new ResponseEntity<LangDTO>(langDTO, HttpStatus.OK);
    }
}
