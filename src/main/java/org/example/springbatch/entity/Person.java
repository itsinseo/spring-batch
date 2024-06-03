package org.example.springbatch.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

@Getter
@Setter
public class Person implements ResourceAware {

    private String firstName;
    private String lastName;
    private Resource resource;
    private String inputSourceFileName;

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        this.inputSourceFileName = resource.getFilename();
    }
}
