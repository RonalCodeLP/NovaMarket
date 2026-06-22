package com.upeu.ordenms.servicio;

import com.upeu.ordenms.evento.EventoOrden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductorOrden {

    private final KafkaTemplate<String, EventoOrden> kafkaTemplate;
    @Value("${app.kafka.topic.ordenes}")
    private String topicOrdenes;

    @Value("${app.kafka.enabled:false}")
    private boolean kafkaEnabled;

    public void publicarOrdenCreada(EventoOrden eventoOrden) {
        if (!kafkaEnabled) {
            return;
        }
        try {
            kafkaTemplate.send(topicOrdenes, String.valueOf(eventoOrden.getOrdenId()), eventoOrden)
                    .whenComplete((resultado, ex) -> {
                        if (ex != null) {
                            log.error(
                                    "service=ms-venta component=producer topic={} ordenId={} status=error error=\"{}\"",
                                    topicOrdenes,
                                    eventoOrden.getOrdenId(),
                                    ex.getMessage()
                            );
                            return;
                        }

                        log.info(
                                "service=ms-venta component=producer topic={} partition={} offset={} ordenId={} status=published",
                                resultado.getRecordMetadata().topic(),
                                resultado.getRecordMetadata().partition(),
                                resultado.getRecordMetadata().offset(),
                                eventoOrden.getOrdenId()
                        );
                    });
        } catch (Exception ex) {
            log.warn("Kafka no disponible, evento orden {} omitido: {}", eventoOrden.getOrdenId(), ex.getMessage());
        }
    }
}
