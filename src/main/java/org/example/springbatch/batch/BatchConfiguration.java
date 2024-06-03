package org.example.springbatch.batch;

import lombok.RequiredArgsConstructor;
import org.example.springbatch.config.MongoConfig;
import org.example.springbatch.entity.Person;
import org.example.springbatch.entity.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final MongoConfig mongoConfig;

    @Bean
    public FlatFileItemReader<Person> itemReader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .delimited()
                .names("firstName", "lastName")
                .targetType(Person.class)
                .build();
    }

    @Bean
    public MultiResourceItemReader<Person> multiReader() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:data/*.csv");

        return new MultiResourceItemReaderBuilder<Person>()
                .name("multiPersonItemReader")
                .delegate(itemReader())
                .resources(resources)
                .build();
    }

    @Bean
    public PersonToUserItemProcessor processor() {
        return new PersonToUserItemProcessor();
    }

    // TODO: make way to config db name and db collection using reader's filename
    @Bean
    public MongoItemWriter<User> writer() {
        MongoTemplate mongoTemplate = mongoConfig.mongoTemplate("testdb");

        return new MongoItemWriterBuilder<User>()
                .template(mongoTemplate)
                .collection("users")
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionListener jobCompletionListener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(jobCompletionListener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      MultiResourceItemReader<Person> reader, PersonToUserItemProcessor processor, MongoItemWriter<User> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, User>chunk(3, transactionManager) // generic method representing <input, output>
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // disable automatic bean exposure of JobRegistryBeanPostProcessor
    @Bean
    public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
        return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
    }
}
