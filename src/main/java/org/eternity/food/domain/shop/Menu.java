package org.eternity.food.domain.shop;

import lombok.Builder;
import lombok.Getter;
import org.eternity.base.domain.AggregateRoot;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public class Menu extends AggregateRoot<Menu, Long> {
    @Id
    private Long id;

    @Column("SHOP_ID")
    private AggregateReference<Shop, Long> shop;

    @Column("MENU_NAME")
    private String name;

    @Column("MENU_DESCRIPTION")
    private String description;

    private boolean open;

    @MappedCollection(idColumn = "MENU_ID")
    private Set<OptionGroup> groups = new HashSet<>();

    public Menu(AggregateReference<Shop, Long> shop, String name, String description) {
        this(null, shop, name, description, false, new HashSet<>());
    }

    @Builder
    public Menu(Long id, AggregateReference<Shop, Long> shop, String name, String description, boolean open, Set<OptionGroup> groups) {
        this.id = id;
        this.shop = shop;
        this.name = name;
        this.description = description;
        this.open = open;
        this.groups = groups;
    }

    Menu() {
    }

    public void addOptionGroup(OptionGroup optionGroup) {
        if (optionGroup == null) {
            throw new IllegalArgumentException();
        }

        if (groups.stream().anyMatch(group -> group.getName().equals(optionGroup.getName()))) {
            throw new IllegalArgumentException("이름이 동일한 옵션그룹이 이미 존재합니다.");
        }

        if (isOpen() && optionGroup.isMandatory() && countOfMandatoryOptionGroups() >= 3) {
            throw new IllegalArgumentException("필수 옵션 그룹은 3개까지 등록할 수 있습니다.");
        }

        groups.add(optionGroup);
    }

    public void open() {
        if (groups.isEmpty()) {
            throw new IllegalStateException("옵션그룹은 하나 이상 존재해야 합니다.");
        }

        if (countOfMandatoryOptionGroups() == 0) {
            throw new IllegalStateException("필수 옵션그룹은 최소 1개는 등록되어 있어야 합니다.");
        }

        if (countOfMandatoryOptionGroups() > 3) {
            throw new IllegalStateException("필수 옵션그룹은 3개까지만 등록가능 합니다.");
        }

        if (countOfFreeOptionGroups() < 1) {
            throw new IllegalStateException("금액이 설정된 옵션그룹이 최소 1개는 등록되어 있어야 합니다.");
        }

        open = true;
    }
    public void removeOptionGroup(Long optionGroupId) {
        if (optionGroupId == null) {
            throw new IllegalArgumentException("파라미터는 null이어서는 안됩니다.");
        }

        if (groups.isEmpty()) {
            throw new IllegalArgumentException("옵션그룹이 비어있습니다.");
        }

        OptionGroup optionGroup =
                groups.stream()
                        .filter(group -> group.getId().equals(optionGroupId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException());

        if (!isOpen()) {
            groups.remove(optionGroup);
            return;
        }

        if (groups.size() == 1) {
            throw new IllegalArgumentException("옵션그룹은 최소 1개는 등록되어 있어야 합니다.");
        }

        if (optionGroup.isMandatory() && countOfMandatoryOptionGroups() == 1) {
            throw new IllegalArgumentException("필수 옵션그룹은 최소 1개는 등록되어 있어야 합니다.");
        }

        if (!optionGroup.isFree() && countOfFreeOptionGroups() == 1) {
            throw new IllegalArgumentException("금액이 설정된 옵션그룹이 최소 1개는 등록되어 있어야 합니다.");
        }

        groups.remove(optionGroup);
    }

    public void changeOptionGroupName(Long optionGroupId, String name) {
        if (optionGroupId == null) {
            throw new IllegalArgumentException("파라미터는 null이어서는 안됩니다.");
        }

        if (groups.isEmpty()) {
            throw new IllegalArgumentException("옵션그룹이 비어있습니다.");
        }

        if (groups.stream().anyMatch(group -> group.getName().equals(name))) {
            throw new IllegalArgumentException("이름이 동일한 옵션그룹이 이미 존재합니다.");
        }

        OptionGroup optionGroup =
                groups.stream()
                        .filter(group -> group.getId().equals(optionGroupId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException());

        optionGroup.chaneName(name);
    }

    public void changeOptionName(Long optionGroupId, Option target, String optionName) {
        if (optionGroupId == null) {
            throw new IllegalArgumentException("optionGroupId 파라미터는 null이어서는 안됩니다.");
        }

        if (target == null) {
            throw new IllegalArgumentException("oldOption 파라미터는 null이어서는 안됩니다.");
        }

        if (groups.isEmpty()) {
            throw new IllegalArgumentException("옵션그룹이 비어있습니다.");
        }

        OptionGroup optionGroup =
                groups.stream()
                        .filter(group -> group.getId().equals(optionGroupId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException());

        optionGroup.changeOptionName(target, optionName);
    }

    private long countOfMandatoryOptionGroups() {
        return groups.stream().filter(group -> group.isMandatory()).count();
    }

    private long countOfFreeOptionGroups() {
        return groups.stream().filter(group -> !group.isFree()).count();
    }

    public Optional<OptionGroup> getOptionGroup(Long optionGroupId) {
        return groups.stream().filter(group -> group.getId().equals(optionGroupId)).findFirst();
    }

    public Optional<OptionGroup> getOptionGroup(String name) {
        return groups.stream().filter(group -> group.getName().equals(name)).findFirst();
    }
}
