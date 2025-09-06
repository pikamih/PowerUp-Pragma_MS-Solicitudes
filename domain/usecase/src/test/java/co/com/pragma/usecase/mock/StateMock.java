package co.com.pragma.usecase.mock;

import co.com.pragma.model.state.State;

public final class StateMock {

    private StateMock() {}

    public static State sample() {
        return State.builder()
                .id(1)
                .name("Pendiente de revisión")
                .description("Estado inicial de la solicitud de préstamo")
                .build();
    }

    public static State sampleUpdated() {
        return State.builder()
                .id(1)
                .name("Aprobado")
                .description("Solicitud aprobada y lista para procesar")
                .build();
    }
}
