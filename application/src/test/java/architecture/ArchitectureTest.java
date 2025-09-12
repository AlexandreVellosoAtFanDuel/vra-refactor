package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Domain Architecture Tests")
class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.betfair.video");
    }

    @ParameterizedTest
    @DisplayName("Domain layer should not depend on other packages and libs")
    @ValueSource(strings = {
            "..config..",
            "..infra..",
            "jakarta..",
            "javax.persistence.."
    })
    void domainShouldNotDependOnConfig(String arg) {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage(arg);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain services should only be accessed through ports")
    void domainServicesShouldOnlyBeAccessedThroughPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.service..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage("..domain.service..", "..config..", "..domain.port..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain ports should not have implementation details")
    void domainPortsShouldNotHaveImplementationDetails() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.port..")
                .should().beInterfaces()
                .orShould().beAnnotatedWith("FunctionalInterface");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Infrastructure adapters should implement domain ports")
    void infraAdaptersShouldImplementDomainPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..output.adapter..")
                .and().haveSimpleNameEndingWith("Adapter")
                .should().dependOnClassesThat()
                .resideInAPackage("..domain.port..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Configuration classes should reside in config package")
    void configurationClassesShouldResideInConfigPackage() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.context.annotation.Configuration")
                .should().resideInAPackage("..config..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should reside in input rest package")
    void controllersShouldResideInInfraInputPackage() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .or().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..input.rest..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain entities should not use framework annotations")
    void domainEntitiesShouldNotUseFrameworkAnnotations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain.dto.entity..")
                .should().beAnnotatedWith("org.springframework.stereotype.Component")
                .orShould().beAnnotatedWith("jakarta.persistence.Entity")
                .orShould().beAnnotatedWith("javax.persistence.Entity");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain value objects should be immutable")
    void domainValueObjectsShouldBeImmutable() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.dto.valueobject..")
                .and().areNotEnums()
                .should().beRecords()
                .orShould().haveOnlyFinalFields();

        rule.check(importedClasses);
    }

}
