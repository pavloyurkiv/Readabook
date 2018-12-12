package ua.readabook;


import java.io.*;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ua.readabook.entity.*;
import ua.readabook.exception.ResourceNotFoundException;
import ua.readabook.repository.*;


@EnableWebMvc
@SpringBootApplication
public class ReadabookApplication implements CommandLineRunner, WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ReadabookApplication.class, args);
    }

    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE");
	}
    
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

	@Autowired
    private LangRepository langRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

    	if (roleRepository.count() == 0) {
    		RoleEntity entity = new RoleEntity();
    		entity.setName("ADMIN");

    		roleRepository.save(entity);

    		entity = new RoleEntity();
    		entity.setName("USER");

    		roleRepository.save(entity);
    	}

    	if (userRepository.count() == 0) {
    		UserEntity user = new UserEntity();
    		user.setUsername("admin");
    		user.setPassword(passwordEncoder.encode("123"));

    		RoleEntity role = roleRepository.findByName("ADMIN")
    				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

    		Set<RoleEntity> roles = new HashSet<>();
    		roles.add(role);
    		user.setRoles(roles);

    		userRepository.save(user);
    	}


		if(bookRepository.count() == 0) {



			try {
				File file = new File("startbase/categoryList.txt");
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				String line = reader.readLine();
				while (line != null) {
					CategoryEntity category = new CategoryEntity();
					category.setName(line);
					categoryRepository.save(category);
					System.out.println(line);
					line = reader.readLine();


				}
				fr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				File file = new File("startbase/langList.txt");
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				String line = reader.readLine();
				while (line != null) {
					LangEntity lang2 = new LangEntity();
					lang2.setName(line);
					langRepository.save(lang2);
					System.out.println(line);
					line = reader.readLine();


				}
				fr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}



			CategoryEntity category2 = new CategoryEntity();
			category2.setName("Інші");
			categoryRepository.save(category2);

			LangEntity lang2 = new LangEntity();
			lang2.setName("Інша");
			langRepository.save(lang2);



			for (int i = 0; i < 20; i++) {

				BookEntity book = new BookEntity();
				book.setName("Назва книги: Том " + (i+1));
				book.setAuthor("Джон Доу");
				book.setImage("default.jpg");
				book.setAnnotation("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam pretium bibendum magna ac malesuada. Maecenas non lacinia ipsum. Etiam laoreet urna ut nibh tempor interdum. Integer hendrerit, est nec commodo congue, lacus lacus facilisis tortor, vel ornare ante arcu ac magna. Praesent varius dolor quis rhoncus condimentum. Curabitur ligula lectus, ullamcorper at ex ut, volutpat bibendum augue. Sed scelerisque, nulla id pharetra sollicitudin, ante nunc volutpat tortor, a pulvinar lacus massa at augue. Maecenas volutpat mauris et ligula posuere, quis dictum enim consequat.#" + i);
				book.setLink("https://rickrolled.fr/");
				book.setCategory(category2);
				book.setLang(lang2);

				bookRepository.save(book);


			}


			System.out.println();

		}

		System.out.println("Сервер успішно запущено!");

    }
}
