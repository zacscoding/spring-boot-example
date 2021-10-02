package io.spring.batch.file;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ExampleProfile profile = ExampleProfile.FLAT_FILE_SINGLE_FIXED_LENGTH;
        // ExampleProfile profile = ExampleProfile.FLAT_FILE_DELIMITER;
        // ExampleProfile profile = ExampleProfile.FLAT_FILE_CUSTOM_FIELD_MAPPER;
        // ExampleProfile profile = ExampleProfile.FLAT_FILE_SINGLE_CUSTOM_LINE_TOKENIZER;
        // ExampleProfile profile = ExampleProfile.FLAT_FILE_MULTI_OBJECT;
        // ExampleProfile profile = ExampleProfile.FLAT_FILE_XML;
        //ExampleProfile profile = ExampleProfile.FLAT_FILE_JSON;

        switch (profile) {
            case FLAT_FILE_SINGLE_FIXED_LENGTH:
                System.setProperty("spring.profiles.active", "flat-file-single,flat-file-single-fixed-length");
                args = new String[] {
                        "customerFile=input/customerFixedWidth.txt"
                };
                break;
            case FLAT_FILE_SINGLE_DELIMITER:
                System.setProperty("spring.profiles.active", "flat-file-single,flat-file-single-delimiter");
                args = new String[] {
                        "customerFile=input/customerDelimiter.txt"
                };
                break;
            case FLAT_FILE_SINGLE_CUSTOM_FIELD_MAPPER:
                System.setProperty("spring.profiles.active", "flat-file-single,flat-file-single-custom-field-mapper");
                args = new String[] {
                        "customerFile=input/customerDelimiter.txt"
                };
                break;
            case FLAT_FILE_SINGLE_CUSTOM_LINE_TOKENIZER:
                System.setProperty("spring.profiles.active", "flat-file-single,flat-file-single-custom-line-tokenizer");
                args = new String[] {
                        "customerFile=input/customerDelimiter.txt"
                };
                break;
            case FLAT_FILE_MULTI_OBJECT:
                System.setProperty("spring.profiles.active", "flat-file-multi-object");
                args = new String[] {
                        "customerFile=input/customerMultiFormat.csv"
                };
                break;
            case FLAT_FILE_XML:
                System.setProperty("spring.profiles.active", "flat-file-xml");
                args = new String[] {
                        "customerFile=input/customer.xml"
                };
                break;
            case FLAT_FILE_JSON:
                System.setProperty("spring.profiles.active", "flat-file-json");
                args = new String[] {
                        "customerFile=input/customer.json"
                };
                break;
        }

        SpringApplication.run(DemoApplication.class, args);
    }

    public enum ExampleProfile {
        FLAT_FILE_SINGLE_FIXED_LENGTH,
        FLAT_FILE_SINGLE_DELIMITER,
        FLAT_FILE_SINGLE_CUSTOM_FIELD_MAPPER,
        FLAT_FILE_SINGLE_CUSTOM_LINE_TOKENIZER,
        FLAT_FILE_MULTI_OBJECT,
        FLAT_FILE_XML,
        FLAT_FILE_JSON
    }
}
