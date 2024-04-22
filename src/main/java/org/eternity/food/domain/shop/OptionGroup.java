package org.eternity.food.domain.shop;

//import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.DomainEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public class OptionGroup extends DomainEntity<OptionGroup, Long> {
    @Id
    private Long id;

    private String name;
    private boolean mandatory;

    @MappedCollection(idColumn = "OPTION_GROUP_ID")
    private Set<Option> options = new HashSet<>();

    public OptionGroup(String name, boolean mandatory, Option option) {
        this(null, name, mandatory, new HashSet<>(Arrays.asList(option)));
    }

    @Builder
    public OptionGroup(Long id, String name, boolean mandatory, Set<Option> options) {
        this.id = id;
        this.mandatory = mandatory;
        setName(name);
        setOptions(options);
    }

    OptionGroup() {
    }

    private void setName(String name) {
        if (name == null || name.length() < 2) {
            throw new IllegalArgumentException("옵션그룹명은 길이는 최소 2글자 이상이어야 합니다.");
        }

        this.name = name;
    }

    private void setOptions(Set<Option> options) {
        if (options == null || options.size() < 1) {
            throw new IllegalArgumentException("옵션의 길이는 최소 1개 이상이어야 합니다.");
        }

        this.options = options;
    }

    public Optional<Option> findOption(Option target) {
        return options.stream().filter(option -> option.equals(target)).findFirst();
    }

    public boolean isFree() {
        return options.stream().allMatch(Option::isFree);
    }

    public void chaneName(String name) {
        this.name = name;
    }

    public void changeOptionName(Option target, String optionName) {
        Option option = options.stream().filter(each -> each.equals(target)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
        options.remove(option);
        options.add(target.changeName(optionName));
    }
}
