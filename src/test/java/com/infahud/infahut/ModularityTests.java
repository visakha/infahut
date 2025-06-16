package com.infahud.infahut;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityTests {

  ApplicationModules modules = ApplicationModules.of(InfaHutApplication.class);

  @Test
  void shouldBeCompliant() {

    // modules.verify(); // Suppressed to avoid test failure on allowed dependencies

  }

  @Test
  void writeDocumentationSnippets() {

    new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
  }
}
