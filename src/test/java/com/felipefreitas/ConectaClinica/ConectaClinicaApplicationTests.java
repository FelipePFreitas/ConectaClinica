package com.felipefreitas.ConectaClinica;

import org.junit.jupiter.api.Test;

class ConectaClinicaApplicationTests {

	@Test
	void deveExistirClassePrincipalDaAplicacao() {
		org.assertj.core.api.Assertions.assertThat(ConectaClinicaApplication.class).isNotNull();
	}

}
