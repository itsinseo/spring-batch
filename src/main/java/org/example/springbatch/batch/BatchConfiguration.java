package org.example.springbatch.batch;

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
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfiguration {

    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("data/GEN-2.csv"))
                .delimited()
                .names("firstName", "lastName")
                .targetType(Person.class)
                .build();
    }

    // TODO: implement multi file reading delegating FlatFileItemReader
//    @Bean
//    public MultiResourceItemReader<Person> reader() throws IOException {
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources = resolver.getResources("classpath:data/*.csv");
//
//        return new MultiResourceItemReaderBuilder<Person>()
//                .name("multiPersonItemReader")
//                .resources(resources)
//                .build();
//    }

    @Bean
    public PersonToUserItemProcessor processor() {
        return new PersonToUserItemProcessor();
    }

//    @Bean
//    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<Person>()
//                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
//                .dataSource(dataSource)
//                .beanMapped()
//                .build();
//    }

    // TODO: make way to config db name and db collection using reader's filename
    @Bean
    public MongoItemWriter<User> writer(MongoTemplate mongoTemplate) {
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

//    @Bean
//    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
//                      FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
//        return new StepBuilder("step1", jobRepository)
//                .<Person, Person>chunk(3, transactionManager) // generic method representing <input, output>
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<Person> reader, PersonToUserItemProcessor processor, MongoItemWriter<User> writer) {
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
