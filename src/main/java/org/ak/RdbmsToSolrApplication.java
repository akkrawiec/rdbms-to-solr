package org.ak;

import org.ak.service.SolrImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RdbmsToSolrApplication implements CommandLineRunner {
	@Autowired
	SolrImportService solrImportService;

	public static void main(String[] args) {
		SpringApplication.run(RdbmsToSolrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		solrImportService.importDataParallel();
	}
}
