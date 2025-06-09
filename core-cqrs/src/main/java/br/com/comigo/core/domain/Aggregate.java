package br.com.comigo.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.comigo.core.domain.command.Command;
import br.com.comigo.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Slf4j
public abstract class Aggregate {

    protected final UUID aggregateId;

    @JsonIgnore
    protected final List<Event> occurredEvents = new ArrayList<>();

    protected int version;

    @JsonIgnore
    protected int baseVersion;

    protected Aggregate(@NonNull UUID aggregateId, int version) {
        this.aggregateId = aggregateId;
        this.baseVersion = this.version = version;
    }

    @Nonnull
    public abstract String getAggregateType();

    /**
     * Caso, no instante da invocação de loadFromHistory, occurredEvents precisa
     * estar vazio. A reconstituição do estado do agregado precisa ocorrer por via
     * dos eventos carregados pelo argumento events.
     * 
     * É importante estar atento para a necessidade de, evento a evento, o agregado
     * precisar ser novamente aplicado/processado.
     * 
     */
    public void loadFromHistory(List<Event> events) {
        if (!occurredEvents.isEmpty()) {
            throw new IllegalStateException("Aggregate has non-empty occurredEvents");
        }
        events.forEach(event -> {
            if (event.getVersion() <= this.version) {
                throw new IllegalStateException("Event version %s <= aggregate base version %s"
                        .formatted(event.getVersion(), this.getNextVersion()));
            }
            this.apply(event);
            this.baseVersion = this.version = event.getVersion();
        });
    }

    protected int getNextVersion() {
        return this.version + 1;
    }

    /**
     * Com esse método os eventos são aplicados, sinalizando a modificação do estado
     * do agregado.
     * 
     * Assim, modifica-se também a versão do agregado que precisará ser atualizado.
     * 
     */
    protected void applyChange(Event event) {
        if (event.getVersion() != this.getNextVersion()) {
            throw new IllegalStateException("Event version %s doesn't match expected version %s"
                    .formatted(event.getVersion(), this.getNextVersion()));
        }
        this.apply(event);
        this.occurredEvents.add(event);
        this.version = event.getVersion();
    }

    private void apply(Event event) {
        log.debug("Applying event {}", event);

        /**
         * O metodo process será invocado na instancia concreta do agregado.
         * 
         */
        this.invoke(event, "apply");
    }

    public void process(Command command) {
        log.debug(" >>> processing command {}", command);
        this.invoke(command, "process");
    }

    @SneakyThrows(InvocationTargetException.class)
    private void invoke(Object o, String methodName) {
        try {
            log.info(" >>> invoking method {} on o {}.", methodName, o);
            Method method = this.getClass().getMethod(methodName, o.getClass());
            log.info(" > method found {}", method.getName());
            method.invoke(this, o);
            log.info(" > method execution was finished.");
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new UnsupportedOperationException("Aggregate %s doesn't support %s(%s)".formatted(this.getClass(),
                    methodName, o.getClass().getSimpleName()), e);
        }
    }
}
