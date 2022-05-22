package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpringUploadDownloadApplicationTests {
@Autowired
private FileRepo repo;

@Autowired
private TestEntityManager entityManager;
	@Test
    @Rollback(false)
	void testUpload() throws IOException {
		File file = new File("D:\\Downloads\\Word.docx");

		BootFile bootFile = new BootFile();
		bootFile.setName(file.getName());

		byte[] bytes = Files.readAllBytes(file.toPath());
		bootFile.setContent(bytes);
		long fileSize = bytes.length;
		bootFile.setSize(fileSize);
		bootFile.setUploadTime(new Date());

		BootFile saved = repo.save(bootFile);
BootFile exist = entityManager.find(BootFile.class, saved.getId());

assertThat(exist.getSize()).isEqualTo(fileSize);

	}

}
