package co.com.pragma.model.state;

import co.com.pragma.model.mock.StateMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void builderAndGettersTest() {
        State state = State.builder()
                .id(1)
                .name("Aprobado")
                .description("Estado de aprobación de la solicitud")
                .build();

        assertEquals(1, state.getId());
        assertEquals("Aprobado", state.getName());
        assertEquals("Estado de aprobación de la solicitud", state.getDescription());
    }

    @Test
    void toBuilderTest() {
        State original = State.builder()
                .id(2)
                .name("Rechazado")
                .description("Solicitud rechazada")
                .build();

        State copy = original.toBuilder()
                .name("Pendiente")
                .description("Pendiente de revisión")
                .build();

        assertEquals(original.getId(), copy.getId());
        assertEquals("Pendiente", copy.getName());
        assertEquals("Pendiente de revisión", copy.getDescription());
    }

    @Test
    void mockSampleTest() {
        State sample = StateMock.sample();
        assertEquals(1, sample.getId());
        assertEquals("Pendiente de revisión", sample.getName());
        // Actualizamos la comprobación para que coincida con el mock
        assertEquals("Estado inicial de la solicitud de préstamo", sample.getDescription());
    }

    @Test
    void mockSampleUpdatedTest() {
        State sampleUpdated = StateMock.sampleUpdated();
        assertEquals(1, sampleUpdated.getId());
        assertEquals("Aprobado", sampleUpdated.getName());
        assertEquals("Solicitud aprobada y lista para procesar", sampleUpdated.getDescription());
    }
}
